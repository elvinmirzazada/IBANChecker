package com.hometask.ibanchecker.service;

import com.hometask.ibanchecker.model.IBAN;
import java.util.List;

public interface IBANService {
    IBAN validateIBAN(IBAN iban_account);
    List<IBAN> validateMultiIBAN(List<IBAN> ibans);
    List<IBAN> getAllIBANs(String filter) throws NoSuchFieldException, IllegalAccessException;
}
