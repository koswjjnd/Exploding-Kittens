# BVA Analysis for CatCard

## Method 1: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input (turnOrder) | Input (gameDeck) | Output |
|--------|-------------------|------------------|--------|
| Step 1 | List of players   | Game deck        | void   |
| Step 2 | List<Player>      | Deck             | void   |
| Step 3 | null, empty, [1], [2], [2-4], [5], [>5] | null, empty, valid | CatCardEffect or Exception |

### Step 4:
##### Each-choice strategy (Using each-choice strategy due to too many input combinations)

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | Player has no cat cards | IllegalStateException | Yes |
| Test Case 2  | Player has only one cat card | IllegalStateException | Yes |
| Test Case 3  | Player has two different types of cat cards | IllegalStateException | Yes |
| Test Case 4  | Player has two same type cat cards | CatCardEffect | Yes |
| Test Case 5  | Player has multiple same type cat cards | CatCardEffect | Yes |
| Test Case 6  | No other players available | IllegalStateException | Yes |
| Test Case 7  | Target player is dead | IllegalStateException | Yes |
| Test Case 8  | Target player has empty hand | IllegalStateException | Yes |
| Test Case 9  | Target player has multiple cards | CatCardEffect | Yes |
| Test Case 10 | Player has no turns left | IllegalStateException | Yes |

## Method 2: ```public static void setInputHandler(CardStealInputHandler handler)```
### Step 1-3 Results
|        | Input (handler) | Output |
|--------|----------------|--------|
| Step 1 | Input handler  | void   |
| Step 2 | CardStealInputHandler | void |
| Step 3 | null, invalid, valid, multiple, exception | void |

### Step 4:
##### Each-choice strategy

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | handler = null | No exception | Yes |
| Test Case 2  | handler = invalid implementation | No exception | Yes |
| Test Case 3  | handler = valid implementation | No exception | Yes |
| Test Case 4  | handler = multiple instances | No exception | Yes |
| Test Case 5  | handler = exception throwing | No exception | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- effect method:
  - Input: List of players and game deck
  - Output: void (returns CatCardEffect through exception)
- setInputHandler method:
  - Input: Card steal input handler
  - Output: void

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- effect method:
  - Input: List<Player>, Deck
  - Output: void
- setInputHandler method:
  - Input: CardStealInputHandler
  - Output: void

### Step 3: Select concrete values along the edges for the input and the output.
- effect method:
  - turnOrder: null, empty, [1], [2], [2-4], [5], [>5]
  - gameDeck: null, empty, valid
  - Output: CatCardEffect or Exception
- setInputHandler method:
  - handler: null, invalid, valid, multiple, exception
  - Output: void

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy because:
  1. Too many input combinations, all-combination would generate too many test cases
  2. Each input parameter has clear boundary values
  3. Test cases already cover all key scenarios 