package revolut.account.service.entity

import revolut.account.service.exception.ChargeAmountException
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "account")
data class Account(
        @Id
        var number: Int,
        var balance: BigDecimal,
        @Version
        val revision: Int = 0
) {
    fun increaseBalance(amount: BigDecimal) {
        balance += amount
    }

    fun charge(amount: BigDecimal) {
        if (balance < amount) throw ChargeAmountException(this, amount)
        balance -= amount
    }
}
