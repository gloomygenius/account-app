package revolut.account.dao

import revolut.account.service.entity.Transaction

interface TransactionDao {

    fun create(newTransaction: Transaction): Transaction
}
