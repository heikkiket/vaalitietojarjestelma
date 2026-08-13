package fi.vaalitietojarjestelma.repository

import fi.vaalitietojarjestelma.domain.Candidate
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class InMemoryCandidateRepository : CandidateRepository {

    private val candidates = ConcurrentHashMap<Long, Candidate>()
    private val nextId = AtomicLong(1)

    init {
        save("Maria Virtanen")
        save("Jukka Korhonen")
    }

    override fun save(name: String): Candidate {
        val candidate = Candidate(id = nextId.getAndIncrement(), name = name)
        candidates[candidate.id] = candidate
        return candidate
    }

    override fun findAll(): List<Candidate> = candidates.values.toList()

    override fun findByName(name: String): Candidate? = candidates.values.firstOrNull { it.name == name }
}
