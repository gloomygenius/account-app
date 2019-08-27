package revolut.account.service

import revolut.account.service.exception.AbsentReceiverAccountException
import revolut.account.service.exception.AbsentSenderAccountException
import revolut.account.service.model.NewTransaction

interface TransactionService {
    /**
     * Method to create new transaction between accounts.
     *
     * @throws AbsentSenderAccountException
     * @throws AbsentReceiverAccountException
     */
    fun createTransaction(newTransaction: NewTransaction)
}