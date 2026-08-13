package fi.vaalitietojarjestelma.service

import fi.vaalitietojarjestelma.domain.BallotTallyStatus
import fi.vaalitietojarjestelma.domain.CandidateVoteCount
import fi.vaalitietojarjestelma.domain.PollingStation
import fi.vaalitietojarjestelma.repository.CandidateRepository
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class BallotTallyService(private val candidateRepository: CandidateRepository) {

    private val pollingStations = ConcurrentHashMap<UUID, PollingStation>()
    private var enteredVoteCounts: List<CandidateVoteCount> = emptyList()
    private var tallyStatus = BallotTallyStatus.DRAFT

    private val defaultPollingStationId: UUID by lazy { registerPollingStation("Default").id }

    fun registerPollingStation(name: String): PollingStation {
        val station = PollingStation(id = UUID.randomUUID(), name = name)
        pollingStations[station.id] = station
        return station
    }

    fun registerCandidate(candidateName: String) {
        candidateRepository.save(candidateName)
    }

    fun enterBallotCount(pollingStationId: UUID, candidateVotes: List<CandidateVoteCount>): List<CandidateVoteCount> {
        require(pollingStations.containsKey(pollingStationId)) { "Polling station not found: $pollingStationId" }
        candidateVotes.forEach { vote ->
            require(candidateRepository.findByName(vote.candidate) != null) { "Candidate not registered: ${vote.candidate}" }
        }
        enteredVoteCounts = candidateVotes
        return candidateVotes
    }

    fun enterBallotCount(candidateVotes: List<CandidateVoteCount>): List<CandidateVoteCount> =
        enterBallotCount(defaultPollingStationId, candidateVotes)

    fun individualVoteCount(candidateName: String): Int {
        return enteredVoteCounts.firstOrNull { it.candidate == candidateName }?.votes
            ?: throw NoSuchElementException("No vote count recorded for candidate: $candidateName")
    }

    fun submitTally(): BallotTallyStatus {
        tallyStatus = BallotTallyStatus.SUBMITTED
        return tallyStatus
    }

    fun tallyStatus(): BallotTallyStatus = tallyStatus
}
