package com.hometask.ibanchecker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.ibanchecker.model.IBAN;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IBANControllerIntegrationTests {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    String[] ibans = {"BG18RZBB91550123456789", "HR172360w001101234565", "CY21002001950000357001234567", "AZ75WQSA84558455845556527586", "CZ5508000000001234567899"};
    IBAN correctIban = new IBAN(ibans[0]);
    IBAN wrongIban = new IBAN(ibans[1]);

    ObjectMapper objectMapper = new ObjectMapper();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/api/v1/IBAN" + uri;
    }

    @Test
    public void testCheckCorrectIBAN() throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(correctIban);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = this.restTemplate
                .exchange(createURLWithPort("/validateIban"), HttpMethod.POST, entity, String.class);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());

        IBAN iban = objectMapper.readValue(responseEntity.getBody(), IBAN.class);
        Assertions.assertTrue(iban.getIsValid() == 1);
    }

    @Test
    public void testCheckWrongIBAN() throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(wrongIban);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = this.restTemplate
                .exchange(createURLWithPort("/validateIban"), HttpMethod.POST, entity, String.class);

        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());

        IBAN iban = objectMapper.readValue(responseEntity.getBody(), IBAN.class);
        Assertions.assertFalse(iban.getIsValid() == 1);
    }

    @Test
    public void testListOfAllIbans(){
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithPort("/listOfAllIbans/ALL"), HttpMethod.GET, entity, String.class);

        Assertions.assertTrue(responseEntity.getStatusCode().value() == 200);
        Assertions.assertTrue(responseEntity.getBody().length() > 0);
    }

}