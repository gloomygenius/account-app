package revolut.account.service.impl

import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.mockk
import org.junit.jupiter.api.Test
import revolut.account.dao.impl.AccountDaoImpl
import revolut.account.dao.impl.TransactionDaoImpl
import revolut.account.service.model.NewTransaction
import java.math.BigDecimal

@MicronautTest
internal class TransactionServiceImplTest(val transactionServiceImpl: TransactionServiceImpl) {
    @Test
    fun `test that transaction can be created successfully`() {
        val newTransaction = NewTransaction(debtorAccountNumber = 123, creditorAccountNumber = 321, amount = BigDecimal("23.0"))
        transactionServiceImpl.createTransaction(newTransaction)
    }

    @MockBean(AccountDaoImpl::class)
    fun accountDaoMock(): AccountDaoImpl {
        return mockk()
    }

    @MockBean(TransactionDaoImpl::class)
    fun transactionDaoMock(): TransactionDaoImpl {
        return mockk()
    }
}