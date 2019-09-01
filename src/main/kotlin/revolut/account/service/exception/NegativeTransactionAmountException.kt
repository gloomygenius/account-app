package revolut.account.service.exception

import java.math.BigDecimal

class NegativeTransactionAmountException(val amount: BigDecimal) : RuntimeException()
