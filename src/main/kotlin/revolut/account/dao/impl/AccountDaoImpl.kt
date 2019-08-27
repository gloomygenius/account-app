package revolut.account.dao.impl

import revolut.account.dao.AccountDao
import revolut.account.service.entity.Account
import javax.inject.Singleton

@Singleton
open class AccountDaoImpl : AccountDao {
    override fun findByNumber(senderAccount: Int): Account? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(copy: Account): Account? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}