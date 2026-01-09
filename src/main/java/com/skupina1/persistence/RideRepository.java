package com.skupina1.persistence;

import com.skupina1.entity.RideEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;

public class RideRepository {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("drivePU");


    @Transactional
    public boolean save(RideEntity r) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            return true;
        }catch (Exception e){
            System.out.println("SQL exception: " + e);
            return false;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public RideEntity find(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(RideEntity.class, id);
        }catch (Exception e){
            System.out.println("SQL exception: " + e);
            return null;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<RideEntity> findByDriverId(long driverId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM RideEntity r WHERE r.driverId = :driverId", RideEntity.class)
                    .setParameter("driverId", driverId)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("SQL exception: " + e);
            return Collections.emptyList(); // return empty list on error
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<RideEntity> findByPassenger(long passengerId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM RideEntity r WHERE r.passengerId = :passengerId", RideEntity.class)
                    .setParameter("passengerId", passengerId)
                    .getResultList();
        } catch (Exception e) {
            System.out.println("SQL exception: " + e);
            return Collections.emptyList(); // return empty list on error
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Transactional
    public RideEntity update(RideEntity r) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            RideEntity merged = em.merge(r);
            tx.commit();
            return merged;
        }catch (Exception e){
            System.out.println("SQL exception: " + e);
            if(tx.isActive())
                tx.rollback();
            return null;
        }finally {
            if(em.isOpen()) em.close();
        }
    }
}
