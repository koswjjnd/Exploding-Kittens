Feature: Game Setup Integration Test

  Background:
    Given the game system is ready

  # 1. Game Initialization Scenarios
  Scenario: IT-Setup-01 GameContext normal initialization
    When the game is initialized
    Then the game context should be initialized correctly
    And the default parameters should be correct:
      | Parameter      | Value |
      | Max Players   | 4     |
      | Initial Turns | 1     |
      | Initial Hand  | 6     |

  Scenario: IT-Setup-02 Player list creation and setup
    When the game is initialized with players:
      | Player |
      | P1     |
      | P2     |
      | P3     |
    Then the player list should be created
    And each player should have correct name
    And the player count should be 3

  Scenario: IT-Setup-03 Deck initialization
    When the game is initialized with 3 players
    And each player has 6 cards
    Then the deck size should be correct
    And the deck should be properly shuffled

  Scenario: IT-Setup-04 Initial turn order setup
    When the game is initialized with players:
      | Player |
      | P1     |
      | P2     |
      | P3     |
    Then the current player should be P1
    And the turn order should be P1, P2, P3

  # 2. Player Hand Setup Scenarios
  Scenario: IT-Setup-05 Player hand distribution
    When the game is initialized with 3 players
    Then each player should have 6 cards

  Scenario: IT-Setup-06 Card instance creation
    When the game is initialized with players having hands:
      | Player | Hand                    |
      | P1     | Attack, Skip, Favor     |
      | P2     | Attack, Skip, CatCard   |
      | P3     | Attack, Skip, Defuse    |
    Then all cards should be valid instances
    And each card should be of correct type

  Scenario: IT-Setup-07 Hand size verification
    When the game is initialized with players having hands:
      | Player | Hand                    |
      | P1     | Attack, Skip, Favor     |
      | P2     | Attack, Skip, CatCard   |
      | P3     | Attack, Skip, Defuse    |
    Then P1 should have 6 cards
    And P2 should have 6 cards
    And P3 should have 6 cards

  Scenario: IT-Setup-08 Card type verification
    When the game is initialized with players having hands:
      | Player | Hand                    |
      | P1     | Attack, Skip, Favor     |
      | P2     | Attack, Skip, CatCard   |
      | P3     | Attack, Skip, Defuse    |
    Then all cards should have valid types

  # 3. Game State Verification Scenarios
  Scenario: IT-Setup-09 Initial turn order verification
    When the game is initialized with players:
      | Player |
      | P1     |
      | P2     |
      | P3     |
    Then the next player should be P2
    And the turn order should be P1, P2, P3

  Scenario: IT-Setup-10 Initial player state verification
    When the game is initialized with 3 players
    Then all players should be in READY state
    And the game should be ready to start

  # 4. Error Handling Scenarios
  Scenario: IT-Error-01 Invalid player count
    When the game is initialized with 1 player
    Then an InvalidPlayerCountException should be thrown
    When the game is initialized with 5 players
    Then an InvalidPlayerCountException should be thrown

  Scenario: IT-Error-02 Invalid card type
    When the game is initialized with players having hands:
      | Player | Hand                    |
      | P1     | InvalidCard, Attack     |
    Then an InvalidCardTypeException should be thrown

  Scenario: IT-Error-03 Duplicate player names
    When the game is initialized with players:
      | Player |
      | P1     |
      | P1     |
      | P3     |
    Then an InvalidNicknameException should be thrown