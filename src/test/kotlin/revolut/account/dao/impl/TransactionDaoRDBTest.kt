package revolut.account.dao.impl

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.core.api.dataset.ExpectedDataSet
import com.github.database.rider.junit5.api.DBRider
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import revolut.account.service.entity.Account
import revolut.account.service.entity.Transaction
import java.math.BigDecimal
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.sql.DataSource

@MicronautTest
internal class TransactionDaoRDBTest(val transactionDaoRDB: TransactionDaoRDB,
                                     val dataSource: DataSource,
                                     @PersistenceContext @CurrentSession val em: EntityManager) {
    val connectionHolder: ConnectionHolder = ConnectionHolder { return@ConnectionHolder dataSource.connection }

    @Test
    @DBRider
    @DataSet("datasets/two_accounts.yml")
    @ExpectedDataSet("datasets/transaction/created_transaction.yml")
    fun create() {
        val debtorAccount = Account(number = 111111, balance = BigDecimal("3000.00"))
        val creditorAccount = Account(number = 222222, balance = BigDecimal("1000.00"))
        val transaction = Transaction(
                debtorAccount = debtorAccount,
                creditorAccount = creditorAccount,
                amount = BigDecimal("100"))
        transactionDaoRDB.create(transaction)
        em.transaction.commit()
    }
}