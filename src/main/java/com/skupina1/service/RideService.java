package com.skupina1.service;

import com.skupina1.entity.RideEntity;
import com.skupina1.model.Notification;
import com.skupina1.persistence.RideRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;

public class RideService {

    RideRepository repo = new RideRepository();

    // update all of these
    private static final String ACCOUNT_URL = "http://localhost:8081/";
    private static final String NOTIFICATIONS_URL = "http://localhost:8082/";
    private static final String TRANSACTIONS_URL = "http://localhost:8083/";
    private static final String SORT_DRIVERS_URL = "http://localhost:8084/";

    public boolean createRide(RideEntity r) {
        r.setStatus("NEW");
        r.setCreatedAt(LocalDateTime.now());

        boolean saved = repo.save(r);
        try {
            sendNotification(r);
        }catch (Exception e){
            System.out.println("Notifications error: " + e);
        }

        return true;
    }

    public RideEntity findById(long id){
        return repo.find(id);
    }

    private String getEmail(long passengerId){

        return ClientBuilder.newClient()
                .target(ACCOUNT_URL + "/email/" + passengerId)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
    }

    private void sendNotification(RideEntity r) {

        System.out.println("LINK NOTIFICATIONS");
        /*
        // fetch email from account-management
        String email = getEmail(r.getPassengerId());

        Notification n = new Notification(
                email,
                "Ride Requested",
                "Your ride has been created. Ride ID = " + r.getId()
        );

        ClientBuilder.newClient()
                .target(NOTIFICATIONS_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(n));

         */
    }

    public RideEntity completeRide(long id) {
        RideEntity r = repo.find(id);
        r.setStatus("COMPLETED");
        repo.update(r);

        triggerTransaction(r);
        updateDriverSorting(r.getDriverId());
        return r;
    }

    private void triggerTransaction(RideEntity r) {
        System.out.println("LINK TRANSACTIONS");
        // implement transactions
        /*
        ClientBuilder.newClient()
                .target(TRANSACTIONS_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(r));

         */
    }

    private void updateDriverSorting(long driverId) {
        System.out.println("LINK SORT DRIVERS");
        // implement sort drivers
        /*
        ClientBuilder.newClient()
                .target(SORT_DRIVERS_URL + "/" + driverId)
                .request()
                .put(Entity.text(""));

         */
    }
}
