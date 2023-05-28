package dev.guilherme.requestcreditsystem.dto

import dev.guilherme.requestcreditsystem.entity.Customer

data class CustomerUpdateDto(
    val firstName: String,
    val lastName: String,
    val income: String,
    val zipCode: String,
    val street: String,
){
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income.toBigDecimal()
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street

        return customer
    }
}
