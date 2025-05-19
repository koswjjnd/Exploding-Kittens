# BVA Analysis for AttackCard

## Method 1: ```public void effect(List<Player> turnOrder, Deck gameDeck)```

### Step 1-3 Results

|        | Input                              | (if more to consider for input) | Output |
|--------|------------------------------------|---------------------------------|--------|
| Step 1 | turnOrder: List of players         | gameDeck: Game deck             | void   |
| Step 2 | turnOrder: List<Player>            | gameDeck: Deck                  | void   |
| Step 3 | turnOrder: empty, multiple players | gameDeck: valid deck            | void   |

### Step 4:

##### Each-choice strategy: Focus on effect execution

|             | System under test                                             | Expected output              | Implemented? |
|-------------|---------------------------------------------------------------|------------------------------|--------------|
| Test Case 1 | turnOrder = empty list                                        | IllegalArgumentException     |              |
| Test Case 2 | turnOrder = multiple players, current player's left turns = 0 | Next player's left turns = 2 |              |
| Test Case 3 | turnOrder = multiple players, current player's left turns = 1 | Next player's left turns = 3 |              |
| Test Case 4 | turnOrder = multiple players, current player's left turns = 2 | Next player's left turns = 4 |              |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain

- Input 1: List of players in turn order
- Input 2: Game deck
- Output: void, transfers current player's left turns + 2 to the next player

### Step 2: Choose the data type for the input and the output from the BVA Catalog

- turnOrder: List<Player>
- gameDeck: Deck
- Output: void

### Step 3: Select concrete values along the edges for the input and the output

- turnOrder: empty list, multiple players list
- gameDeck: valid deck

### Step 4: Determine the test cases using each-choice strategy

- Test empty list (invalid case)
- Test current player with 0 left turns
- Test current player with 1 left turn
- Test current player with 2 left turns
