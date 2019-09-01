package revolut.account.service.impl

import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import revolut.account.dao.AccountDao
import revolut.account.dao.TransactionDao
import revolut.account.dao.impl.AccountDaoRDB
import revolut.account.dao.impl.TransactionDaoRDB
import revolut.account.service.entity.Account
import revolut.account.service.entity.Transaction
import revolut.account.service.exception.*
import revolut.account.service.model.NewTransaction
import java.math.BigDecimal

@MicronautTest
internal class TransactionServiceImplTest(val transactionServiceImpl: TransactionServiceImpl) {

    @Test
    fun `test that transaction can be performed successfully`(accountDao: AccountDao,
                                                              transactionDao: TransactionDao) {
        every { accountDao.findByNumber(123) } returns Account(number = 123, balance = BigDecimal("100"))
        every { accountDao.findByNumber(321) } returns Account(number = 321, balance = BigDecimal("200"))
        every { transactionDao.create(any()) } answers { s -> s.invocation.args[0] as Transaction }

        val newTransaction = NewTransaction(debtorAccountNumber = 123, creditorAccountNumber = 321, amount = BigDecimal("23.0"))
        transactionServiceImpl.createTransaction(newTransaction)

        val slot = CapturingSlot<Transaction>()
        verify { transactionDao.create(capture(slot)) }
        val transaction = slot.captured
        assertEquals(newTransaction.amount, transaction.amount)
        assertEquals(newTransaction.debtorAccountNumber, transaction.debtorAccount.number)
        assertEquals(newTransaction.creditorAccountNumber, transaction.creditorAccount.number)
    }

    @Test
    fun `test that transaction fails when debtor account does not exist`(accountDao: AccountDao) {
        //preparation
        every { accountDao.findByNumber(123) } returns null
        val newTransaction = NewTransaction(debtorAccountNumber = 123, creditorAccountNumber = 321, amount = BigDecimal("23.0"))

        //action
        val exception = assertThrows<AbsentSenderAccountException> { transactionServiceImpl.createTransaction(newTransaction) }

        //assertion
        assertEquals(newTransaction.debtorAccountNumber, exception.senderAccount)
    }

    @Test
    fun `test that transaction fails when creditor account does not exist`(accountDao: AccountDao) {
        //preparation
        every { accountDao.findByNumber(123) } returns Account(number = 123, balance = BigDecimal("100"))
        every { accountDao.findByNumber(321) } returns null
        val newTransaction = NewTransaction(debtorAccountNumber = 123, creditorAccountNumber = 321, amount = BigDecimal("200.0"))

        //action
        val exception = assertThrows<AbsentReceiverAccountException> { transactionServiceImpl.createTransaction(newTransaction) }

        //assertion
        assertEquals(newTransaction.creditorAccountNumber, exception.receiverAccountNumber)
    }

    @Test
    fun `test that transaction fails when debtor does not have enough money on balance`(accountDao: AccountDao) {
        //preparation
        val debtorAccount = Account(number = 123, balance = BigDecimal("100"))
        every { accountDao.findByNumber(123) } returns debtorAccount
        every { accountDao.findByNumber(321) } returns Account(number = 321, balance = BigDecimal("200"))
        val newTransaction = NewTransaction(debtorAccountNumber = 123, creditorAccountNumber = 321, amount = BigDecimal("200.0"))

        //action
        val exception = assertThrows<ChargeAmountException> { transactionServiceImpl.createTransaction(newTransaction) }

        //assertion
        assertEquals(debtorAccount, exception.account)
        assertEquals(newTransaction.amount, exception.amount)
    }

    @Test
    fun `test that transaction fails when transaction amount is negative`() {
        //preparation
        val newTransaction = NewTransaction(debtorAccountNumber = 123, creditorAccountNumber = 321, amount = BigDecimal("-200.0"))

        //action
        val exception = assertThrows<NegativeTransactionAmountException> { transactionServiceImpl.createTransaction(newTransaction) }

        //assertion
        assertEquals(newTransaction.amount, exception.amount)
    }

    @Test
    fun `test that transaction fails when accounts are same`() {
        //preparation
        val newTransaction = NewTransaction(debtorAccountNumber = 123, creditorAccountNumber = 123, amount = BigDecimal("200.0"))

        //action
        val exception = assertThrows<SameAccountsException> { transactionServiceImpl.createTransaction(newTransaction) }

        //assertion
        assertEquals(newTransaction.debtorAccountNumber, exception.accountNumber)
    }

    @MockBean(AccountDaoRDB::class)
    fun accountDaoMock(): AccountDaoRDB {
        return mockk()
    }

    @MockBean(TransactionDaoRDB::class)
    fun transactionDaoMock(): TransactionDaoRDB {
        return mockk()
    }

    companion object {
        @AfterAll
        @JvmStatic
        fun destroy(embeddedServer: EmbeddedServer) {
            embeddedServer.close()
        }
    }
}