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

An electoral list submitted by a party (or independent group) for a specific constituency. In Finnish elections this is the *ehdokaslista*.

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

## Voter

A person eligible to vote. Voter identity is kept strictly separate from Vote records to preserve ballot secrecy.

| Field | Type | Notes |
|---|---|---|
| id | UUID | Internal identifier only |
| nationalId | string | Encrypted; used only for authentication and eligibility |
| constituencyId | UUID | Assigned based on residence |
| hasVoted | boolean | Set true when vote is cast; no link to actual vote |

**Privacy constraint:** No join between Voter and Vote must ever be persisted or queryable.

---

## Vote

An anonymous record of a single vote cast. Contains no voter-identifying information.

| Field | Type | Notes |
|---|---|---|
| id | UUID | |
| electionId | UUID | |
| constituencyId | UUID | |
| candidateId | UUID | The chosen candidate |
| castedAt | datetime | Timestamp |

---

## Result

Aggregated results for an election, scoped to a constituency. Computed after voting closes.

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

Vote tally for a single candidate within a constituency result.

| Field | Type | Notes |
|---|---|---|
| resultId | UUID | Parent Result |
| candidateId | UUID | |
| voteCount | int | Raw votes received |
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
 ├── Constituency[]
 │    ├── CandidateList[]  (per party)
 │    │    └── Candidate[]
 │    ├── Vote[]           (anonymous, no voter link)
 │    └── Result
 │         ├── CandidateResult[]
 │         ├── CandidateListResult[]
 │         └── SeatAllocation[]
 └── (Party — referenced by CandidateList)

Voter  ──→  authenticates  ──→  marks hasVoted = true
            (no persistent link to Vote)
```

---

## Design Notes

- **Ballot secrecy**: `Voter` and `Vote` are intentionally unlinked after the vote is recorded. The system must never expose a mapping between a voter's identity and their ballot.
- **Modularity**: Each aggregate (Election, CandidateList, Result) is independently addressable via the REST API and can evolve separately.
- **Seat allocation algorithm**: Encapsulated behind the Result computation step; the algorithm (D'Hondt, Sainte-Laguë, etc.) is election-type specific and should be pluggable.
- **Status machines**: Election, CandidateList, and Result each have explicit status enums to control which operations are permitted at each lifecycle stage.
