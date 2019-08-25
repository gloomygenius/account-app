package revolut.account.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import revolut.account.controller.dto.NewTransactionRequest

@Controller("/transaction")
class TransactionController {
    @Post(consumes = [MediaType.APPLICATION_JSON],
            processes = [MediaType.APPLICATION_JSON])
    fun createTransaction(newTransactionRequest: NewTransactionRequest) {
        //implement it
    }
}