package revolut.account.dao

import revolut.account.service.entity.Account

interface AccountDao {

    fun findByNumber(accountNumber: Int): Account?
}
