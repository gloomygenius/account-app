package revolut.account.controller.dto

object ErrorCodes {
    const val ACCOUNT_WAS_MODIFIED = "account-was-modified "
    const val ACCOUNT_HAS_NOT_ENOUGH_MONEY = "account-has-not-enough-money"
    const val DEBTOR_ACCOUNT_DOES_NOT_EXIST = "debtor-account-does-not-exist"
    const val CREDITOR_ACCOUNT_DOES_NOT_EXIST = "creditor-account-does-not-exist"
    const val NEGATIVE_TRANSACTION_AMOUNT = "negative-transaction-amount"
    const val SAME_ACCOUNTS = "same-accounts"
}