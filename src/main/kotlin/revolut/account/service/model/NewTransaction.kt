package revolut.account.service.model

import java.math.BigDecimal

data class NewTransaction(
        val senderAccount: Int,
        val receiverAccount: Int,
        val amount: BigDecimal
)
