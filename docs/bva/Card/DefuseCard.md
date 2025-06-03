# BVA Analysis for DefuseCard

## Method: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input (turnOrder) | Input (gameDeck) | Output |
|--------|------------------|------------------|--------|
| Step 1 | turnOrder = null | gameDeck = null  | Throws NullPointerException |
| Step 2 | turnOrder = empty list | gameDeck = empty deck | No effect (card is handled by game controller) |
| Step 3 | turnOrder = valid list | gameDeck = valid deck | No effect (card is handled by game controller) |

### Step 4:
##### Strategy: each-choice (since we're testing two input parameters with clear boundaries)

| Test Case | System under test | Expected output | Implemented? |
|-----------|-------------------|-----------------|--------------|
| Test Case 1 | turnOrder = null, gameDeck = null | Throws NullPointerException | Yes |
| Test Case 2 | turnOrder = empty list, gameDeck = empty deck | No effect | Yes |
| Test Case 3 | turnOrder = valid list, gameDeck = valid deck | No effect | Yes |
| Test Case 4 | turnOrder = null, gameDeck = valid deck | Throws NullPointerException | Yes |
| Test Case 5 | turnOrder = valid list, gameDeck = null | Throws NullPointerException | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- Input: 
  - turnOrder: List of players in the game
  - gameDeck: Deck of cards in the game
- Output: 
  - No direct effect (card is handled by game controller)
  - Exception (when inputs are invalid)

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- Input: 
  - List<Player> (turnOrder)
  - Deck (gameDeck)
- Output: 
  - void (no direct effect)
  - Exception (NullPointerException)

### Step 3: Select concrete values along the edges for the input and the output.
- Input boundaries:
  - turnOrder:
    - null
    - empty list
    - valid list with players
  - gameDeck:
    - null
    - empty deck
    - valid deck with cards
- Output boundaries:
  - No direct effect (handled by game controller)
  - Exception: NullPointerException

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy since we're testing two input parameters
- Test cases cover all boundary conditions and typical scenarios
- Note: The actual defuse functionality is handled by the game controller when a player draws an Exploding Kitten
