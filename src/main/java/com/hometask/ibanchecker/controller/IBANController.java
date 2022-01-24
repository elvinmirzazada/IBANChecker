package com.hometask.ibanchecker.controller;

import com.hometask.ibanchecker.model.IBAN;
import com.hometask.ibanchecker.service.IBANServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/IBAN")
@RestController
public class IBANController {
    private final IBANServiceImpl ibanService;
    @Autowired
    public IBANController(IBANServiceImpl ibanService) {
        this.ibanService = ibanService;
    }
    private static final Logger logger = LoggerFactory.getLogger(IBANController.class);

    /**
     * validate the given single IBAN
     *
     * @param iban - the IBAN object
     * @return validated IBAN object
     */
    @PostMapping("/validateIban")
    public IBAN validateIban(@RequestBody IBAN iban) {
        logger.info("Call validateIban method: " + iban.toString());
        return ibanService.validateIBAN(iban);
    }

    /**
     * validate the given multiple IBANs
     *
     * @param ibans - list of IBANs
     * @return list of validated IBAN objects
     */
    @PostMapping("/validateMultiIbans")
    public List<IBAN> validateMultiIbans(@RequestBody List<IBAN> ibans) {
        logger.info("Call validateMultiIBAN method: " + ibans.toString());
        return ibanService.validateMultiIBAN(ibans);
    }

    /**
     * List of all validated IBAN objects
     *
     * @param filter - ALL, VALID, INVALID filter for filtering the list of IBAN objects
     * @return list of validated IBAN objects
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @GetMapping("/listOfAllIbans/{filter}")
    public List<IBAN> getAllIBANs(@PathVariable String filter) {
        logger.info("Call getAllIBANs method with filter: " + filter);
        try {
            return ibanService.getAllIBANs(filter);
        } catch (NoSuchFieldException e) {
            logger.error(e.toString());
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
        }
        return null;
    }



}
