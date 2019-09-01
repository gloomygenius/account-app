package revolut.account.service.exception

import revolut.account.service.entity.Account

class AccountModificationException(val account: Account) : RuntimeException()
