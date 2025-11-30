package com.skupina1.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("drivePU");

    @Produces
    public EntityManager produceEntityManager() {
        return emf.createEntityManager();
    }

    // Optional: close EMF on shutdown
    public void close() {
        emf.close();
    }
}
