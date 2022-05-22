import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomersComponent} from "./customers/customers.component";
import {AccountsComponent} from "./accounts/accounts.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {CustomerAccountsComponent} from "./customer-accounts/customer-accounts.component";
import {NewAccountComponent} from "./new-account/new-account.component";

const routes: Routes = [
  {path:"customers",component:CustomersComponent},
  {path:"accounts",component:AccountsComponent},
  {path:"new-customer",component:NewCustomerComponent},
  {path:"new-accounts",component:NewAccountComponent},
  {path:"customer-accounts/:id",component:CustomerAccountsComponent},
  {path:"accounts/:id",component:AccountsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
