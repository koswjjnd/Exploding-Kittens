# BVA Analysis for GameSetupController

## Method 1: `public List<Player> createPlayers(int count)`

### Step 1-3 Results

|        | Input                     | Output                                    |
| ------ | ------------------------- | ----------------------------------------- |
| Step 1 | count (number of players) | List<Player> (list of created players)    |
| Step 2 | int (primitive type)      | List<Player> (collection type)            |
| Step 3 | 2, 3, 4                   | List with corresponding number of players |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test | Expected output                  | Implemented? |
| ----------- | ----------------- | -------------------------------- | ------------ |
| Test Case 1 | count = 2         | returns list with 2 players      | Yes          |
| Test Case 2 | count = 3         | returns list with 3 players      | Yes          |
| Test Case 3 | count = 4         | returns list with 4 players      | Yes          |
| Test Case 4 | count = 2         | calls promptNickname twice       | Yes          |
| Test Case 5 | count = 3         | calls promptNickname three times | No           |
| Test Case 6 | count = 4         | calls promptNickname four times  | No           |
| Test Case 7 | count = 2         | calls createPlayer twice         | No           |
| Test Case 8 | count = 3         | calls createPlayer three times   | No           |
| Test Case 9 | count = 4         | calls createPlayer four times    | No           |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: count represents the number of players to create
- Output: List of Player objects
- Valid range: 2-4 players (already validated by PlayerService)

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: int (primitive type)
- Output: List<Player> (collection type)

### Step 3: Select concrete values along the edges for the input and the output.

- Minimum: 2 players
- Normal: 3 players
- Maximum: 4 players

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Using each-choice strategy since we have a single input parameter
- Testing all valid player counts (2, 3, 4)
- Testing both the output (list size) and the process (method calls)
- Testing interaction with dependencies (GameSetupView and PlayerService)
