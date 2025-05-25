# BVA Analysis for FavorCard

## Method 1: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input | (if more to consider for input) | Output |
|--------|-------|---------------------------------|--------|
| Step 1 | turnOrder: List<Player> | targetPlayer's hand size | void (changes player hands) |
| Step 2 | List<Player> | int | void |
| Step 3 | - 2 players<br>- 3 players<br>- 4 players | - Empty hand<br>- 1 card<br>- Multiple cards | - Exception<br>- Card transfer |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | turnOrder = 2 players, targetPlayer has empty hand | IllegalStateException | No |
| Test Case 2  | turnOrder = 2 players, targetPlayer has 1 card | Card transferred to current player | No |
| Test Case 3  | turnOrder = 2 players, targetPlayer has multiple cards | First card transferred to current player | No |
| Test Case 4  | turnOrder = 3 players, targetPlayer has empty hand | IllegalStateException | No |
| Test Case 5  | turnOrder = 3 players, targetPlayer has 1 card | Card transferred to current player | No |
| Test Case 6  | turnOrder = 3 players, targetPlayer has multiple cards | First card transferred to current player | No |
| Test Case 7  | turnOrder = 4 players, targetPlayer has empty hand | IllegalStateException | No |
| Test Case 8  | turnOrder = 4 players, targetPlayer has 1 card | Card transferred to current player | No |
| Test Case 9  | turnOrder = 4 players, targetPlayer has multiple cards | First card transferred to current player | No |
| Test Case 10 | turnOrder = 2 players, targetPlayer is current player | IllegalArgumentException | No |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
