package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dtos.CustomerDTO;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.repositories.BankAccountRepository;
import com.example.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService ;
    @GetMapping("/customers")
    public List<CustomerDTO> customers()
    {
        return  bankAccountService.listCustomers();
    }
    @GetMapping("/customers/search")
    public List<CustomerDTO> Searchcustomers(@RequestParam(name = "keyword" , defaultValue = "") String keyword)
    {

        return  bankAccountService.searchCustomers("%"+keyword+"%") ;
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id")Long customerId) throws CustomerNotFoundException {
         CustomerDTO customerDTO= bankAccountService.getCustomer(customerId) ;
         return  customerDTO ;
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO)
    {
      return   bankAccountService.saveCustomer(customerDTO) ;

    }
    @PutMapping("/customers/{id}")
    public  CustomerDTO updateCustomer(@PathVariable(name = "id")Long customerId , @RequestBody CustomerDTO customerDTO)
    {
        customerDTO.setId(customerId);
      return   bankAccountService.updateCustomer(customerDTO) ;
    }
    @DeleteMapping("/customers/{id}")
    public  void deleteCustomer(@PathVariable(name = "id")Long customerId )
    {

       bankAccountService.deleteCustomer(customerId);
    }
}


//v3/api-doc
//swagger-ui/index.html