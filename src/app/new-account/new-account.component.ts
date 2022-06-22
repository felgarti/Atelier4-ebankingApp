import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AccountsService} from "../services/accounts.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-account',
  templateUrl: './new-account.component.html',
  styleUrls: ['./new-account.component.css']
})
export class NewAccountComponent implements OnInit {

  newSavingFormGroup! :FormGroup
  newCurrentFormGroup! :FormGroup
  constructor(private fb :FormBuilder,private accountService:AccountsService,private router: Router) { }

  ngOnInit(): void {
    this.newCurrentFormGroup = this.fb.group({
      balance : this.fb.control(null,),
      overDraft : this.fb.control(null,),
      customerId : this.fb.control(null,),
    })
    this.newSavingFormGroup = this.fb.group({
      balance : this.fb.control(null,),
      interestRate : this.fb.control(null,),
      customerId : this.fb.control(null,),
    })
  }

  handleSaveCurrentAccount() {
    let balance : number =this.newCurrentFormGroup.value.balance
    let overdraft : number=this.newCurrentFormGroup.value.overDraft
    let customerId : number=this.newCurrentFormGroup.value.customerId
    this.accountService.saveCurrentAccount(balance,overdraft,customerId).subscribe({
      next : data=>{
        alert("Customer has been saved successfully")
        //this.newCustomerFormGroup.reset();
        this.router.navigateByUrl("/customer-accounts/"+this.newSavingFormGroup.value.customerId)
      },
      error : err =>{
        console.log(err);
      }
    });
  }

  handleSaveSavingAccount() {
    let balance : number =this.newSavingFormGroup.value.balance
    let interest : number=this.newSavingFormGroup.value.interest
    let customerId : number=this.newSavingFormGroup.value.customerId
    this.accountService.saveSavingAccount(balance,interest,customerId).subscribe({
      next : data=>{
        alert("Customer has been saved successfully")
        //this.newCustomerFormGroup.reset();
        this.router.navigateByUrl("/customer-accounts/"+this.newSavingFormGroup.value.customerId)
      },
      error : err =>{
        console.log(err);
      }
    });
  }
}
