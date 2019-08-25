package revolut.account

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("revolut.account")
                .mainClass(Application.javaClass)
                .start()
    }
}