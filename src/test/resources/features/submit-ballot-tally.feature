Feature: Submit Ballot Tally
  UC-12: As a poll worker chairperson, I want to enter and submit the ballot
  count for my polling district, so that the tally is recorded for review.

  Scenario: Chairperson submits individual candidate vote count
    Given there is polling station "Helsinki Central"
    And there is candidate called Maria Virtanen
    When the chairperson enters the following ballot count
      | candidate      | votes |
      | Maria Virtanen | 245   |
    Then the individual vote count of Maria Virtanen is 245

  Scenario: Chairperson submits vote counts for several candidates
    Given there is polling station "Helsinki Central"
    And there is candidate called Maria Virtanen
    And there is candidate called Jukka Korhonen
    When the chairperson enters the following ballot count
      | candidate      | votes |
      | Maria Virtanen | 245   |
      | Jukka Korhonen | 198   |
    Then the recorded vote counts for candidates are
      | candidate      | votes |
      | Maria Virtanen | 245   |
      | Jukka Korhonen | 198   |

  @wip 
  Scenario: Chairperson submits the ballot tally
    Given there is polling station "Helsinki Central"
    And there is candidate called Maria Virtanen
    And there is candidate called Jukka Korhonen
    When the chairperson enters the following ballot count
      | candidate      | votes |
      | Maria Virtanen | 245   |
      | Jukka Korhonen | 198   |
    And the chairperson reviews and confirms the tally
    Then the tally is recorded with status "SUBMITTED"
    And the election official is notified to review the tally
