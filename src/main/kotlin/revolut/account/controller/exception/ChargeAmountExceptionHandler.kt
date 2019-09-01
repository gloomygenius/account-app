package revolut.account.controller.exception

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import revolut.account.controller.dto.ErrorCodes
import revolut.account.controller.dto.ErrorDto
import revolut.account.service.exception.ChargeAmountException
import javax.inject.Singleton

@Produces
@Singleton
@Requires(classes = [ChargeAmountException::class, ExceptionHandler::class])
class ChargeAmountExceptionHandler() : ExceptionHandler<ChargeAmountException, HttpResponse<ErrorDto>> {
    override fun handle(request: HttpRequest<*>?, exception: ChargeAmountException): HttpResponse<ErrorDto> {
        val payload = mapOf(
                "debtorAccount" to exception.account.number,
                "amount" to exception.amount)
        val errorDto = ErrorDto(ErrorCodes.ACCOUNT_HAS_NOT_ENOUGH_MONEY, payload)

        return HttpResponse.status<ErrorDto>(HttpStatus.NOT_ACCEPTABLE).body(errorDto)
    }
}