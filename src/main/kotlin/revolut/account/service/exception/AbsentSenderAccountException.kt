package revolut.account.service.exception

class AbsentSenderAccountException(val senderAccount: Int) : RuntimeException()