# Use Cases — Electoral Information System

## Actors

| Actor | Description |
|---|---|
| **Election Official** | Configures and administers elections, manages candidates and lists |
| **Voter** | Casts a vote in an election |
| **Candidate** | Stands for election; may view own results |
| **Party Administrator** | Manages party information and candidate lists |
| **Observer** | Read-only access to published results and statistics |
| **System Administrator** | Manages system configuration, users, and access rights |

---

## UC-01: Set Up an Election

**Actor:** Election Official  
**Goal:** Create and configure a new election event.

**Main Flow:**
1. Official creates an election with name, type, and date range.
2. Official defines constituencies/districts.
3. Official sets voting rules (e.g., proportional, single-member).
4. Official publishes the election so registration can begin.

**Variations:**
- Municipal election (multi-seat, proportional by list)
- Parliamentary election (multi-seat, open-list proportional)
- Presidential election (single-seat, two-round)
- EU Parliament election

---

## UC-02: Register a Party

**Actor:** Party Administrator / Election Official  
**Goal:** Add a registered political party to the election.

**Main Flow:**
1. Administrator submits party name, abbreviation, and registration details.
2. Election Official approves the party registration.
3. Party becomes available for candidate list association.

---

## UC-03: Submit a Candidate List

**Actor:** Party Administrator  
**Goal:** Register a candidate list (electoral list) for a constituency.

**Main Flow:**
1. Party Administrator selects election and constituency.
2. Administrator adds candidates in ranked order.
3. System validates list (minimum/maximum candidates, eligibility).
4. Election Official approves or rejects the list.
5. Approved list is published.

---

## UC-04: Register a Candidate

**Actor:** Party Administrator / Election Official  
**Goal:** Add an individual candidate to a candidate list.

**Main Flow:**
1. Administrator provides candidate details (name, number, party affiliation).
2. System assigns a candidate number within the list.
3. Candidate is associated with a constituency and list.
4. Candidate details are published once the list is approved.

---

## UC-05: Cast a Vote

**Actor:** Voter  
**Goal:** Vote for a candidate or party list in an election.

**Main Flow:**
1. Voter authenticates (strong identification).
2. System presents the ballot for the voter's constituency.
3. Voter selects a candidate (or list, depending on election type).
4. System confirms the vote and records it anonymously.
5. Voter receives confirmation; vote is irreversible.

**Preconditions:** Voter is eligible and has not yet voted in this election.  
**Postconditions:** Vote is recorded; voter is marked as having voted.

---

## UC-06: View Candidate Information

**Actor:** Observer, Voter  
**Goal:** Browse candidates and their details before or during an election.

**Main Flow:**
1. User selects an election and optionally a constituency.
2. System displays candidate lists with candidates and their details.
3. User can search or filter by party, name, or constituency.

---

## UC-07: Close Voting and Tally Results

**Actor:** Election Official  
**Goal:** Close an election and compute preliminary results.

**Main Flow:**
1. Election Official closes the voting period.
2. System aggregates votes per candidate and per list.
3. System applies seat allocation algorithm (e.g., D'Hondt).
4. Preliminary results are computed and stored.
5. Results are published to observers.

---

## UC-08: Publish Official Results

**Actor:** Election Official  
**Goal:** Publish final, certified election results.

**Main Flow:**
1. Official reviews and approves tallied results.
2. System marks results as official.
3. Seat assignments per constituency are finalised.
4. Results are made publicly accessible via API and UI.

---

## UC-09: View Election Results

**Actor:** Observer, Candidate, Voter  
**Goal:** View results at various levels of aggregation.

**Main Flow:**
1. User selects an election.
2. User views results by: overall, constituency, party, or candidate.
3. System shows vote counts, percentages, and seat allocations.
4. User can compare results across constituencies or elections.

**Variations:**
- Live results during counting (preliminary)
- Final official results
- Historical results from past elections

---

## UC-10: Export Results and Statistics

**Actor:** Observer, Election Official  
**Goal:** Download election data for analysis or reporting.

**Main Flow:**
1. User selects an election and desired scope.
2. System generates export (JSON, CSV) with votes, results, and metadata.
3. User downloads the file.

---

## UC-11: Manage Users and Roles

**Actor:** System Administrator  
**Goal:** Create accounts and assign roles to election officials and party administrators.

**Main Flow:**
1. Administrator creates a user account.
2. Administrator assigns one or more roles (Election Official, Party Administrator, Observer).
3. User can log in and perform actions permitted by their role.

---

## UC-12: Validate Voter Eligibility

**Actor:** System (automated)  
**Goal:** Confirm a voter is eligible to vote in a given election and constituency.

**Main Flow:**
1. Voter attempts to vote.
2. System checks voter's registered constituency, age, and citizenship.
3. System confirms eligibility or rejects with a reason.
4. System checks that the voter has not already voted.

---

## Summary Matrix

| Use Case | Election Official | Voter | Party Admin | Observer | Sys Admin |
|---|:---:|:---:|:---:|:---:|:---:|
| UC-01 Set Up Election | ✓ | | | | |
| UC-02 Register Party | ✓ | | ✓ | | |
| UC-03 Submit Candidate List | ✓ | | ✓ | | |
| UC-04 Register Candidate | ✓ | | ✓ | | |
| UC-05 Cast Vote | | ✓ | | | |
| UC-06 View Candidates | ✓ | ✓ | ✓ | ✓ | |
| UC-07 Tally Results | ✓ | | | | |
| UC-08 Publish Results | ✓ | | | | |
| UC-09 View Results | ✓ | ✓ | ✓ | ✓ | |
| UC-10 Export Results | ✓ | | | ✓ | |
| UC-11 Manage Users | | | | | ✓ |
| UC-12 Validate Eligibility | ✓ (system) | | | | |
