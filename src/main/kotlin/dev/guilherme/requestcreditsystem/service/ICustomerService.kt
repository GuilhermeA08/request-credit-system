package dev.guilherme.requestcreditsystem.service

import dev.guilherme.requestcreditsystem.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer
    fun delete(id: Long)
}