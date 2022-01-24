package com.hometask.ibanchecker.dao;

import com.hometask.ibanchecker.model.IBAN;
import org.springframework.data.repository.CrudRepository;

public interface IBANRepo extends CrudRepository<IBAN, Long> {
}
