package revolut.account.service.impl

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

@Singleton
open class TransactionServiceImpl(private val accountDao: AccountDao,
                                  private val transactionDao: TransactionDao) : TransactionService {
    private val log: Logger = LoggerFactory.getLogger(TransactionServiceImpl::class.java)

    override fun createTransaction(newTransaction: NewTransaction) {

        val debtorAccount = accountDao.findByNumber(newTransaction.debtorAccountNumber)
                ?: throw AbsentSenderAccountException(newTransaction.debtorAccountNumber)

        val creditorAccount = accountDao.findByNumber(newTransaction.creditorAccountNumber)
                ?: throw AbsentReceiverAccountException(newTransaction.creditorAccountNumber)

        val senderAccountWithLockedMoney = lockAmount(debtorAccount, newTransaction.amount)

        var transaction: Transaction? = null
        try {
            transaction = transactionDao.create(newTransaction)
            increaseCreditorBalance(creditorAccount, transaction.amount)
            chargeAmount(debtorAccount, transaction.amount)
        } catch (ex: Exception) {
            unlockAmount(senderAccountWithLockedMoney, newTransaction.amount)
            transaction?.let { transactionDao.markAsFailed(it) }
            log.error("Transaction was failed")
            throw ex
        }
    }

    private fun chargeAmount(account: Account, amount: BigDecimal): Account {
        return updateAccount(
                account = account,
                block = { it.charge(amount) },
                numberOfAttempts = 5) ?: throw ChargeAmountException(account, amount)
    }

    private fun increaseCreditorBalance(account: Account, amount: BigDecimal): Account {
        return updateAccount(
                account = account,
                block = { it.increaseBalance(amount) },
                numberOfAttempts = 3) ?: throw CreditAmountException(account, amount)
    }

    private fun lockAmount(account: Account, amount: BigDecimal): Account {
        return updateAccount(
                account = account,
                block = { it.lockMoney(amount) },
                numberOfAttempts = 3) ?: throw AmountLockException(account, amount)
    }

    private fun unlockAmount(account: Account, amount: BigDecimal): Account {
        return updateAccount(
                account = account,
                block = { it.unlockMoney(amount) },
                numberOfAttempts = 3) ?: throw AmountUnlockException(account, amount)
    }

    private fun updateAccount(account: Account, block: (Account) -> Unit, numberOfAttempts: Int): Account? {
        var accountToUpdate = account
        var updatedAccount: Account? = null
        var count = numberOfAttempts
        while ((updatedAccount == null) and (count < 3)) {
            block(accountToUpdate)
            updatedAccount = accountDao.update(accountToUpdate)
            count++
            if (updatedAccount == null) {
                accountToUpdate = accountDao.findByNumber(account.number)!!
            }
        }
        return updatedAccount
    }
}
