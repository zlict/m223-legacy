package net.komeo.m223.api.gitlab.boundary;

import net.komeo.m223.api.gitlab.control.HitCounter;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import net.komeo.m223.api.gitlab.aop.ElapseTimeLogger;
import net.komeo.m223.api.gitlab.control.UserDAO;
import net.komeo.m223.api.gitlab.control.CallMicroService;
import net.komeo.m223.api.gitlab.entity.KomeoUser;
import org.gitlab4j.api.models.User;

@Interceptors(ElapseTimeLogger.class)
@Stateless
public class Engine {

    @Inject
    CallMicroService callMicroService;

    @EJB
    UserDAO userDAO = null;

    @EJB
    HitCounter hitCounter = null;

    @PostConstruct
    void testDependencyInjection() {
        if (callMicroService == null || userDAO == null || hitCounter == null) {
            System.out.println(this.getClass().getName() + " failed on Dependency Injection");
        }
    }

    public List<User> getAllUsersFromGitLab() {
        return callMicroService.getAllUser();
    }

    public long elapeTimeToCallGitLab() {
        return callMicroService.elapseTime();
    }

    public List<KomeoUser> getAllUsersFromDB() {
        return userDAO.getAllUser();
    }

    public long elapeTimeToCallDB() {
        return userDAO.elapseTime();
    }

    public String tellHits() {
        return "HitCounter : " + hitCounter.tellHits();
    }

    public String echo(String text) {
        return "WildFly Echo : " + text;
    }

    public String merge() {
        return userDAO.updateUsers();
    }
}
