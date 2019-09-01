package revolut.account.service.impl

import io.micronaut.spring.tx.annotation.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import revolut.account.dao.AccountDao
import revolut.account.dao.TransactionDao
import revolut.account.service.TransactionService
import revolut.account.service.entity.Account
import revolut.account.service.entity.Transaction
import revolut.account.service.exception.*
import revolut.account.service.model.NewTransaction
import java.math.BigDecimal
import javax.inject.Singleton
import javax.persistence.OptimisticLockException

@Singleton
open class TransactionServiceImpl(private val accountDao: AccountDao,
                                  private val transactionDao: TransactionDao) : TransactionService {
    private val log: Logger = LoggerFactory.getLogger(TransactionServiceImpl::class.java)

    @Transactional
    override fun createTransaction(newTransaction: NewTransaction) {
        try {
            if (newTransaction.amount <= BigDecimal.ZERO) {
                throw NegativeTransactionAmountException(newTransaction.amount)
            }
            if (newTransaction.creditorAccountNumber == newTransaction.debtorAccountNumber) {
                throw SameAccountsException(newTransaction.creditorAccountNumber)
            }
            val debtorAccount = accountDao.findByNumber(newTransaction.debtorAccountNumber)
                    ?: throw AbsentSenderAccountException(newTransaction.debtorAccountNumber)

            val creditorAccount = accountDao.findByNumber(newTransaction.creditorAccountNumber)
                    ?: throw AbsentReceiverAccountException(newTransaction.creditorAccountNumber)

            val transaction = Transaction(
                    debtorAccount = debtorAccount,
                    creditorAccount = creditorAccount,
                    amount = newTransaction.amount)

            debtorAccount.charge(amount = transaction.amount)
            creditorAccount.increaseBalance(amount = transaction.amount)
            transactionDao.create(transaction)
        } catch (ex: OptimisticLockException) {
            val account = ex.entity as Account
            throw AccountModificationException(account)
        }
    }
}
