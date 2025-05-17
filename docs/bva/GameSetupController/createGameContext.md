# BVA Analysis for GameSetupController

## Method 1: `public GameContext createGameContext(List<Player> order)`

### Step 1-3 Results

|        | Input                                        | (if more to consider for input)       | Output                   |
|--------|----------------------------------------------|---------------------------------------|--------------------------|
| Step 1 | List<Player> order                           | Can be null, empty, 1+, contain null  | GameContext object       |
| Step 2 | List of object references                    | Input length (0–N), may contain nulls | New GameContext instance |
| Step 3 | null, [], [p1], [p1, p2], [null], [p1, null] | GameContext with turn order set       |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test             | Expected output                   | Implemented? |
|-------------|-------------------------------|-----------------------------------|--------------|
| Test Case 1 | createGameContext(null)       | Throws IllegalArgumentException   | Yes          |
| Test Case 2 | createGameContext([])         | Throws IllegalArgumentException   | Yes          |
| Test Case 3 | createGameContext([p1])       | Returns GameContext with [p1]     | Yes          |
| Test Case 4 | createGameContext([p1, p2])   | Returns GameContext with [p1, p2] | Yes          |
| Test Case 5 | createGameContext([null])     | Throws IllegalArgumentException   | Yes          |
| Test Case 6 | createGameContext([p1, null]) | Throws IllegalArgumentException   | Yes          |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: List of Player objects representing the turn order
- Output: New GameContext instance with turn order set
- Valid range: Non-null, non-empty list of valid Player objects

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: List<Player> (collection type)
- Output: GameContext (object type)

### Step 3: Select concrete values along the edges for the input and the output.

- null: Invalid input
- []: Empty list
- [p1]: Single player
- [p1, p2]: Multiple players
- [null]: List with null element
- [p1, null]: List with valid and null elements

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Using each-choice strategy since we have a single input parameter
- Testing all edge cases and normal cases
- Verifying both the exception cases and successful creation 