package fi.vaalitietojarjestelma.service

import fi.vaalitietojarjestelma.domain.Constituency
import fi.vaalitietojarjestelma.domain.Election
import fi.vaalitietojarjestelma.domain.ElectionStatus
import fi.vaalitietojarjestelma.domain.ElectionType
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class ElectionService {

    private val store = ConcurrentHashMap<UUID, Election>()

    fun createElection(
        name: String,
        type: ElectionType,
        votingStarts: LocalDateTime,
        votingEnds: LocalDateTime,
    ): Election {
        require(votingEnds.isAfter(votingStarts)) { "Voting end must be after voting start" }
        val election = Election(
            id = UUID.randomUUID(),
            name = name,
            type = type,
            status = ElectionStatus.DRAFT,
            votingStarts = votingStarts,
            votingEnds = votingEnds,
            constituencies = emptyList(),
        )
        store[election.id] = election
        return election
    }

    fun addConstituency(electionId: UUID, name: String, code: String, seatsAvailable: Int): Constituency {
        val election = store[electionId] ?: throw NoSuchElementException("Election not found: $electionId")
        val constituency = Constituency(
            id = UUID.randomUUID(),
            electionId = electionId,
            name = name,
            code = code,
            seatsAvailable = seatsAvailable,
        )
        store[electionId] = election.copy(constituencies = election.constituencies + constituency)
        return constituency
    }

    fun publishElection(electionId: UUID): Election {
        val election = store[electionId] ?: throw NoSuchElementException("Election not found: $electionId")
        check(election.status == ElectionStatus.DRAFT) { "Only DRAFT elections can be published" }
        check(election.constituencies.isNotEmpty()) { "Election must have at least one constituency before publishing" }
        val published = election.copy(status = ElectionStatus.REGISTRATION_OPEN)
        store[electionId] = published
        return published
    }
}
