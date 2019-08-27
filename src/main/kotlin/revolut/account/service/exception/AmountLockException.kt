package revolut.account.service.exception

import revolut.account.service.entity.Account
import java.lang.RuntimeException
import java.math.BigDecimal

class AmountLockException(val account: Account, val amount: BigDecimal) : RuntimeException()