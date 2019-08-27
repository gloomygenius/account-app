package revolut.account.service.model

import java.math.BigDecimal

data class NewTransaction(
        val debtorAccountNumber: Int,
        val creditorAccountNumber: Int,
        val amount: BigDecimal
)
