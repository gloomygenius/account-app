package revolut.account.dao

import revolut.account.service.entity.Transaction
import revolut.account.service.model.NewTransaction

interface TransactionDao {

    fun create(newTransaction: NewTransaction): Transaction

    fun markAsFailed(transaction: Transaction)
}
