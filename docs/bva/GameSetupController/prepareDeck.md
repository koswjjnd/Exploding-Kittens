# BVA Analysis for GameSetupController

## Method 1: `public Deck prepareDeck(int count)`

### Step 1-3 Results

|        | Input                     | (if more to consider for input) | Output                    |
|--------|---------------------------|---------------------------------|---------------------------|
| Step 1 | count (number of players) | Can be 2-4, <2, >4              | Deck (prepared game deck) |
| Step 2 | int (primitive type)      | Input range (1-5)               | Deck object               |
| Step 3 | 1, 2, 3, 4, 5             | Prepared deck or exception      |                           |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test | Expected output                             | Implemented? |
|-------------|-------------------|---------------------------------------------|--------------|
| Test Case 1 | prepareDeck(1)    | Throws InvalidPlayerCountException          | Yes          |
| Test Case 2 | prepareDeck(2)    | Returns deck with correct card distribution | Yes          |
| Test Case 3 | prepareDeck(4)    | Returns deck with correct card distribution | Yes          |
| Test Case 4 | prepareDeck(5)    | Throws InvalidPlayerCountException          | Yes          |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: Number of players in the game
- Output: Prepared game deck with correct card distribution
- Valid range: 2-4 players

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: int (primitive type)
- Output: Deck (object type)

### Step 3: Select concrete values along the edges for the input and the output.

- 1: Below minimum (invalid)
- 2: Minimum valid value
- 4: Maximum valid value
- 5: Above maximum (invalid)

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Using each-choice strategy since we have a single input parameter
- Testing boundary values (1, 2, 4, 5)
- Verifying both exception cases and successful deck preparation 