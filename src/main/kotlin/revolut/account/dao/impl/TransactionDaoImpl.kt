package revolut.account.dao.impl

import revolut.account.dao.TransactionDao
import revolut.account.service.entity.Transaction
import revolut.account.service.model.NewTransaction
import javax.inject.Singleton

@Singleton
open class TransactionDaoImpl : TransactionDao {
    override fun create(newTransaction: NewTransaction): Transaction {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun markAsFailed(transaction: Transaction) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}