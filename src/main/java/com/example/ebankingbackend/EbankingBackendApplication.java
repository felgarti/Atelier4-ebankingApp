package com.example.ebankingbackend;

import com.example.ebankingbackend.dtos.BankAccountDTO;
import com.example.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.example.ebankingbackend.dtos.CustomerDTO;
import com.example.ebankingbackend.dtos.SavingBankAccountDTO;
import com.example.ebankingbackend.entities.*;
import com.example.ebankingbackend.enums.AccountStatus;
import com.example.ebankingbackend.enums.OperationType;
import com.example.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.repositories.AccountOperationRepository;
import com.example.ebankingbackend.repositories.BankAccountRepository;
import com.example.ebankingbackend.repositories.CustomerRepository;
//import com.example.ebankingbackend.services.BankService;
import com.example.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
//2h58
@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}

	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository , BankAccountRepository bankAccountRepository , AccountOperationRepository accountOperationRepository)
{
	return args ->
	{
		Stream.of("fatima" , "yousra " , "hamza" ).forEach(
				name->
				{
					Customer customer=new Customer() ;
					customer.setName(name) ;
					customer.setEmail(name+"@gmail.com");
					customerRepository.save(customer) ;
				}


		);
	customerRepository.findAll().forEach(cust->{
		CurrentAccount currentAccount=new CurrentAccount() ;
		currentAccount.setId(UUID.randomUUID().toString());
		currentAccount.setBalance(Math.random()*9000);
		currentAccount.setCreatedAt(new Date());
		currentAccount.setStatus(AccountStatus.CREATED);
		currentAccount.setCustomer(cust);
		currentAccount.setOverDraft(9000);
		bankAccountRepository.save(currentAccount) ;
	});
		customerRepository.findAll().forEach(cust->{
			SavingAccount SavingAccount=new SavingAccount() ;
			SavingAccount.setId(UUID.randomUUID().toString());
			SavingAccount.setBalance(Math.random()*9000);
			SavingAccount.setCreatedAt(new Date());
			SavingAccount.setStatus(AccountStatus.CREATED);
			SavingAccount.setCustomer(cust);
			SavingAccount.setInterestRate(5.5);
			bankAccountRepository.save(SavingAccount) ;
		});
		bankAccountRepository.findAll().forEach(acc->
		{
			for (int i =0 ; i<5 ; i++)
			{
				AccountOperation accountOperation= new AccountOperation() ;
				accountOperation.setOperationDate(new Date());
				accountOperation.setBankAccount(acc);
				accountOperation.setAmount(Math.random()*12000);
				accountOperation.setType(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
				accountOperationRepository.save(accountOperation) ;
			}
		});





	} ;


}
@Bean
 CommandLineRunner commandLineRunner(BankAccountService bankAccountService)
{
	return  args -> {
		Stream.of("Hassan" , "Fatima" , "Yousra").forEach(name->
		{
			CustomerDTO customer = new CustomerDTO() ;
			customer.setName(name);
			customer.setEmail(name+"@gmail.com");
			bankAccountService.saveCustomer(customer) ;
		});
		bankAccountService.listCustomers().forEach(customer -> {
			try {
				bankAccountService.saveCurrentBankAccount(Math.random()*9000 , 9000 , customer.getId()) ;
				bankAccountService.saveSavingBankAccount(Math.random()*120000 , 5.5 , customer.getId()) ;

		 List<BankAccountDTO> bankAccounts= bankAccountService.bankAccountList();
			for(BankAccountDTO bankAccount : bankAccounts)
			{
				for(int i = 0 ; i < 10 ; i++)
				{
					String accountId ;
					if(bankAccount instanceof SavingBankAccountDTO)
					{
						accountId= ((SavingBankAccountDTO)bankAccount).getId() ;
					}else
					{
						accountId= ((CurrentBankAccountDTO)bankAccount).getId() ;
					}
					bankAccountService.credit(accountId,10000+Math.random()*120000, "credit");
					bankAccountService.debit(accountId,1000+Math.random()*9000, "debit");
				}
			}



			} catch (CustomerNotFoundException e) {
		e.printStackTrace();
			} catch (BankAccountNotFoundException e) {
				throw new RuntimeException(e);
			} catch (BalanceNotSufficientException e) {
				throw new RuntimeException(e);
			}


		});
//
	};
}
}

