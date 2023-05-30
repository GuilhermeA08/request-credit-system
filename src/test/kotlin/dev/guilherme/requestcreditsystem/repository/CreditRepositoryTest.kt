package dev.guilherme.requestcreditsystem.repository

import dev.guilherme.requestcreditsystem.entity.Address
import dev.guilherme.requestcreditsystem.entity.Credit
import dev.guilherme.requestcreditsystem.entity.Customer
import dev.guilherme.requestcreditsystem.enummeration.Status
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.UUID

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach fun setup () {
        customer = testEntityManager.persist(buildCustomer())
        credit1 = testEntityManager.persist(buildCredit(customer = customer))
        credit2 = testEntityManager.persist(buildCredit(customer = customer))
    }

    @Test
    fun `should find credit by credit code`() {
        //given
        val creditCode1 = UUID.fromString("1a049102-ec82-4269-b902-57a5767d95ee")
        val creditCode2 = UUID.fromString("517a63e9-1892-4b75-8b28-72876e95d559")
        credit1.creditCode = creditCode1
        credit2.creditCode = creditCode2
        //when
        val fakeCredit1: Credit = creditRepository.findByCreditCode(creditCode1)!!
        val fakeCredit2: Credit = creditRepository.findByCreditCode(creditCode2)!!
        //then
        Assertions.assertThat(fakeCredit1).isNotNull
        Assertions.assertThat(fakeCredit2).isNotNull
        Assertions.assertThat(fakeCredit1).isSameAs(credit1)
        Assertions.assertThat(fakeCredit2).isSameAs(credit2)

        Assertions.assertThat(fakeCredit1.creditCode).isEqualTo(creditCode1)
        Assertions.assertThat(fakeCredit2.creditCode).isEqualTo(creditCode2)

        Assertions.assertThat(fakeCredit1.customer!!.id).isEqualTo(fakeCredit2.customer!!.id)
    }

    @Test
    fun `should find all credits by customer id`() {
        //given
        val customerId: Long = 1L
        //when
        val creditList: List<Credit> = creditRepository.findAllByCustomerId(customerId)

        //then
        Assertions.assertThat(creditList).isNotEmpty
        Assertions.assertThat(creditList).hasSize(2)
        Assertions.assertThat(creditList[0].customer!!.id).isEqualTo(customerId)
        Assertions.assertThat(creditList[1].customer!!.id).isEqualTo(customerId)
    }

    private fun buildCustomer(
        firstName: String = "Guilherme",
        lastName: String = "Santos",
        cpf: String = "414.119.250-33",
        income: BigDecimal = BigDecimal(1000),
        email: String = "abc@email.com",
        password: String = "123456",
        street: String = "Rua 1",
        zipCode: String = "4565489",
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        income = income,
        address = Address(
            street = street,
            zipCode = zipCode
        ),
        email = email,
        password = password
    )

    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal(1000),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusDays(30),
        numberOfInstallments: Int = 10,
        customer: Customer,
        status: Status = Status.IN_PROGRESS,
    ) = Credit(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer,
        status = status,
    )

}