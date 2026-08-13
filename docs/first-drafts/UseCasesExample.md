# Use Cases — Electoral Information System

## Actors

| Actor | Description |
|---|---|
| **Election Official** | Configures and administers elections, manages candidates and lists |
| **Candidate** | Stands for election; may view own results |
| **Party Administrator** | Manages party information and candidate lists |
| **Poll Worker** | Member of a polling district board; participates in counting paper ballots and submitting tallies |
| **Central Electoral Commission** | Oversees the election process, reviews ballot tallies, and certifies final results |
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

## UC-05: Manage Polling Districts and Boards

**Actor:** Election Official  
**Goal:** Set up polling districts and assign polling district boards for an election.

**Main Flow:**
1. Official defines polling districts within each constituency, including polling station addresses.
2. Official creates a polling district board for each district.
3. Official assigns poll workers to each board and designates roles (chairperson, secretary, members, substitutes).
4. The system registers the assignments and notifies poll workers.

---

## UC-06: View Candidate Information

**Actor:** Observer, Candidate, Election Official, Party Administrator  
**Goal:** Browse candidates and their details before or during an election.

**Main Flow:**
1. User selects an election and optionally a constituency.
2. System displays candidate lists with candidates and their details.
3. User can search or filter by party, name, or constituency.

---

## UC-07: Close Voting and Initiate Counting

**Actor:** Election Official  
**Goal:** Close the polling period and initiate the paper ballot counting process.

**Main Flow:**
1. Election Official closes the voting period.
2. System transitions election status to COUNTING.
3. Polling district boards are notified to begin counting their paper ballots.
4. Boards submit their tallies (see UC-12).
5. Once all tallies are approved, the system aggregates votes per candidate and per list.
6. System applies the seat allocation algorithm (e.g., D'Hondt).
7. Preliminary results are computed and stored.
8. Results are published to observers.

---

## UC-08: Publish Official Results

**Actor:** Election Official / Central Electoral Commission  
**Goal:** Publish final, certified election results.

**Main Flow:**
1. Central Electoral Commission reviews and approves the tallied results.
2. System marks results as official.
3. Seat assignments per constituency are finalised.
4. Results are made publicly accessible via API and UI.

---

## UC-09: View Election Results

**Actor:** Observer, Candidate, Election Official  
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
2. System generates export (JSON, CSV) with vote counts, results, and metadata.
3. User downloads the file.

---

## UC-11: Manage Users and Roles

**Actor:** System Administrator  
**Goal:** Create accounts and assign roles to election officials, commission members, party administrators, and poll workers.

**Main Flow:**
1. Administrator creates a user account.
2. Administrator assigns one or more roles (Election Official, Central Electoral Commission, Party Administrator, Poll Worker, Observer).
3. User can log in and perform actions permitted by their role.

---

## UC-12: Submit Ballot Tally

**Actor:** Poll Worker (Chairperson)  
**Goal:** Enter and submit the paper ballot count for a polling district.

**Main Flow:**
1. Board chairperson initiates tally entry after the polling station closes.
2. Chairperson (or secretary) enters the ballot count per candidate as determined by the board's physical count.
3. System validates that total counts are internally consistent.
4. Chairperson reviews and confirms the tally.
5. System records the tally with status SUBMITTED.
6. Election Official is notified for review.

**Preconditions:** The voting period has closed; the board is assigned to the polling district.  
**Postconditions:** A BoardTally record exists in SUBMITTED status for the polling district.

---

## UC-13: Review and Approve Ballot Tallies

**Actor:** Election Official / Central Electoral Commission  
**Goal:** Review submitted ballot tallies and approve them for inclusion in constituency results.

**Main Flow:**
1. Official or commission member reviews submitted tallies from each polling district board.
2. Any discrepancies or anomalies are flagged and the relevant board is contacted for clarification.
3. Tallies are approved or rejected individually.
4. Once all tallies for a constituency are approved, the system aggregates them into a preliminary constituency result.

---

## Summary Matrix

| Use Case | Election Official | Candidate | Party Admin | Poll Worker | Central Electoral Commission | Observer | Sys Admin |
|---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| UC-01 Set Up Election | ✓ | | | | | | |
| UC-02 Register Party | ✓ | | ✓ | | | | |
| UC-03 Submit Candidate List | ✓ | | ✓ | | | | |
| UC-04 Register Candidate | ✓ | | ✓ | | | | |
| UC-05 Manage Polling Districts and Boards | ✓ | | | | | | |
| UC-06 View Candidates | ✓ | ✓ | ✓ | | | ✓ | |
| UC-07 Close Voting and Initiate Counting | ✓ | | | | | | |
| UC-08 Publish Results | ✓ | | | | ✓ | | |
| UC-09 View Results | ✓ | ✓ | ✓ | | ✓ | ✓ | |
| UC-10 Export Results | ✓ | | | | ✓ | ✓ | |
| UC-11 Manage Users | | | | | | | ✓ |
| UC-12 Submit Ballot Tally | | | | ✓ | | | |
| UC-13 Review and Approve Tallies | ✓ | | | | ✓ | | |
