# Core Domain Types — Electoral Information System

Domain types derived from [UseCases.md](UseCases.md).

---

## Election

The top-level entity representing a single election event.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| name | string | Official name, e.g. "2027 Parliamentary Election" |
| type | ElectionType | See enum below |
| status | ElectionStatus | See enum below |
| votingStarts | datetime | When polls open |
| votingEnds | datetime | When polls close |
| constituencies | Constituency[] | Districts in this election |

**ElectionType:** `PARLIAMENTARY | MUNICIPAL | PRESIDENTIAL | EU_PARLIAMENT | OTHER`

**ElectionStatus:** `DRAFT | REGISTRATION_OPEN | VOTING_OPEN | COUNTING | RESULTS_PRELIMINARY | RESULTS_FINAL`

---

## Constituency

A geographic or administrative voting district within an election.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| electionId | UUID | Parent election |
| name | string | e.g. "Helsinki constituency" |
| code | string | Official code |
| seatsAvailable | int | Number of seats to be filled |
| pollingDistricts | PollingDistrict[] | Polling districts within this constituency |

---

## PollingDistrict

A subdivision of a constituency with its own polling station and board.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| constituencyId | UUID | Parent constituency |
| name | string | e.g. "Kallio polling district" |
| code | string | Official code |
| pollingStationAddress | string | Physical address of the polling station |

---

## PollingDistrictBoard

The board (vaalilautakunta) responsible for overseeing a polling station and counting paper ballots.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| electionId | UUID | |
| pollingDistrictId | UUID | The polling district this board serves |
| members | PollWorker[] | All assigned board members |
| status | BoardStatus | See enum below |

**BoardStatus:** `ASSIGNED | ACTIVE | TALLY_SUBMITTED | COMPLETED`

---

## PollWorker

An individual poll worker (vaalivirkailija) assigned to a polling district board.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| boardId | UUID | The PollingDistrictBoard this worker belongs to |
| firstName | string | |
| lastName | string | |
| role | PollWorkerRole | See enum below |

**PollWorkerRole:** `CHAIRPERSON | SECRETARY | MEMBER | SUBSTITUTE`

---

## CentralElectoralCommission

The central oversight body (keskusvaalilautakunta) responsible for supervising the election process and certifying final results.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| electionId | UUID | |
| name | string | Official name of the commission |
| jurisdiction | string | e.g. "National" or the municipality name |
| members | CommissionMember[] | |

---

## CommissionMember

A member of the Central Electoral Commission.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| commissionId | UUID | |
| firstName | string | |
| lastName | string | |
| role | CommissionMemberRole | See enum below |
| appointedAt | date | |

**CommissionMemberRole:** `CHAIRPERSON | DEPUTY_CHAIRPERSON | MEMBER | SUBSTITUTE`

---

## Party

A registered political party that can sponsor candidate lists.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| name | string | Full official name |
| abbreviation | string | e.g. "SDP", "KOK" |
| registrationNumber | string | Official registration ID |
| status | PartyStatus | `ACTIVE | INACTIVE` |

---

## CandidateList

An electoral list submitted by a party (or independent group) for a specific constituency (ehdokaslista in Finnish elections).

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| electionId | UUID | |
| constituencyId | UUID | |
| partyId | UUID | nullable for independent lists |
| name | string | Display name of the list |
| status | ListStatus | See enum below |
| candidates | Candidate[] | Ordered list of candidates |

**ListStatus:** `SUBMITTED | APPROVED | REJECTED`

---

## Candidate

An individual standing for election on a candidate list.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| listId | UUID | The CandidateList this candidate belongs to |
| candidateNumber | int | Official number on the ballot |
| firstName | string | |
| lastName | string | |
| dateOfBirth | date | Used for eligibility checks |
| municipality | string | Home municipality |
| occupation | string | Optional; shown on ballot |
| biography | string | Optional public profile |

---

## BoardTally

The ballot count submitted by a polling district board after physically counting paper ballots. Serves as the authoritative input for constituency-level results.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| electionId | UUID | |
| pollingDistrictId | UUID | |
| boardId | UUID | The PollingDistrictBoard that submitted this tally |
| status | BoardTallyStatus | See enum below |
| submittedAt | datetime | When the board submitted the tally |
| approvedAt | datetime | nullable; when an official approved the tally |
| totalBallotsCounted | int | Total number of physical ballots counted |
| invalidBallots | int | Number of blank or invalid ballots |
| candidateTallies | CandidateTallyEntry[] | Per-candidate ballot counts |

**BoardTallyStatus:** `DRAFT | SUBMITTED | APPROVED | REJECTED`

---

## CandidateTallyEntry

The count of physical ballots for a specific candidate within a board tally.

| Field | Type | Notes |
|---|---|---|
| tallyId | UUID | Parent BoardTally |
| candidateId | UUID | |
| ballotCount | int | Number of physical ballots counted for this candidate |

---

## Result

Aggregated results for an election, scoped to a constituency. Computed from approved BoardTallies after voting closes.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| electionId | UUID | |
| constituencyId | UUID | |
| status | ResultStatus | `PRELIMINARY | OFFICIAL` |
| publishedAt | datetime | |
| candidateResults | CandidateResult[] | |
| listResults | CandidateListResult[] | |
| seatAllocations | SeatAllocation[] | |

**ResultStatus:** `PRELIMINARY | OFFICIAL`

---

## CandidateResult

Vote tally for a single candidate within a constituency result, aggregated from approved board tallies.

| Field | Type | Notes |
|---|---|---|
| resultId | UUID | Parent Result |
| candidateId | UUID | |
| voteCount | int | Total ballots received across all polling districts |
| voteShare | decimal | Percentage of valid votes |
| comparativeNumber | decimal | D'Hondt or other allocation figure |
| elected | boolean | Whether this candidate won a seat |

---

## CandidateListResult

Aggregated vote tally for an entire candidate list within a result.

| Field | Type | Notes |
|---|---|---|
| resultId | UUID | Parent Result |
| listId | UUID | |
| totalVotes | int | Sum of all candidate votes on this list |
| voteShare | decimal | Percentage of valid votes |
| seatsWon | int | Seats allocated to this list |

---

## SeatAllocation

Records which candidate fills which seat after the allocation algorithm runs.

| Field | Type | Notes |
|---|---|---|
| resultId | UUID | |
| candidateId | UUID | |
| seatNumber | int | Seat identifier within the constituency |
| allocationRound | int | Round in which seat was assigned |

---

## Type Relationship Overview

```
Election
 ├── CentralElectoralCommission
 │    └── CommissionMember[]
 ├── Constituency[]
 │    ├── PollingDistrict[]
 │    │    └── PollingDistrictBoard  (vaalilautakunta)
 │    │         ├── PollWorker[]     (vaalivirkailijat)
 │    │         └── BoardTally
 │    │              └── CandidateTallyEntry[]
 │    ├── CandidateList[]  (per party)
 │    │    └── Candidate[]
 │    └── Result  ←── aggregated from approved BoardTallies
 │         ├── CandidateResult[]
 │         ├── CandidateListResult[]
 │         └── SeatAllocation[]
 └── (Party — referenced by CandidateList)
```

---

## Design Notes

- **Paper ballots only**: Voting is conducted exclusively on physical paper ballots. No electronic voting is supported. BoardTallies are the sole authoritative source for vote counts.
- **Counting chain**: Each PollingDistrictBoard counts its own ballots and submits a BoardTally. Constituency Results are aggregated from approved BoardTallies only.
- **Modularity**: Each aggregate (Election, CandidateList, BoardTally, Result) is independently addressable via the REST API and can evolve separately.
- **Seat allocation algorithm**: Encapsulated behind the Result computation step; the algorithm (D'Hondt, Sainte-Laguë, etc.) is election-type specific and should be pluggable.
- **Status machines**: Election, CandidateList, BoardTally, and Result each have explicit status enums to control which operations are permitted at each lifecycle stage.
- **Central Electoral Commission**: Supervises the counting process and certifies final results; commission members are recorded to support audit trails.
