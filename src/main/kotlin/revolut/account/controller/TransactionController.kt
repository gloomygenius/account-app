package revolut.account.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import revolut.account.controller.dto.NewTransactionRequest
import revolut.account.service.TransactionService

@Controller("/transaction")
class TransactionController(private val transactionService: TransactionService) {

    @Post(consumes = [MediaType.APPLICATION_JSON],
            processes = [MediaType.APPLICATION_JSON])
    fun createTransaction(newTransactionRequest: NewTransactionRequest) {

        transactionService.createTransaction(newTransactionRequest.toModel())
    }
}