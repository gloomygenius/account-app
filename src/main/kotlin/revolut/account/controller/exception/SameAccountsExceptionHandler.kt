package revolut.account.controller.exception

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import revolut.account.controller.dto.ErrorCodes
import revolut.account.controller.dto.ErrorDto
import revolut.account.service.exception.SameAccountsException
import javax.inject.Singleton

@Produces
@Singleton
@Requires(classes = [SameAccountsException::class, ExceptionHandler::class])
class SameAccountsExceptionHandler : ExceptionHandler<SameAccountsException, HttpResponse<ErrorDto>> {
    override fun handle(request: HttpRequest<*>?, exception: SameAccountsException): HttpResponse<ErrorDto> {
        val payload = mapOf("accountNumber" to exception.accountNumber)
        val errorDto = ErrorDto(ErrorCodes.SAME_ACCOUNTS, payload)

        return HttpResponse.status<ErrorDto>(HttpStatus.NOT_ACCEPTABLE).body(errorDto)
    }
}