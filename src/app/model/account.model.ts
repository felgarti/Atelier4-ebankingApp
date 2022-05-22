export interface AccountDetails {
  accountOperationDTOS: AccountOperation[];
  accountId:            string;
  id :                  string;
  balance:              number;
  currentPage:          number;
  totalPages:           number;
  pageSize:             number;
}

export interface AccountOperation{
  id:            number;
  operationDate: Date;
  amount:        number;
  type:          string;
  description:   string;
}
