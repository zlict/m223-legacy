package net.komeo.m223.api.gitlab.control;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.komeo.m223.api.gitlab.aop.Mailer;
import net.komeo.m223.api.gitlab.aop.ElapseTimeLogger;
import net.komeo.m223.api.gitlab.entity.KomeoUser;
import org.gitlab4j.api.models.User;

@Interceptors({Mailer.class, ElapseTimeLogger.class})
@Stateless
public class UserDAO {

    @Inject
    CallMicroService callMicroService;

    @PersistenceContext(name = "pu_api")
    EntityManager em;

    @PostConstruct
    void testDependencyInjection() {
        if (em == null) {
            System.out.println(this.getClass().getName() + " failed on Dependency Injection");
        }
    }

    public String updateUsers() {
        List<User> users = callMicroService.getAllUser();
        users.forEach((u) -> {
            KomeoUser komeoUser = new KomeoUser();
            komeoUser.setId(u.getId());
            komeoUser.setName(u.getName());
            komeoUser.setUserName(u.getUsername());
            komeoUser.setWebUrl(u.getWebUrl());
            komeoUser.setAvatarUrl(u.getAvatarUrl());
            em.merge(komeoUser);
        });
        return this.getClass().getName() + " merged " + users.size() + " user to DB";
    }

    public List<KomeoUser> getAllUser() {
        return em.createNamedQuery("allUser", KomeoUser.class).getResultList();
    }

    public long elapseTime() {
        long start = System.nanoTime();
        List<KomeoUser> users = this.getAllUser();
        long stop = System.nanoTime();
        return stop - start;
    }
}
