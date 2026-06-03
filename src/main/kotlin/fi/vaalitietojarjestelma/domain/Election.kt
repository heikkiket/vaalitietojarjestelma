package fi.vaalitietojarjestelma.domain

import java.time.LocalDateTime
import java.util.UUID

enum class ElectionType { PARLIAMENTARY, MUNICIPAL, PRESIDENTIAL, EU_PARLIAMENT, OTHER }

enum class ElectionStatus { DRAFT, REGISTRATION_OPEN, VOTING_OPEN, COUNTING, RESULTS_PRELIMINARY, RESULTS_FINAL }

data class Constituency(
    val id: UUID,
    val electionId: UUID,
    val name: String,
    val code: String,
    val seatsAvailable: Int,
)

data class Election(
    val id: UUID,
    val name: String,
    val type: ElectionType,
    val status: ElectionStatus,
    val votingStarts: LocalDateTime,
    val votingEnds: LocalDateTime,
    val constituencies: List<Constituency>,
)
