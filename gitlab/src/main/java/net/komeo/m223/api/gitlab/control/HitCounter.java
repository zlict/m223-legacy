package net.komeo.m223.api.gitlab.control;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Singleton
public class HitCounter {

    private long hits;

    @PostConstruct
    public void resetCounts() {
        hits = 0;
    }

    public void incCount() {
        hits++;
    }

    @Lock(LockType.READ)
    public String tellHits() {
        return this.getClass().getName() + " registered hits : " + this.hits;
    }
}
