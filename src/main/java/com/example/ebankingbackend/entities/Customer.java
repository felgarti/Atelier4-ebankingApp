package com.example.ebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.SimpleTriggerContext;

import javax.persistence.*;
import java.util.List;
 @NoArgsConstructor @AllArgsConstructor
@Entity
@Data

public class Customer {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name ;
    private  String email ;
    @OneToMany(mappedBy = "customer")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BankAccount> bankAccounts ;
}
