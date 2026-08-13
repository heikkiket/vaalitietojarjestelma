package fi.vaalitietojarjestelma.behavior

import fi.vaalitietojarjestelma.domain.CandidateVoteCount
import fi.vaalitietojarjestelma.service.BallotTallyService
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import java.util.UUID

class SubmitBallotTallySteps {

    private val service = BallotTallyService()

    private lateinit var pollingStationId: UUID

    @Given("there is polling station {string}")
    fun thereIsPollingStation(stationName: String) {
        pollingStationId = service.registerPollingStation(stationName).id
    }

    @Given("^there is candidate called (.+)$")
    fun thereIsCandidateCalled(candidateName: String) {
        service.registerCandidate(candidateName)
    }

    @When("the chairperson enters the following ballot count")
    fun theChairpersonEntersTheFollowingBallotCount(ballotCount: DataTable) {
        val candidateVotes = ballotCount.asMaps(String::class.java, String::class.java).map { row ->
            CandidateVoteCount(candidate = row.getValue("candidate"), votes = row.getValue("votes").toInt())
        }
        service.enterBallotCount(pollingStationId, candidateVotes)
    }

    @Then("^the individual vote count of (.+) is (\\d+)$")
    fun theIndividualVoteCountOfCandidateIs(candidateName: String, votes: Int) {
        service.individualVoteCount(candidateName) shouldBe votes
    }
}
