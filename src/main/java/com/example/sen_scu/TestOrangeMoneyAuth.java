//package com.example.sen_scu;
//
//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//public class TestOrangeMoneyAuth {
//
//    public static void main(String[] args) {
//        String clientId = "2gaX0MZWwfi1wbn4mEp5hsr6p3FmtRV9";
//        String clientSecret = "axz7RfeHPmcjgLAV1l6X0yRXboMInH7M227lio5YY8E6";
//        String tokenUrl = "https://api.orange.com/openidconnect/playground/v1.0/token";
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBasicAuth(clientId, clientSecret); // Basic Auth
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("Accept", "application/json");
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "client_credentials");
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                System.out.println("✅ Authentification réussie !");
//                System.out.println("Access Token: " + response.getBody().get("access_token"));
//            } else {
//                System.out.println("❌ Erreur d'authentification : " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            System.out.println("❌ Exception : " + e.getMessage());
//        }
//    }
//}
