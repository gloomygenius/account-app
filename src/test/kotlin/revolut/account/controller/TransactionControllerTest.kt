package revolut.account.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import revolut.account.controller.dto.NewTransactionRequest
import revolut.account.service.impl.TransactionServiceImpl
import java.math.BigDecimal

@MicronautTest
class TransactionControllerTest(private val embeddedServer: EmbeddedServer,
                                private val transactionService: TransactionServiceImpl) {

    @Test
    fun `test endpoint for creation transaction`() {
        //preparation
        every { transactionService.createTransaction(any()) } just Runs
        val request = NewTransactionRequest(
                senderAccount = 123123,
                receiverAccount = 124123,
                amount = BigDecimal("23.00"))
        val client: HttpClient = HttpClient.create(embeddedServer.url)
        val httpRequest:MutableHttpRequest<NewTransactionRequest> = HttpRequest.POST("/transaction", request)

        //action
        val response = client.toBlocking().exchange<NewTransactionRequest, Unit>(httpRequest)

        //assertion
        assertEquals(HttpStatus.OK, response.status)
        assertNull(response.body())
        verify { transactionService.createTransaction(any()) }
        client.close()
        embeddedServer.close()
    }

    @MockBean(TransactionServiceImpl::class)
    fun transactionServiceMock(): TransactionServiceImpl = mockk()
}