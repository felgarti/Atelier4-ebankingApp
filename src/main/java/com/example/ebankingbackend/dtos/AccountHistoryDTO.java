package com.example.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDTO {
    private  String accountId ;
    private  double Balance ;
    private String accountType  ;
    private int currentPage ;
    private int totalPages ;
    private  int pageSize ;
    private List<AccountOperationDTO>     accountOperationDTOS ;
}
