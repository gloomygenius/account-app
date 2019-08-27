package revolut.account.service.exception

class AbsentReceiverAccountException(val receiverAccountNumber: Int) : RuntimeException()
