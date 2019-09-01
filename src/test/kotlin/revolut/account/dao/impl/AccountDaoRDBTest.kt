package revolut.account.dao.impl

import com.github.database.rider.core.api.connection.ConnectionHolder
import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.junit5.api.DBRider
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import javax.sql.DataSource

@MicronautTest
internal class AccountDaoRDBTest(private val accountDaoRDB: AccountDaoRDB,
                                 private val dataSource: DataSource) {
    private val connectionHolder: ConnectionHolder = ConnectionHolder { return@ConnectionHolder dataSource.connection }

    @Test
    @DBRider
    @DataSet("datasets/two_accounts.yml")
    fun `test that findByNumber() should return Account when it's present`() {
        val account = accountDaoRDB.findByNumber(111111)
        assertNotNull(account)
        account!!
        assertEquals(account.number, 111111)
        assertTrue(account.balance.compareTo(BigDecimal("3000.00")) == 0)
        assertEquals(account.revision, 0)
    }

    @Test
    @DBRider
    @DataSet("datasets/two_accounts.yml")
    fun `test that findByNumber() should return null when it's not present`() {
        val notExistsAccount = 999999
        val account = accountDaoRDB.findByNumber(notExistsAccount)
        assertNull(account)
    }
}