package fi.vaalitietojarjestelma.behavior

import io.cucumber.datatable.DataTable
import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class SubmitBallotTallySteps {

    @Given("the polling station {string} has closed")
    fun thePollingStationHasClosed(stationName: String) {
        throw PendingException()
    }

    @When("the chairperson enters the following ballot count")
    fun theChairpersonEntersTheFollowingBallotCount(ballotCount: DataTable) {
        throw PendingException()
    }

    @When("the chairperson reviews and confirms the tally")
    fun theChairpersonReviewsAndConfirmsTheTally() {
        throw PendingException()
    }

    @Then("the tally is recorded with status {string}")
    fun theTallyIsRecordedWithStatus(status: String) {
        throw PendingException()
    }

    @Then("the election official is notified to review the tally")
    fun theElectionOfficialIsNotifiedToReviewTheTally() {
        throw PendingException()
    }
}
