package com.hometask.ibanchecker.dao;

import com.hometask.ibanchecker.model.IBAN;
import java.util.List;

public interface IBANDao {

    IBAN checkIBANIsValid(IBAN account);
    List<IBAN> checkMultiIBANsIsValid(List<IBAN> ibans);
    List<IBAN> getAllIBANs(int filter);

}
