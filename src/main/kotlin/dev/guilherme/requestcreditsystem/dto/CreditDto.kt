package dev.guilherme.requestcreditsystem.dto

import dev.guilherme.requestcreditsystem.entity.Credit
import dev.guilherme.requestcreditsystem.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(

    @field:NotNull(message = "Credit value is required")
    val creditValue: BigDecimal,
    @field:Future(message = "Date must be in the future")
    @field:NotNull(message = "Date is required")
    val dayFirstOfInstallment: LocalDate,
    @field:NotNull(message = "Number of installments is required")
    @field:Min(value = 1, message = "Credit value must be greater than 0")
    @field:Max(value = 48, message = "Credit value must be <= 48")
    val numberOfInstallments: Int,
    @field:NotNull(message = "Customer id is required")
    val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstOfInstallment = this.dayFirstOfInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
