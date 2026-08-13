Feature: Manage Candidates
  As an election administrator, I want to register candidates and retrieve
  the list of registered candidates, so that they can be included in the
  ballot tally.

  Scenario: Administrator registers a new candidate
    When the administrator registers a candidate called Maria Virtanen
    Then Maria Virtanen is a registered candidate

  Scenario: Administrator retrieves the list of registered candidates
    Given there are following registered candidates
      | candidate      |
      | Maria Virtanen |
      | Jukka Korhonen |
    When the administrator lists the registered candidates
    Then the list of registered candidates includes Maria Virtanen
