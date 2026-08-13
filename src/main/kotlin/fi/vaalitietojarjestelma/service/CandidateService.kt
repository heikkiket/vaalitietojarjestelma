package fi.vaalitietojarjestelma.service

import fi.vaalitietojarjestelma.domain.Candidate
import fi.vaalitietojarjestelma.repository.CandidateRepository
import org.springframework.stereotype.Service

@Service
class CandidateService(private val candidateRepository: CandidateRepository) {

    fun registerCandidate(name: String): Candidate = candidateRepository.save(name)

    fun listCandidates(): List<Candidate> = candidateRepository.findAll()

    fun isRegistered(name: String): Boolean = candidateRepository.findByName(name) != null
}
