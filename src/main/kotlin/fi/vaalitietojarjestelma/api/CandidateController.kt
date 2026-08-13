package fi.vaalitietojarjestelma.api

import fi.vaalitietojarjestelma.service.CandidateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/candidates")
class CandidateController(private val candidateService: CandidateService) {

    @GetMapping
    fun candidates(): List<String> = candidateService.listCandidates().map { it.name }
}
