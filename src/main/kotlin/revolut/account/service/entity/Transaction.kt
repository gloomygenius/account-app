package revolut.account.service.entity

import java.math.BigDecimal

class Transaction(
        val id: Long,
        val senderAccount: Int,
        val receiverAccount: Int,
        val amount: BigDecimal) {
}