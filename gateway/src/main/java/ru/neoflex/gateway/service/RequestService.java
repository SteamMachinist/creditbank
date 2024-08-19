package ru.neoflex.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    public <T, R> T forwardRequest(String targetServiceUrl,
                                   String path,
                                   HttpMethod method,
                                   Optional<R> requestBody,
                                   Class<T> responseType) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(targetServiceUrl).path(path);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uriBuilder.toUriString(),
                method,
                createRequestEntity(requestBody),
                String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException();
        }

        return getObjectFromResponse(responseEntity, responseType);
    }

    private <R> HttpEntity<R> createRequestEntity(Optional<R> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return requestBody.map(r -> new HttpEntity<>(r, headers)).orElseGet(() -> new HttpEntity<>(null));
    }

    private <T> T getObjectFromResponse(ResponseEntity<String> responseEntity, Class<T> responseType) {
        if (responseType == Void.class) {
            return null;
        }

        String jsonResponse = responseEntity.getBody();

        try {
            return objectMapper.readValue(jsonResponse, responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize response", e);
        }
    }
}
