package com.example.ebankingbackend.services;


import com.example.ebankingbackend.dtos.*;
import com.example.ebankingbackend.entities.*;
import com.example.ebankingbackend.enums.OperationType;
import com.example.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.mappers.BankAccountMapperImpl;
import com.example.ebankingbackend.repositories.AccountOperationRepository;
import com.example.ebankingbackend.repositories.BankAccountRepository;
import com.example.ebankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

   private CustomerRepository customerRepository ;
    private BankAccountRepository bankAccountRepository ;
    private AccountOperationRepository accountOperationRepository ;
private BankAccountMapperImpl bankAccountMapper ;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO) ;
        Customer savedCustomer = customerRepository.save(customer) ;

        return bankAccountMapper.fromCustomer(savedCustomer) ;

    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft , Long customerId) throws CustomerNotFoundException {
       Customer customer= customerRepository.findById(customerId).orElse(null) ;
       if(customer==null)
       {
           throw new CustomerNotFoundException("Customer not found ! ") ;
       }
       CurrentAccount currentAccount= new CurrentAccount() ;

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date()) ;
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
       CurrentAccount saved= bankAccountRepository.save(currentAccount) ;
        return bankAccountMapper.fromCurrentBankAccount(saved) ;



    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null) ;
        if(customer==null)
        {
            throw new CustomerNotFoundException("Customer not found ! ") ;
        }
        SavingAccount savingAccount= new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date()) ;
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount saved= bankAccountRepository.save(savingAccount) ;
        return bankAccountMapper.fromSavingBankAccount(saved) ;

    }

    @Override
    public List<CustomerDTO> listCustomers() {
      List<Customer> customers=customerRepository.findAll() ;
  List<CustomerDTO> customerDTOS=    customers
          .stream()
          .map(customer -> bankAccountMapper.fromCustomer(customer) )
              .collect(Collectors.toList()) ;
//    List<CustomerDTO> customerDTOS= new ArrayList<>() ;
//    for (Customer customer : customers)
//    {
//        CustomerDTO customerDTO = bankAccountMapper.fromCustomer(customer) ;
//        customerDTOS.add(customerDTO) ;
//    }
    return  customerDTOS ;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository
                .findById(accountId).orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found ! ")) ;

        if(bankAccount instanceof  SavingAccount)
        {
            SavingAccount savingAccount=(SavingAccount) bankAccount ;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount =(CurrentAccount) bankAccount ;
            return  bankAccountMapper.fromCurrentBankAccount(currentAccount) ;
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found ! ")) ;
     if(bankAccount.getBalance()<amount)
     {
         throw new BalanceNotSufficientException("Banlance not sufficient !") ;
     }

        AccountOperation accountOperation = new AccountOperation() ;
     accountOperation.setType(OperationType.DEBIT);
     accountOperation.setAmount(amount);
     accountOperation.setOperationDate(new Date());
     accountOperation.setDescription(description);
     accountOperationRepository.save(accountOperation);


     accountOperation.setBankAccount(bankAccount);
     bankAccount.setBalance(bankAccount.getBalance()-amount);
     bankAccountRepository.save(bankAccount) ;


    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found ! ")) ;


        AccountOperation accountOperation = new AccountOperation() ;
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperationRepository.save(accountOperation);


        accountOperation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount) ;

    }


    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {


        debit(accountIdSource,amount,"transfer to "+ accountIdDestination );
        credit(accountIdDestination,amount,"transfer from "+ accountIdSource);


    }

    @Override
    public  List<BankAccountDTO> bankAccountList()
    {

        List<BankAccount> bankAccounts=bankAccountRepository.findAll() ;
        List<BankAccountDTO> bankAccountDTOList=  bankAccounts
                .stream()
                .map(bankAccount -> {
                    if(bankAccount instanceof  SavingAccount ) {
                        SavingAccount savingAccount = (SavingAccount) bankAccount;
                        return bankAccountMapper.fromSavingBankAccount(savingAccount) ;
                    }else{
                       CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return bankAccountMapper.fromCurrentBankAccount(currentAccount) ;
                    }

                }).collect(Collectors.toList());
        return  bankAccountDTOList ;
    }

@Override
    public  CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
       Customer customer= customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("Customer Not Found !") ) ;
    return  bankAccountMapper.fromCustomer(customer) ;
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("updating customer");
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO) ;
        Customer savedCustomer = customerRepository.save(customer) ;

        return bankAccountMapper.fromCustomer(savedCustomer) ;

    }
    @Override
    public void  deleteCustomer(Long customerId)
    {
        customerRepository.deleteById(customerId);
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId)
    {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId) ;
      List<AccountOperationDTO> accountOperationDTOS=  accountOperations.stream().map(accountOperation ->
            bankAccountMapper.fromAccountOperation(accountOperation) ).collect(Collectors.toList()) ;
      return  accountOperationDTOS ;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(id).orElse(null) ;
        if(bankAccount==null)
        {
            throw new BankAccountNotFoundException("Bank account not found !") ;

        }
        Page<AccountOperation>  accountOperations  = accountOperationRepository.findByBankAccountId(id , PageRequest.of(page , size)) ;
AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO() ;
       List<AccountOperationDTO> accountOperationDTOS= accountOperations.getContent().stream().map(accountOperation ->
        bankAccountMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
             accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
                accountHistoryDTO.setAccountId(bankAccount.getId());
                accountHistoryDTO.setBalance(bankAccount.getBalance());
                accountHistoryDTO.setPageSize(size);
                accountHistoryDTO.setCurrentPage(page);
                accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
                return  accountHistoryDTO ;

    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=  customerRepository.searchCustomer(keyword) ;
        List<CustomerDTO> customerDTOS=    customers
                .stream()
                .map(customer -> bankAccountMapper.fromCustomer(customer) )
                .collect(Collectors.toList()) ;
        return customerDTOS;
    }

}
