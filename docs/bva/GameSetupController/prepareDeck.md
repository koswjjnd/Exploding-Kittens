# BVA Analysis for GameSetupController

## Method 1: `public Deck prepareDeck(int count, List<Player> players)`

### Step 1-3 Results

|        | Input                     | (if more to consider for input) | Output                                       |
|--------|---------------------------|---------------------------------|----------------------------------------------|
| Step 1 | count (number of players) | Can be 2-4, <2, >4              | Deck (prepared game deck) and players' hands |
| Step 2 | int (primitive type)      | Input range (1-5)               | Deck object and List<Player> with hands      |
| Step 3 | 1, 2, 3, 4, 5             | Prepared deck or exception      |                                              |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test       | Expected output                                                                 | Implemented? |
|-------------|-------------------------|---------------------------------------------------------------------------------|--------------|
| Test Case 1 | prepareDeck(1, players) | Throws InvalidPlayerCountException                                              | Yes          |
| Test Case 2 | prepareDeck(2, players) | Returns deck with correct card distribution and players have 1 defuse + 5 cards | Yes          |
| Test Case 3 | prepareDeck(4, players) | Returns deck with correct card distribution and players have 1 defuse + 5 cards | Yes          |
| Test Case 4 | prepareDeck(5, players) | Throws InvalidPlayerCountException                                              | Yes          |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: Number of players in the game and list of players
- Output: Prepared game deck with correct card distribution and players' hands (1 defuse + 5 cards each)
- Valid range: 2-4 players

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: int (primitive type) and List<Player> (object type)
- Output: Deck (object type) and List<Player> with hands (object type)

### Step 3: Select concrete values along the edges for the input and the output.

- 1: Below minimum (invalid)
- 2: Minimum valid value
- 4: Maximum valid value
- 5: Above maximum (invalid)

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Using each-choice strategy since we have a single input parameter
- Testing boundary values (1, 2, 4, 5)
- Verifying both exception cases and successful deck preparation with initial hands
- Verifying correct card distribution in deck and players' hands 