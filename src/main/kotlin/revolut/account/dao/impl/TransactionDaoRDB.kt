package revolut.account.dao.impl

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import revolut.account.dao.TransactionDao
import revolut.account.service.entity.Transaction
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Singleton
open class TransactionDaoRDB(@PersistenceContext @CurrentSession private val em: EntityManager) : TransactionDao {
    override fun create(newTransaction: Transaction): Transaction {
        em.persist(newTransaction)
        return newTransaction
    }
}