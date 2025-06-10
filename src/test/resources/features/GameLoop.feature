Feature: Game Loop Integration Tests
  As a game system
  I want to verify the game loop functionality
  So that I can ensure the game runs correctly

  @IT-TURN-01
  Scenario: Normal card play and draw
    Given the game loop is initialized with players having hands:
      | Player | Hand      |
      | P1     | Skip, Skip|
      | P2     | Skip, Skip|
    And the deck is stacked with cards:
      | Card  |
      | Favor |
    When player "P1" plays "Skip"
    And player "P1" chooses to draw a card
    Then player "P1" should have "Favor" in hand
    And player "P1" should have no "Skip" cards
    And the turn should end
    And the next player should be "P2"

  @IT-TURN-02
  Scenario: Draw exploding kitten and defuse
    Given the game loop is initialized with players having hands:
      | Player | Hand    |
      | P1     | Defuse  |
      | P2     | Skip    |
    And the deck is stacked with cards:
      | Card             |
      | ExplodingKitten  |
    When player "P1" chooses to draw a card
    Then player "P1" should be alive
    And player "P1" should have no "Defuse" cards
    And the deck should contain "ExplodingKitten"
    And the turn should end
    And the next player should be "P2"

  @IT-TURN-03
  Scenario: Draw exploding kitten without defuse
    Given the game loop is initialized with players having hands:
      | Player | Hand  |
      | P1     | Skip  |
      | P2     | Skip  |
    And the deck is stacked with cards:
      | Card             |
      | ExplodingKitten  |
    When player "P1" chooses to draw a card
    Then player "P1" should be eliminated
    And player "P1" should not be in turn order
    And the turn should end
    And the next player should be "P2"

  @IT-TURN-04
  Scenario: Attack card effect
    Given the game loop is initialized with players having hands:
      | Player | Hand        |
      | P1     | Attack      |
      | P2     | Skip, Skip  |
    When player "P1" plays "Attack"
    Then player "P2" should have 2 turns left
    And player "P1" should be at the end of turn order
    And the next player should be "P2"

  @IT-TURN-05
  Scenario: Favor card effect
    Given the game loop is initialized with players having hands:
      | Player | Hand           |
      | P1     | Favor          |
      | P2     | Skip, CatCard  |
    When player "P1" plays "Favor"
    And player "P2" gives a card to player "P1"
    Then player "P1" should have 1 card in hand
    And player "P2" should have 1 card in hand
    And the turn should end
    And the next player should be "P2"

  @IT-TURN-06
  Scenario: Nope card countering Favor
    Given the game loop is initialized with players having hands:
      | Player | Hand  |
      | P1     | Favor |
      | P2     | Nope  |
    When player "P1" plays "Favor"
    And player "P2" counters with "Nope"
    Then player "P1" should have no "Favor" cards
    And player "P2" should have no "Nope" cards
    And the turn should end
    And the next player should be "P2"

  @IT-TURN-07
  Scenario: Nope card countering Nope
    Given the game loop is initialized with players having hands:
      | Player | Hand           |
      | P1     | Favor          |
      | P2     | Nope, Skip     |
      | P3     | Nope           |
    When player "P1" plays "Favor"
    And player "P2" counters with "Nope"
    And player "P3" counters with "Nope"
    Then player "P1" should have no "Favor" cards
    And player "P2" should have no "Nope" cards
    And player "P3" should have no "Nope" cards
    And player "P2" should give a card to player "P1"
    And the turn should end
    And the next player should be "P2"

  @IT-TURN-08
  Scenario: Game over when only one player remains
    Given the game loop is initialized with players having hands:
      | Player | Hand  |
      | P1     | Skip  |
      | P2     | Skip  |
    When player "P2" is eliminated
    Then the game should be over
    And player "P1" should be the winner

  @IT-TURN-09
  Scenario: Game over when gameOver flag is set
    Given the game loop is initialized with players having hands:
      | Player | Hand  |
      | P1     | Skip  |
      | P2     | Skip  |
    When the game is marked as over
    Then the game should be over
    And player "P1" should be the winner 