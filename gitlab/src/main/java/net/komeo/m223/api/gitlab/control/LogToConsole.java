package net.komeo.m223.api.gitlab.control;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.Interceptors;
import net.komeo.m223.api.gitlab.aop.ElapseTimeLogger;

@Startup
@Singleton
@Interceptors(ElapseTimeLogger.class)
public class LogToConsole {

    private final String HOST = System.getenv("DB_HOST");
    private final String USER = System.getenv("DB_USER");
    private final String PASS = System.getenv("DB_PASSWORD");
    private final String NAME = System.getenv("DB_NAME");

    @PostConstruct
    public void logOnStartUP() {
        System.out.println(this.getClass().getName()
                + "\n"
                + "\n\t adminer URL : https://adminer.komeo.net/"
                + "\n\t Server      : k289mysql1.vlan50.citrin.ch "
                + "\n\t Benutzer    : " + USER
                + "\n\t Passeword   : " + PASS
                + "\n\t Datenbank   : " + NAME
        );
    }

    @Schedule(hour = "*/1", persistent = false)
    public void logBySchedule() {
        System.out.println(this.getClass().getName() + ".logBySchedule() called by scheduler!");
    }
}
