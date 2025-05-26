# BVA Analysis for SnatchCard

## Method 1: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input | (if more to consider for input) | Output |
|--------|-------|---------------------------------|--------|
| Step 1 | turnOrder: List<Player> | targetPlayer's hand size | void (changes player hands) |
| Step 2 | List<Player> | int | void |
| Step 3 | Any number of players | - Empty hand<br>- Has cards | - Exception<br>- Random card transfer |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | targetPlayer has empty hand | IllegalStateException | No |
| Test Case 2  | targetPlayer has cards | Random card transferred to current player | No |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- Input: List of players (turnOrder) and target player's hand size
- Output: Randomly transfer one card to current player

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- Input: List<Player> and int (hand size)
- Output: void (effect achieved by modifying player hands)

### Step 3: Select concrete values along the edges for the input and the output.
- Player count: any valid number (not a boundary)
- Hand size boundaries: empty, has cards
- Output boundaries: exception, successful transfer

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
Using each-choice strategy to cover the two essential boundary cases.
