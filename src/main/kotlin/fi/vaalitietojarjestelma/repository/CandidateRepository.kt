package fi.vaalitietojarjestelma.repository

import fi.vaalitietojarjestelma.domain.Candidate

interface CandidateRepository {
    fun save(name: String): Candidate
    fun findAll(): List<Candidate>
    fun findByName(name: String): Candidate?
}
