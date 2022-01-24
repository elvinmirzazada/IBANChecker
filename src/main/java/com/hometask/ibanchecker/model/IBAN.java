package com.hometask.ibanchecker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Entity
public class IBAN {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ibanNumber;
    private int isValid;
    @CreationTimestamp
    private Timestamp createdTime;

    public IBAN(@JsonProperty("ibanNumber") String ibanNumber) {
        this.ibanNumber = ibanNumber;
        this.isValid = 0;
    }

    public IBAN() {}
}
