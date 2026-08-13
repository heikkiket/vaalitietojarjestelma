package fi.vaalitietojarjestelma.domain

import java.util.UUID

data class PollingStation(
    val id: UUID,
    val name: String,
)

data class CandidateVoteCount(
    val candidate: String,
    val votes: Int,
)

enum class BallotTallyStatus { DRAFT, SUBMITTED }
