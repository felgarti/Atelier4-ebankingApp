package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dtos.AccountHistoryDTO;
import com.example.ebankingbackend.dtos.AccountOperationDTO;
import com.example.ebankingbackend.dtos.BankAccountDTO;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
public class BankAccountRestAPI {
    private BankAccountService bankAccountService ;


    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @GetMapping("/accounts/{id}")

public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {

        return  bankAccountService.getBankAccount(id) ;
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> ListAccounts()
    {

        return  bankAccountService.bankAccountList() ;
    }

    @GetMapping("/accounts/{id}/operations")
    public  List<AccountOperationDTO> getHistory(@PathVariable String id)
    {
        return  bankAccountService.accountHistory(id) ;
    }
    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String id , @RequestParam(name = "page" , defaultValue = "0") int page , @RequestParam(name = "size" , defaultValue = "5") int size ) throws BankAccountNotFoundException {
        return  bankAccountService.getAccountHistory(id , page , size ) ;
    }

}
