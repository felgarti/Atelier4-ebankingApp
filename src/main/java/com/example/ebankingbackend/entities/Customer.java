package com.example.ebankingbackend.entities;

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
    private List<BankAccount> bankAccounts ;
}
