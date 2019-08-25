package revolut.account.controller.dto

import java.math.BigDecimal

data class NewTransactionRequest(
        val senderAccount: Int,
        val receiverAccount: Int,
        val amount: BigDecimal
)
