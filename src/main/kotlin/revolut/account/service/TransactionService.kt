package revolut.account.service

import revolut.account.service.model.NewTransaction

interface TransactionService {

    fun createTransaction(newTransaction: NewTransaction)
}