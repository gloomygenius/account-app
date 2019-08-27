package revolut.account.dao

import revolut.account.service.entity.Account

interface AccountDao {

    fun findByNumber(senderAccount: Int): Account?

    /**
     * Update Account.
     * @return account with new revision or null if entity has expired revision
     */
    fun update(copy: Account): Account?
}
