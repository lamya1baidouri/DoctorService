package com.service.service.service;

import com.service.service.model.NurseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NurseServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public List<NurseDTO> getNursesByIds(List<String> nurseIds) {
        String url = "http://nurse-service/api/nurses";

        // Use exchange instead of postForEntity to handle ParameterizedTypeReference
        ResponseEntity<List<NurseDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(nurseIds),
                new ParameterizedTypeReference<List<NurseDTO>>() {}
        );

        return response.getBody();
    }
}
