package revolut.account.controller.exception

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import revolut.account.controller.dto.ErrorCodes
import revolut.account.controller.dto.ErrorDto
import revolut.account.service.exception.NegativeTransactionAmountException
import javax.inject.Singleton

@Produces
@Singleton
@Requires(classes = [NegativeTransactionAmountException::class, ExceptionHandler::class])
class NegativeTransactionAmountExceptionHandler : ExceptionHandler<NegativeTransactionAmountException, HttpResponse<ErrorDto>> {
    override fun handle(request: HttpRequest<*>?, exception: NegativeTransactionAmountException): HttpResponse<ErrorDto> {
        val payload = mapOf("amount" to exception.amount)
        val errorDto = ErrorDto(ErrorCodes.NEGATIVE_TRANSACTION_AMOUNT, payload)

        return HttpResponse.status<ErrorDto>(HttpStatus.NOT_ACCEPTABLE).body(errorDto)
    }
}