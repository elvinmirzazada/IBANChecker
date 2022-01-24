package com.hometask.ibanchecker.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.ibanchecker.model.IBAN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class IBANServiceDocsTests {

    private MockMvc mockMvc;
    String[] ibans = null;
    IBAN ibanNumber = null;
    List<IBAN> listOfIbans = null;
    ObjectMapper objectMapper = null;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)).build();
        this.ibans = new String[]{"BG18RZBB91550123456789", "HR172360w001101234565"};
        this.ibanNumber = new IBAN(ibans[0]);
        this.objectMapper = new ObjectMapper();
        listOfIbans = new ArrayList<>();
        for(var item: ibans) {
            listOfIbans.add(new IBAN(item));
        }
    }

    @Test
    public void testValidateIBAN() throws Exception {
        String body = objectMapper.writeValueAsString(ibanNumber);

        mockMvc.perform(post("/api/v1/IBAN/validateIban")
                        .content(body)
                        .contentType("application/json")).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testValidateMultiIBANs() throws Exception {
        String body = objectMapper.writeValueAsString(listOfIbans);

        mockMvc.perform(post("/api/v1/IBAN/validateMultiIbans")
                        .content(body)
                        .contentType("application/json")).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void testListOfAllIbans() throws Exception {
        this.testValidateIBAN();
        mockMvc.perform(get("/api/v1/IBAN/listOfAllIbans/ALL")
                        .contentType("application/json")).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

}
