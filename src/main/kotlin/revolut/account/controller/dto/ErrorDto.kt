package revolut.account.controller.dto

data class ErrorDto(val code: String, val payload: Map<String, Any>? = null)