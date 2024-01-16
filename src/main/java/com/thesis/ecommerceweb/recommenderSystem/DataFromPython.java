package com.thesis.ecommerceweb.recommenderSystem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataFromPython {
    List<Integer> pidList = new ArrayList<>();

    public List<Integer> getDataFromPython(String username) {
        RestTemplate restTemplate = new RestTemplate();
        String pythonAPIUrl = "http://127.0.0.1:5000/recommendation/" + username;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(pythonAPIUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                responseBody = responseBody.replaceAll("\\[|\\]", "");
                String[] newResponse = responseBody.split(",");
                pidList = Arrays.stream(newResponse)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

            }
        } catch (Exception e) {
        }
        return pidList;
    }
}
