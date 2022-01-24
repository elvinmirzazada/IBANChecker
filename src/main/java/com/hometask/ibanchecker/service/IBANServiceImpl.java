package com.hometask.ibanchecker.service;

import com.hometask.ibanchecker.constants.IBANConstants;
import com.hometask.ibanchecker.dao.IBANDao;
import com.hometask.ibanchecker.model.IBAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IBANServiceImpl implements IBANService{

    private final IBANDao ibanDao;

    @Autowired
    public IBANServiceImpl(@Qualifier("ibanDao") IBANDao ibanDao) {
        this.ibanDao = ibanDao;
    }

    public IBAN validateIBAN(IBAN iban_account) {
        return ibanDao.checkIBANIsValid(iban_account);
    }

    @Override
    public List<IBAN> validateMultiIBAN(List<IBAN> ibans) {
        return ibanDao.checkMultiIBANsIsValid(ibans);
    }

    @Override
    public List<IBAN> getAllIBANs(String filter) throws NoSuchFieldException, IllegalAccessException {
        int filter_ind = (int) IBANConstants.class.getDeclaredField(filter).get(null);
        return ibanDao.getAllIBANs(filter_ind);
    }

}
