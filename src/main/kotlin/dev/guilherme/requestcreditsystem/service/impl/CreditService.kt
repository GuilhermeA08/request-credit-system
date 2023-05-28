package dev.guilherme.requestcreditsystem.service.impl

import dev.guilherme.requestcreditsystem.entity.Credit
import dev.guilherme.requestcreditsystem.exception.BusinessException
import dev.guilherme.requestcreditsystem.repository.CreditRepository
import dev.guilherme.requestcreditsystem.service.ICreditService
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> =
        this.creditRepository.findAllByCustomerId(customerId)

    override fun findByCreditCode(creditCode: UUID, customerId: Long): Credit {
        val credit: Credit = this.creditRepository.findByCreditCode(creditCode) ?:
        throw BusinessException("Credit $creditCode not found")

        return  if (credit.customer?.id == customerId) credit
                else throw IllegalArgumentException("Contact Admin")
    }

}