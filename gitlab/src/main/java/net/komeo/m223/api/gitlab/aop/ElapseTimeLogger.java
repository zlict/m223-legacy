package net.komeo.m223.api.gitlab.aop;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import net.komeo.m223.api.gitlab.control.HitCounter;

public class ElapseTimeLogger {

    @EJB
    HitCounter hitCounter;

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        hitCounter.incCount();
        long invoke = System.nanoTime();
        String log = this.getClass().getName() + " calculated access time:\n\t";

        try {
            log += ctx.getTarget().getClass().getName();
            log += "." + ctx.getMethod().getName();
            return ctx.proceed();
        } finally {
            long time = System.nanoTime() - invoke;
            log += "\n\telaspsed " + NANOSECONDS.toSeconds(time) + " seconds";
            log += "\n\telaspsed " + NANOSECONDS.toMillis(time) + " milli seconds";
            log += "\n\telaspsed " + NANOSECONDS.toMicros(time) + " micro seconds";
            log += "\n\telaspsed " + NANOSECONDS.toNanos(time) + " nanno seconds";
            System.out.println(log);
        }
    }
}
