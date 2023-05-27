package dev.guilherme.requestcreditsystem.repository

import dev.guilherme.requestcreditsystem.entity.Credit
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditRepository: JpaRepository<Credit, Long> {

    fun findByCreditCode(creditCode: UUID): Credit?

    @Query (value =  "SELECT * FROM Credit WHERE CUSTOMER_ID = :customerId", nativeQuery = true)
    fun findAllByCustomerId(customerId: Long): List<Credit>
}