package fi.vaalitietojarjestelma.service

import fi.vaalitietojarjestelma.domain.ElectionStatus
import fi.vaalitietojarjestelma.domain.ElectionType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime
import java.util.UUID

class ElectionServiceTest : BehaviorSpec() {

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    private val service = ElectionService()
    private val votingStarts = LocalDateTime.of(2027, 4, 2, 8, 0)
    private val votingEnds = LocalDateTime.of(2027, 4, 2, 20, 0)

    init {
        given("a valid election request") {
            `when`("creating the election") {
                val election = service.createElection("2027 Parliamentary Election", ElectionType.PARLIAMENTARY, votingStarts, votingEnds)

                then("it is created with DRAFT status and an assigned id") {
                    election.id shouldNotBe null
                    election.name shouldBe "2027 Parliamentary Election"
                    election.type shouldBe ElectionType.PARLIAMENTARY
                    election.status shouldBe ElectionStatus.DRAFT
                    election.constituencies.shouldBeEmpty()
                }
            }
        }

        given("a request with voting end before voting start") {
            `when`("creating the election") {
                then("it throws IllegalArgumentException") {
                    shouldThrow<IllegalArgumentException> {
                        service.createElection("Bad Election", ElectionType.MUNICIPAL, votingEnds, votingStarts)
                    }
                }
            }
        }

        given("a DRAFT election with no constituencies") {
            val election = service.createElection("2027 Municipal Election", ElectionType.MUNICIPAL, votingStarts, votingEnds)

            `when`("adding a constituency") {
                val constituency = service.addConstituency(election.id, "Helsinki", "HKI", 85)

                then("the constituency is created and linked to the election") {
                    constituency.id shouldNotBe null
                    constituency.name shouldBe "Helsinki"
                    constituency.electionId shouldBe election.id
                    constituency.seatsAvailable shouldBe 85
                }
            }

            `when`("publishing") {
                then("it throws IllegalStateException") {
                    shouldThrow<IllegalStateException> {
                        service.publishElection(election.id)
                    }
                }
            }
        }

        given("a DRAFT election with constituencies") {
            val election = service.createElection("2027 Parliamentary Election", ElectionType.PARLIAMENTARY, votingStarts, votingEnds)
            service.addConstituency(election.id, "Helsinki", "HKI", 35)
            service.addConstituency(election.id, "Uusimaa", "UUS", 10)

            `when`("publishing") {
                val published = service.publishElection(election.id)

                then("the status becomes REGISTRATION_OPEN") {
                    published.status shouldBe ElectionStatus.REGISTRATION_OPEN
                }
            }
        }

        given("an already published election") {
            val election = service.createElection("2027 EU Election", ElectionType.EU_PARLIAMENT, votingStarts, votingEnds)
            service.addConstituency(election.id, "Finland", "FIN", 15)
            service.publishElection(election.id)

            `when`("publishing again") {
                then("it throws IllegalStateException") {
                    shouldThrow<IllegalStateException> {
                        service.publishElection(election.id)
                    }
                }
            }
        }

        given("a non-existent election id") {
            val unknownId = UUID.randomUUID()

            `when`("adding a constituency") {
                then("it throws NoSuchElementException") {
                    shouldThrow<NoSuchElementException> {
                        service.addConstituency(unknownId, "Helsinki", "HKI", 10)
                    }
                }
            }

            `when`("publishing") {
                then("it throws NoSuchElementException") {
                    shouldThrow<NoSuchElementException> {
                        service.publishElection(unknownId)
                    }
                }
            }
        }
    }
}
