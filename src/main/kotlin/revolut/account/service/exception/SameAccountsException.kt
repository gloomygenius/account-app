package revolut.account.service.exception

class SameAccountsException(val accountNumber: Int) : RuntimeException()
