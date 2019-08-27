package revolut.account.controller.dto

import revolut.account.service.model.NewTransaction
import java.math.BigDecimal

data class NewTransactionRequest(
        val senderAccount: Int,
        val receiverAccount: Int,
        val amount: BigDecimal
) {
    fun toModel(): NewTransaction {
        return NewTransaction(
                debtorAccountNumber = senderAccount,
                creditorAccountNumber = receiverAccount,
                amount = amount
        )
    }
}
