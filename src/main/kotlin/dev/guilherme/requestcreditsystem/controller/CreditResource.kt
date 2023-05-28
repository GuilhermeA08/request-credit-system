package dev.guilherme.requestcreditsystem.controller

import dev.guilherme.requestcreditsystem.dto.CreditDto
import dev.guilherme.requestcreditsystem.dto.CreditView
import dev.guilherme.requestcreditsystem.dto.CreditViewList
import dev.guilherme.requestcreditsystem.entity.Credit
import dev.guilherme.requestcreditsystem.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {
    @PostMapping
    fun saveCredit(@RequestBody creditDto: CreditDto): ResponseEntity<String> {
        println(creditDto)
        val credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(
            "Credit saved with success! Credit code: ${credit.creditCode} - Customer id: ${credit.customer?.firstName}"
        )
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditViewList>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId).stream()
            .map{ credit : Credit -> CreditViewList(credit)}.collect(Collectors.toList())

        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditView> {
        val credit = this.creditService.findByCreditCode(creditCode, customerId)
        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }
}