# BVA Analysis for ExplodingKittenCard

## Method: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input (turnOrder) | Input (gameDeck) | Output (Player Status) |
|--------|------------------|------------------|----------------------|
| Step 1 | turnOrder = null | gameDeck = null  | Throws NullPointerException |
| Step 2 | turnOrder = empty list | gameDeck = any | Throws IndexOutOfBoundsException |
| Step 3 | turnOrder = valid list | gameDeck = any | Player dies if no defuse card |

### Step 4:
##### Strategy: each-choice (since we're testing two input parameters with clear boundaries)

| Test Case | System under test | Expected output | Implemented? |
|-----------|-------------------|-----------------|--------------|
| Test Case 1 | turnOrder = null, gameDeck = null | Throws NullPointerException | Yes |
| Test Case 2 | turnOrder = empty list, gameDeck = valid deck | Throws IndexOutOfBoundsException | Yes |
| Test Case 3 | turnOrder = valid list with player having defuse, gameDeck = valid deck | Player stays alive | Yes |
| Test Case 4 | turnOrder = valid list with player having no defuse, gameDeck = valid deck | Player dies | Yes |
| Test Case 5 | turnOrder = valid list with dead player, gameDeck = valid deck | Player stays dead | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- Input: 
  - turnOrder: List of players in the game
  - gameDeck: Deck of cards in the game
- Output: 
  - Player's alive status (true/false)
  - Exception (when inputs are invalid)

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- Input: 
  - List<Player> (turnOrder)
  - Deck (gameDeck)
- Output: 
  - boolean (player's alive status)
  - Exception (NullPointerException, IndexOutOfBoundsException)

### Step 3: Select concrete values along the edges for the input and the output.
- Input boundaries:
  - turnOrder:
    - null
    - empty list
    - valid list with players
  - gameDeck:
    - null
    - valid deck
- Output boundaries:
  - Player status:
    - alive (true)
    - dead (false)
  - Exceptions:
    - NullPointerException
    - IndexOutOfBoundsException

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy since we're testing two input parameters
- Test cases cover all boundary conditions and typical scenarios
- Note: The actual defuse functionality is handled by the game controller when a player draws an Exploding Kitten 