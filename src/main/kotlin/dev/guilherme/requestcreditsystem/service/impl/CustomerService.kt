package dev.guilherme.requestcreditsystem.service.impl

import dev.guilherme.requestcreditsystem.entity.Customer
import dev.guilherme.requestcreditsystem.exception.BusinessException
import dev.guilherme.requestcreditsystem.repository.CustomerRepository
import dev.guilherme.requestcreditsystem.service.ICustomerService
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) : ICustomerService {
    override fun save(customer: Customer): Customer =
        this.customerRepository.save(customer)


    override fun findById(id: Long): Customer =
        this.customerRepository.findById(id).orElseThrow {
            throw BusinessException("Id $id not found")
        }

    override fun delete(id: Long) {
        val customer = this.findById(id)

        this.customerRepository.delete(customer)
    }
}