package fi.vaalitietojarjestelma.api

import fi.vaalitietojarjestelma.domain.BallotTallyStatus
import fi.vaalitietojarjestelma.domain.CandidateVoteCount
import fi.vaalitietojarjestelma.service.BallotTallyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class EnterBallotCountRequest(
    val candidateVotes: List<CandidateVoteCount>,
)

data class TallyStatusResponse(
    val status: BallotTallyStatus,
)

private val MOCK_CANDIDATES = listOf("Maria Virtanen", "Jukka Korhonen")

@RestController
@RequestMapping("/api/ballot-tally")
class BallotTallyController(private val ballotTallyService: BallotTallyService) {

    @PostMapping("/enter")
    fun enter(@RequestBody request: EnterBallotCountRequest): List<CandidateVoteCount> =
        ballotTallyService.enterBallotCount(request.candidateVotes)

    @GetMapping("/votes/{candidate}")
    fun votesFor(@PathVariable candidate: String): CandidateVoteCount =
        CandidateVoteCount(candidate = candidate, votes = ballotTallyService.individualVoteCount(candidate))

    @PostMapping("/submit")
    fun submit(): TallyStatusResponse = TallyStatusResponse(ballotTallyService.submitTally())

    @GetMapping("/status")
    fun status(): TallyStatusResponse = TallyStatusResponse(ballotTallyService.tallyStatus())

    @GetMapping("/candidates")
    fun candidates(): List<String> = MOCK_CANDIDATES

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(): ResponseEntity<Unit> = ResponseEntity.badRequest().build()

    @ExceptionHandler(IllegalStateException::class)
    fun handleConflict(): ResponseEntity<Unit> = ResponseEntity.status(HttpStatus.CONFLICT).build()

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(): ResponseEntity<Unit> = ResponseEntity.notFound().build()
}
