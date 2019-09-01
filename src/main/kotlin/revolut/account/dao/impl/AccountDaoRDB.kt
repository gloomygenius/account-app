package revolut.account.dao.impl

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import revolut.account.dao.AccountDao
import revolut.account.service.entity.Account
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Singleton
open class AccountDaoRDB(@PersistenceContext @CurrentSession val em: EntityManager) : AccountDao {

    override fun findByNumber(accountNumber: Int): Account? {
        return em.find(Account::class.java, accountNumber)
    }
}