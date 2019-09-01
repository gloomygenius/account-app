package revolut.account.controller.exception

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import revolut.account.controller.dto.ErrorCodes
import revolut.account.controller.dto.ErrorDto
import revolut.account.service.exception.AccountModificationException
import javax.inject.Singleton

@Produces
@Singleton
@Requires(classes = [AccountModificationException::class, ExceptionHandler::class])
class AccountModificationExceptionHandler() : ExceptionHandler<AccountModificationException, HttpResponse<ErrorDto>> {
    override fun handle(request: HttpRequest<*>?, exception: AccountModificationException): HttpResponse<ErrorDto> {
        val errorDto = ErrorDto(ErrorCodes.ACCOUNT_WAS_MODIFIED, mapOf("accountNumber" to exception.account.number))

        return HttpResponse.status<ErrorDto>(HttpStatus.NOT_ACCEPTABLE).body(errorDto)
    }

}