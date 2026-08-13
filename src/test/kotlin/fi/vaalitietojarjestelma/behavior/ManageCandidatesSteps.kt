package fi.vaalitietojarjestelma.behavior

import fi.vaalitietojarjestelma.repository.InMemoryCandidateRepository
import fi.vaalitietojarjestelma.service.CandidateService
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContain

class ManageCandidatesSteps {

    private val service = CandidateService(InMemoryCandidateRepository())

    private var registeredCandidates: List<String> = emptyList()

    @When("^the administrator registers a candidate called (.+)$")
    fun theAdministratorRegistersACandidateCalled(candidateName: String) {
        service.registerCandidate(candidateName)
    }

    @Then("^(.+) is a registered candidate$")
    fun candidateIsARegisteredCandidate(candidateName: String) {
        service.isRegistered(candidateName) shouldBe true
    }

    @Given("there are following registered candidates")
    fun thereAreFollowingRegisteredCandidates(candidates: DataTable) {
        candidates.asMaps(String::class.java, String::class.java).forEach { row ->
            service.registerCandidate(row.getValue("candidate"))
        }
    }

    @When("the administrator lists the registered candidates")
    fun theAdministratorListsTheRegisteredCandidates() {
        registeredCandidates = service.listCandidates().map { it.name }
    }

    @Then("^the list of registered candidates includes (.+)$")
    fun theListOfRegisteredCandidatesIncludes(candidateName: String) {
        registeredCandidates shouldContain candidateName
    }
}
