package dev.guilherme.requestcreditsystem.service

import dev.guilherme.requestcreditsystem.entity.Credit
import dev.guilherme.requestcreditsystem.entity.Customer
import dev.guilherme.requestcreditsystem.enummeration.Status
import dev.guilherme.requestcreditsystem.repository.CreditRepository
import dev.guilherme.requestcreditsystem.service.impl.CreditService
import dev.guilherme.requestcreditsystem.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {


    @MockK
    lateinit var customerService: CustomerService

    @MockK
    lateinit var creditRepository: CreditRepository
    @InjectMockKs
    lateinit var creditServiceImpl: CreditService

    @Test
    fun `should create credit`() {
        //given
        val fakeCredit = buildCredit()
        val customerId = 1L

        every { customerService.findById(customerId) } returns fakeCredit.customer!!
        every { creditRepository.save(any()) } returns fakeCredit

        //when
        val actual: Credit = creditServiceImpl.save(fakeCredit)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should list all credits by customer`(){
        //given
        val fakeCredit = buildCredit()
        val customerId = 1L

        every { creditRepository.findAllByCustomerId(customerId) } returns listOf(fakeCredit)

        //when
        val actual: List<Credit> = creditServiceImpl.findAllByCustomer(customerId)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).hasSize(1)
        Assertions.assertThat(actual).contains(fakeCredit)
        verify(exactly = 1) { creditRepository.findAllByCustomerId(customerId) }
    }

    @Test
    fun `should find credit by creditCode`(){
        //given
        val fakeCredit = buildCredit()
        val creditCode = fakeCredit.creditCode
        val customerId = 1L

        every { creditRepository.findByCreditCode(creditCode) } returns fakeCredit
        //when

        val actual: Credit = creditServiceImpl.findByCreditCode(creditCode, customerId)

        //then

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }




companion object {
    private fun buildCredit(
        customer: Customer = CustomerServiceTest.buildCustomer(),
        creditValue: BigDecimal = BigDecimal.valueOf(1000),
        dayFirstOfInstallment: LocalDate = LocalDate.now(),
        numberOfInstallments: Int = 1,
        status: Status = Status.IN_PROGRESS,
        id: Long = 1L,
    ) = Credit(
        customer = customer,
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        status = status,
        id = id
    )
}
}