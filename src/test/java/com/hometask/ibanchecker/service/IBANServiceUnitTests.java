package com.hometask.ibanchecker.service;

import com.hometask.ibanchecker.model.IBAN;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IBANServiceUnitTests {

    @Autowired
    IBANService ibanService;
    String[] ibans = {"BG18RZBB91550123456789", "HR172360w001101234565", "CY21002001950000357001234567", "AZ75WQSA84558455845556527586", "CZ5508000000001234567899"};
    IBAN correctIban;
    IBAN wrongIban;
    List<IBAN> listOfIbans;

    @BeforeAll
    void setup() {
        /*
        Creating single IBAN object for testing the checkIBAN function

        - 1. Correct IBAN
        - 2. Wrong IBAN
         */
        correctIban = new IBAN(ibans[0]);
        wrongIban = new IBAN(ibans[1]);

        /*
        Creating List of IBAN objects for testing the checkMultiIBAN function
         */
        listOfIbans = new ArrayList<>();
        for(var item: ibans) {
            listOfIbans.add(new IBAN(item));
        }
    }

    @Test
    void testCheckCorrectIBAN() {
        /*
        * Assert When the result is wrong.
        * */
        Assertions.assertTrue(ibanService.validateIBAN(correctIban).getIsValid() == 1);
    }

    @Test
    void testCheckWrongIBAN() {
        /*
         * Assert When the result is correct.
         * */
        Assertions.assertFalse(ibanService.validateIBAN(wrongIban).getIsValid() == 1);
    }

    @Test
    void testCheckMultiIBAN() {
        /*
         * Assert When the result is null and has different size.
         * */
        Assertions.assertNotNull(ibanService.validateMultiIBAN(listOfIbans));
        Assertions.assertTrue(ibanService.validateMultiIBAN(listOfIbans).size() == listOfIbans.size());
    }

}
