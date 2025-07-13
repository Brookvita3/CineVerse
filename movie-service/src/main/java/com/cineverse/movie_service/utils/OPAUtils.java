package com.cineverse.movie_service.utils;

import com.cineverse.movie_service.dto.OPAInput;
import com.cineverse.movie_service.dto.OPAResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OPAUtils {

    @Value("${opa.url}")
    private String opaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean checkAuthorization(String method, String[] path, String role) {
        Map<String, Object> input = new HashMap<>();
        input.put("method", method);
        input.put("path", path);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Role", role);
        input.put("headers", headers);

        OPAInput opaInput = new OPAInput(input);

        try {
            ResponseEntity<OPAResponse> response = restTemplate.postForEntity(
                    opaUrl,
                    opaInput,
                    OPAResponse.class
            );

            return response.getBody() != null && response.getBody().isResult();

        } catch (RestClientException e) {
            return false;
        }
    }

}
