package fi.vaalitietojarjestelma.service

import fi.vaalitietojarjestelma.domain.CandidateVoteCount
import fi.vaalitietojarjestelma.domain.PollingStation
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class BallotTallyService {

    private val pollingStations = ConcurrentHashMap<UUID, PollingStation>()
    private val registeredCandidates = ConcurrentHashMap.newKeySet<String>()
    private var enteredVoteCounts: List<CandidateVoteCount> = emptyList()

    fun registerPollingStation(name: String): PollingStation {
        val station = PollingStation(id = UUID.randomUUID(), name = name)
        pollingStations[station.id] = station
        return station
    }

    fun registerCandidate(candidateName: String) {
        registeredCandidates.add(candidateName)
    }

    fun enterBallotCount(pollingStationId: UUID, candidateVotes: List<CandidateVoteCount>): List<CandidateVoteCount> {
        require(pollingStations.containsKey(pollingStationId)) { "Polling station not found: $pollingStationId" }
        candidateVotes.forEach { vote ->
            require(vote.candidate in registeredCandidates) { "Candidate not registered: ${vote.candidate}" }
        }
        enteredVoteCounts = candidateVotes
        return candidateVotes
    }

    fun individualVoteCount(candidateName: String): Int {
        return enteredVoteCounts.firstOrNull { it.candidate == candidateName }?.votes
            ?: throw NoSuchElementException("No vote count recorded for candidate: $candidateName")
    }
}
