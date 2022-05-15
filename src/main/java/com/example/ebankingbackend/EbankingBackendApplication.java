package com.example.ebankingbackend;

import com.example.ebankingbackend.entities.*;
import com.example.ebankingbackend.enums.AccountStatus;
import com.example.ebankingbackend.enums.OperationType;
import com.example.ebankingbackend.repositories.AccountOperationRepository;
import com.example.ebankingbackend.repositories.BankAccountRepository;
import com.example.ebankingbackend.repositories.CustomerRepository;
import com.example.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}

//	@Bean
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
@Bean CommandLineRunner commandLineRunner(BankService bankService)
{
	return  args -> {
	bankService.consulter();
//
	};
}
}

