package fi.vaalitietojarjestelma.api

import fi.vaalitietojarjestelma.domain.Constituency
import fi.vaalitietojarjestelma.domain.Election
import fi.vaalitietojarjestelma.domain.ElectionType
import fi.vaalitietojarjestelma.service.ElectionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.UUID

data class CreateElectionRequest(
    val name: String,
    val type: ElectionType,
    val votingStarts: LocalDateTime,
    val votingEnds: LocalDateTime,
)

data class AddConstituencyRequest(
    val name: String,
    val code: String,
    val seatsAvailable: Int,
)

@RestController
@RequestMapping("/api/elections")
class ElectionController(private val electionService: ElectionService) {

    @PostMapping
    fun create(@RequestBody request: CreateElectionRequest): ResponseEntity<Election> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            electionService.createElection(request.name, request.type, request.votingStarts, request.votingEnds)
        )

    @PostMapping("/{id}/constituencies")
    fun addConstituency(
        @PathVariable id: UUID,
        @RequestBody request: AddConstituencyRequest,
    ): ResponseEntity<Constituency> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            electionService.addConstituency(id, request.name, request.code, request.seatsAvailable)
        )

    @PostMapping("/{id}/publish")
    fun publish(@PathVariable id: UUID): ResponseEntity<Election> =
        ResponseEntity.ok(electionService.publishElection(id))

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(): ResponseEntity<Unit> = ResponseEntity.badRequest().build()

    @ExceptionHandler(IllegalStateException::class)
    fun handleConflict(): ResponseEntity<Unit> = ResponseEntity.status(HttpStatus.CONFLICT).build()

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(): ResponseEntity<Unit> = ResponseEntity.notFound().build()
}
