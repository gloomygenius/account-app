package revolut.account.service.entity

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "transaction")
data class Transaction(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @OneToOne
        @JoinColumn(name = "debtor_account_number")
        var debtorAccount: Account,
        @OneToOne
        @JoinColumn(name = "creditor_account_number")
        var creditorAccount: Account,
        var amount: BigDecimal) {
}