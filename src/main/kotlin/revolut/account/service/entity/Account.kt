package revolut.account.service.entity

import revolut.account.service.exception.InsufficientBalanceException
import java.math.BigDecimal

class Account(
        val number: Int,
        var balance: BigDecimal,
        var lockedBalance: BigDecimal,
        var revision: Int = 0
) {
    fun lockMoney(amount: BigDecimal) {
        if (balance - lockedBalance < amount) {
            throw InsufficientBalanceException()
        }
        lockedBalance += amount
    }

    fun increaseBalance(amount: BigDecimal) {
        balance += amount
    }

    fun unlockMoney(amount: BigDecimal) {
        lockedBalance -= amount
    }

    fun charge(amount: BigDecimal) {
        lockedBalance -= amount
        balance -= amount
    }

}
