package revolut.account.controller

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import revolut.account.controller.dto.NewTransactionRequest
import java.math.BigDecimal

class TransactionControllerTest {

    @Test
    fun `test endpoint for creation transaction`() {
        val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
        val client: HttpClient = HttpClient.create(embeddedServer.url)
        val request = NewTransactionRequest(
                senderAccount = 123123,
                receiverAccount = 124123,
                amount = BigDecimal("23.00")
        )
        val response = client.toBlocking().exchange<NewTransactionRequest, Unit>(HttpRequest.POST("/transaction", request))
        assertEquals(HttpStatus.OK, response.status)
        assertNull(response.body())
        client.close()
        embeddedServer.close()
    }
}