package revolut.account.controller.exception

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import revolut.account.controller.dto.ErrorCodes
import revolut.account.controller.dto.ErrorDto
import revolut.account.service.exception.AbsentReceiverAccountException
import javax.inject.Singleton

@Produces
@Singleton
@Requires(classes = [AbsentReceiverAccountException::class, ExceptionHandler::class])
class AbsentReceiverAccountExceptionHandler() : ExceptionHandler<AbsentReceiverAccountException, HttpResponse<ErrorDto>> {
    override fun handle(request: HttpRequest<*>?, exception: AbsentReceiverAccountException): HttpResponse<ErrorDto> {
        val errorDto = ErrorDto(ErrorCodes.DEBTOR_ACCOUNT_DOES_NOT_EXIST, mapOf("debtorAccount" to exception.receiverAccountNumber))
        return HttpResponse.status<ErrorDto>(HttpStatus.NOT_ACCEPTABLE).body(errorDto)
    }
}