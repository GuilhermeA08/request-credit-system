package dev.guilherme.requestcreditsystem.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.guilherme.requestcreditsystem.dto.CreditDto
import dev.guilherme.requestcreditsystem.entity.Address
import dev.guilherme.requestcreditsystem.entity.Customer
import dev.guilherme.requestcreditsystem.repository.CreditRepository
import dev.guilherme.requestcreditsystem.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL = "/api/credits"
    }

    //Instaciar um customer para o teste


    @BeforeEach
    fun setUp() {
        this.creditRepository.deleteAll()
        this.customerRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        this.creditRepository.deleteAll()
        this.customerRepository.deleteAll()
    }

    @Test
    fun `should save a credit and return 201`() {
        // given
        val customer = builderCustomer()
        this.customerRepository.save(customer)

        val creditDto = builderCreditDTO(customerId = customer.id!!)
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)

        // when
        // then

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(creditDto.creditValue))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(creditDto.numberOfInstallments)
            )
            .andDo(MockMvcResultHandlers.print())


    }

    @Test
    fun `should not save a credit with invalid customer and return 400`() {
        // given
        val creditDto = builderCreditDTO(customerId = 200L)
        val customer = builderCustomer()
        this.customerRepository.save(customer)
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)

        // when
        // then

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class dev.guilherme.requestcreditsystem.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find all credit by customer id and return 200`() {
        // given
        val customer = builderCustomer()
        this.customerRepository.save(customer)

        val creditDto = builderCreditDTO()
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)
        creditRepository.save(creditDto.toEntity())

        // when
        // then

        mockMvc.perform(
            MockMvcRequestBuilders.get("${URL}?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(creditDto.creditValue))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(creditDto.numberOfInstallments)
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by creditCode and return 200`() {
        // given
        val customer = builderCustomer()
        this.customerRepository.save(customer)

        val creditDto = builderCreditDTO(customerId = customer.id!!)
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)
        val credit = creditRepository.save(creditDto.toEntity())

        // when
        // then

        mockMvc.perform(
            MockMvcRequestBuilders.get("${URL}/${credit.creditCode}?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(creditDto.creditValue))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(creditDto.numberOfInstallments)
            )
            .andDo(MockMvcResultHandlers.print())
    }

    private fun builderCreditDTO(
        creditValue: BigDecimal = BigDecimal("1000.0"),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusDays(30),
        numberOfInstallments: Int = 10,
        customerId: Long = 1L
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId,

        )

    private fun builderCustomer(

    ) = Customer(
        firstName = "teste",
        lastName = "teste",
        income = BigDecimal("1000.00"),
        cpf = "585.488.590-59",
        email = "abc@email.com",
        password = "123456",
        address = Address(
            zipCode = "12345678",
            street = "Rua teste",
        )
    )
}





