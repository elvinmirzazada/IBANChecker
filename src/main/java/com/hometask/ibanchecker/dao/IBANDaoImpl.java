package com.hometask.ibanchecker.dao;

import com.hometask.ibanchecker.constants.IBANConstants;
import com.hometask.ibanchecker.controller.IBANController;
import com.hometask.ibanchecker.model.IBAN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository("ibanDao")
public class IBANDaoImpl implements IBANDao{

    private final static HashMap<IBAN, String> DB = new HashMap<>();
    private final IBANRepo ibanRepo;

    public IBANDaoImpl(IBANRepo ibanRepo) {
        this.ibanRepo = ibanRepo;
    }
    private static final Logger logger = LoggerFactory.getLogger(IBANDaoImpl.class);


    @Override
    public IBAN checkIBANIsValid(IBAN account) {
        /**
         * First filter the IBAN's length and correct pattern.
         * then applying the algorithm to calculate the IBAN validation
         */
        var isValid = this.filterIban(account.getIbanNumber());
        if (isValid) {
            isValid =  this.validateIban(account.getIbanNumber());
        }
        account.setIsValid(isValid ? 1 : 0);

        ibanRepo.save(account);
        logger.info("The response of validateIban: " + account.toString());
        return account;
    }

    @Override
    public List<IBAN> checkMultiIBANsIsValid(List<IBAN> ibans) {
        /**
         * Check the validation of each item in the list
         */
        List<IBAN> result = new ArrayList<>();
        ibans.forEach((item) -> {
            result.add(this.checkIBANIsValid(item));
        });
        return result;
    }

    @Override
    public List<IBAN> getAllIBANs(int filter) {
        List<IBAN> result = new ArrayList<>();
        ibanRepo.findAll().forEach(result::add);
        if (filter == -1) {
            return result;
        }
        result = result.stream().filter(item -> item.getIsValid() == filter).collect(Collectors.toList());
        logger.info("The response of getAllIBANs with filter " + filter + ": " + result);
        return result;
    }

    private boolean filterIban(String ibanNumber){
        /**
         * The IBAN consists of up to 34 alphanumeric characters, as follows:
         *
         * country code using ISO 3166-1 alpha-2 – two letters,
         * check digits – two digits, and
         * Basic Bank Account Number (BBAN) – up to 30 alphanumeric characters that are country-specific.[1]
         *
         * First check the IBAN number length. The minimum length of IBAN is 15, the maximum length of IBAN is 34.
         * Then apply the filter which is using for check the Country codes and length of IBAN for each country.
         */
        if((ibanNumber.length() > 34) || (ibanNumber.length() < 15)){
            return false;
        }
        return ibanNumber.matches(IBANConstants.IBAN_FILTER_PATTERN);
    }

    private boolean validateIban(String ibanNumber) {
        /**
         * An IBAN is validated by converting it into an integer and performing a basic mod-97 operation (as described in ISO 7064) on it. If the IBAN is valid, the remainder equals 1.[Note 1] The algorithm of IBAN validation is as follows:[8]
         *
         * Check that the total IBAN length is correct as per the country. If not, the IBAN is invalid
         * Move the four initial characters to the end of the string
         * Replace each letter in the string with two digits, thereby expanding the string, where A = 10, B = 11, ..., Z = 35
         * Interpret the string as a decimal integer and compute the remainder of that number on division by 97
         * If the remainder is 1, the check digit test is passed and the IBAN might be valid.
         */
        ibanNumber = ibanNumber.toLowerCase(Locale.ROOT);

        var front = ibanNumber.substring(0, 4);
        var account = ibanNumber.substring(4);
        var newIbanStr = account + front;
        String finalStr = "";
        for(int i = 0; i < newIbanStr.length(); i++) {
            if(Character.isDigit(newIbanStr.charAt(i))) {
                finalStr += newIbanStr.charAt(i);
            } else {
                finalStr += this.indexOf(IBANConstants.ALPHABET, newIbanStr.charAt(i)) + 10;
            }
        }
        BigInteger finalInteger = new BigInteger(finalStr);
        if (finalInteger.mod(new BigInteger("97")).intValue() == 1) {
            return true;
        }
        return false;
    }

    private int indexOf(char[] arr, char val) {
        return IntStream.range(0, arr.length).filter(i -> arr[i] == val).findFirst().orElse(-1);
    }
}
