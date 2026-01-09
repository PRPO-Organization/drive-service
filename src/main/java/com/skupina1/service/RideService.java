package com.skupina1.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skupina1.entity.RideEntity;
import com.skupina1.model.Notification;
import com.skupina1.model.TransactionRequest;
import com.skupina1.persistence.RideRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class RideService {

    RideRepository repo = new RideRepository();

    // update all of these
    private static final String ACCOUNT_URL = "http://9.235.137.130/users/";
    //private static final String ACCOUNT_URL = "http://localhost:8080/users/";
    private static final String NOTIFICATIONS_URL = "http://9.235.137.130/notifications/send";
    private static final String TRANSACTIONS_URL = "http://9.235.137.130/api/transactions/fare";
    //private static final String SORT_DRIVERS_URL = "http://localhost:8084/";

    private static final String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkB0ZXN0LmNvbSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc2Nzk3MDk0OSwiZXhwIjoxNzY4MDU3MzQ5fQ.bjbob2S4ssXYF7RsdisSJZwqaE2Tqaj_0p2yOTG9HeU-fkziP2LIjw5DVSKy8IbR";

    private void getNewToken(){

    }
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

    public List<RideEntity> findByDriverId(long driverId) { return repo.findByDriverId(driverId); }

    public List<RideEntity> findByPassengerId(long passId) { return repo.findByPassenger(passId); }

    private String getEmail(long passengerId){
        String endpoint = ACCOUNT_URL + passengerId;
        System.out.println(endpoint);

        Response response = ClientBuilder.newClient()
                .target(endpoint)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get();

        //System.out.println("STATUS: " + response.getStatus());
        String body = response.readEntity(String.class);
        System.out.println(body);
        //response.close();

        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode node = mapper.readTree(body);
            String email = node.get("email").asText();
            System.out.println(email);
            return email;
        }catch (Exception e){
            System.err.println(e);
            return "";
        }

        //return response.readEntity(String.class);
    }

    private void sendNotification(RideEntity r) {

        //System.out.println("LINK NOTIFICATIONS");

        // fetch email from account-management
        System.out.println("FETCHING EMAIL...");
        String email = getEmail(r.getPassengerId());
        System.out.println("EMAIL: " + email);
        Notification n = new Notification(email, "Driver en route.",
                "Driver en route with ID="+r.getDriverId());

        String recipient = "{\"recipient\": \""+email+"\",";
        String subject = "\"subject\": \"Driver en route.\",";
        String body = "\"body\": \"Driver en route with ID="+r.getDriverId()+".\"}";

        String json = recipient+subject+body;

        Client client = ClientBuilder.newClient();

        System.out.println("SENDING NOTIFICATION TO " + n.getRecipient());
        Response response = client.
                target(NOTIFICATIONS_URL)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.readEntity(String.class));

        // Close client
        client.close();

        /*
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
        LocalDateTime createdAt = r.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        long minutesElapsed = Duration.between(createdAt, now).toMinutes();
        double distanceKm = distanceInKm(r.getPickupLat(), r.getPickupLng(), r.getDropoffLat(), r.getDropoffLng());

        TransactionRequest t = new TransactionRequest(distanceKm, minutesElapsed, 1.0);

        Response response = ClientBuilder.newClient()
                .target(TRANSACTIONS_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(t, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
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

    public static double distanceInKm(
            double lat1, double lon1,
            double lat2, double lon2
    ) {
        final double EARTH_RADIUS_KM = 6371.0088; // Mean Earth radius

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

}
