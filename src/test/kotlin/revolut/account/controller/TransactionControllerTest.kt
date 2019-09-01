package revolut.account.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import revolut.account.controller.dto.ErrorDto
import revolut.account.controller.dto.NewTransactionRequest
import revolut.account.service.entity.Account
import revolut.account.service.exception.*
import revolut.account.service.impl.TransactionServiceImpl
import java.math.BigDecimal
import java.util.stream.Stream

@MicronautTest
class TransactionControllerTest(private val embeddedServer: EmbeddedServer,
                                private val transactionService: TransactionServiceImpl) {

    @Test
    fun `test that endpoint 'transaction' can correctly handle request`() {
        //preparation
        every { transactionService.createTransaction(any()) } just Runs
        val request = NewTransactionRequest(
                senderAccount = 123123,
                receiverAccount = 124123,
                amount = BigDecimal("23.00"))
        val client: HttpClient = HttpClient.create(embeddedServer.url)
        val httpRequest: MutableHttpRequest<NewTransactionRequest> = HttpRequest.POST("/transaction", request)

        //action
        val response = client.toBlocking().exchange<NewTransactionRequest, Unit>(httpRequest)

        //assertion
        assertEquals(HttpStatus.OK, response.status)
        assertNull(response.body())
        verify { transactionService.createTransaction(any()) }
        client.close()
        embeddedServer.close()
    }

    @ParameterizedTest
    @MethodSource("possibleExceptions")
    fun `test that endpoint 'transaction' correctly handle exceptions`(ex: Exception, expectedHttpStatus: HttpStatus) {
        //preparation
        every { transactionService.createTransaction(any()) } throws ex
        val request = NewTransactionRequest(
                senderAccount = 123123,
                receiverAccount = 124123,
                amount = BigDecimal("23.00"))
        val client: HttpClient = HttpClient.create(embeddedServer.url)
        val httpRequest: MutableHttpRequest<NewTransactionRequest> = HttpRequest.POST("/transaction", request)

        //action
        val exception = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange<NewTransactionRequest, ErrorDto>(httpRequest)
        }

        //assertion
        val response = exception.response
        assertEquals(expectedHttpStatus, response.status)
    }

    @MockBean(TransactionServiceImpl::class)
    fun transactionServiceMock(): TransactionServiceImpl = mockk()

    companion object {
        @JvmStatic
        fun possibleExceptions(): Stream<Arguments> {
            val account = Account(123, "23".toBigDecimal())
            return Stream.of(
                    arguments(AbsentSenderAccountException(123), HttpStatus.NOT_ACCEPTABLE),
                    arguments(AbsentReceiverAccountException(123), HttpStatus.NOT_ACCEPTABLE),
                    arguments(AccountModificationException(account), HttpStatus.NOT_ACCEPTABLE),
                    arguments(ChargeAmountException(account, "100".toBigDecimal()), HttpStatus.NOT_ACCEPTABLE),
                    arguments(NegativeTransactionAmountException("100".toBigDecimal()), HttpStatus.NOT_ACCEPTABLE),
                    arguments(SameAccountsException(123), HttpStatus.NOT_ACCEPTABLE)
            )
        }

        @AfterAll
        @JvmStatic
        fun destroy(embeddedServer: EmbeddedServer) {
            embeddedServer.close()
        }
    }
}