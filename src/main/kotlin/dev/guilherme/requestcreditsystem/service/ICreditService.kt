package dev.guilherme.requestcreditsystem.service

import dev.guilherme.requestcreditsystem.entity.Credit
import java.util.UUID

interface ICreditService {

    fun save(credit: Credit): Credit
    fun findAllByCustomer(customerId: Long): List<Credit>
    fun findByCreditCode(creditCode: UUID, customerId: Long): Credit

}