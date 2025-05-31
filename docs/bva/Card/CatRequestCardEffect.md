# BVA Analysis for CatRequestCard.effect()

## Method: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input (turnOrder) | Input (gameDeck) | Output |
|--------|-------------------|------------------|--------|
| Step 1 | List of players   | Game deck        | void   |
| Step 2 | List<Player>      | Deck             | void   |
| Step 3 | null, empty, [1], [2], [2-4], [5], [>5] | null, empty, valid | void or Exception |

### Step 4:
##### Each-choice strategy (Using each-choice strategy due to too many input combinations)

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | Input handler not set | IllegalStateException | Yes |
| Test Case 2  | Player has no turns left | IllegalStateException | Yes |
| Test Case 3  | Player has less than three cat cards of EXACT SAME TYPE | IllegalStateException | Yes |
| Test Case 4  | Player has exactly three cat cards of EXACT SAME TYPE | Success | Yes |
| Test Case 5  | Player has more than three cat cards of EXACT SAME TYPE | Success | Yes |
| Test Case 6  | No other players available | IllegalStateException | Yes |
| Test Case 7  | Target player is dead | IllegalStateException | Yes |
| Test Case 8  | Target player has no cards | IllegalStateException | Yes |
| Test Case 9  | Target player has the requested card | Card requested | Yes |
| Test Case 10 | Target player doesn't have the requested card | Cards discarded | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- Input: List of players and game deck
- Output: void

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- Input: List<Player>, Deck
- Output: void

### Step 3: Select concrete values along the edges for the input and the output.
- turnOrder: null, empty, [1], [2], [2-4], [5], [>5]
- gameDeck: null, empty, valid
- Output: void or Exception

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy because:
  1. Too many input combinations, all-combination would generate too many test cases
  2. Each input parameter has clear boundary values
  3. Test cases already cover all key scenarios 