package com.service.service.service;

import com.service.service.model.MessageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public void sendMessageToPatient(String patientId, String message) {
        String url = "http://comm-service/api/messages/send";
        MessageRequestDTO request = new MessageRequestDTO(patientId, message);
        restTemplate.postForEntity(url, request, Void.class);
    }

    public void subscribeToNotifications() {
        // Souscription à Kafka ou tout autre service de notification
        // Implémentez ici la logique pour recevoir les notifications
    }
}
