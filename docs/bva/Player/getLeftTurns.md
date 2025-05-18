# BVA Analysis for Player

## Method 1: `public int getLeftTurns()`

### Step 1-3 Results

|        | Input               | Output                                      |
| ------ | ------------------- | ------------------------------------------- |
| Step 1 | No input parameters | Number of turns left (non-negative integer) |
| Step 2 | No input parameters | Output: int (non-negative integer)          |
| Step 3 | No input parameters | Output boundaries: 0, 1, MAX_VALUE          |

### Step 4:

##### Strategy: each-choice (since there's no input parameter, we'll test different output scenarios)

|             | System under test               | Expected output | Implemented? |
| ----------- | ------------------------------- | --------------- | ------------ |
| Test Case 1 | New player with default turns   | 1               | Yes          |
| Test Case 2 | Player with 0 turns left        | 0               | Yes          |
| Test Case 3 | Player with multiple turns left | 3               | Yes          |
| Test Case 4 | Player after using skip card    | 0               | Yes          |
| Test Case 5 | Player after using attack card  | 2               | Yes          |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: No input parameters required
- Output: Number of turns left for the player (non-negative integer)

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: No input parameters
- Output: int (non-negative integer)

### Step 3: Select concrete values along the edges for the input and the output.

- Output boundaries:
  - Minimum value: 0 (no turns left)
  - Default value: 1 (new player)
  - Multiple turns: 3 (example of multiple turns)
  - After skip card: 0 (reduced from 1)
  - After attack card: 2 (increased from 1)

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Strategy: each-choice
- Rationale: Since there are no input parameters, we focus on testing different output scenarios based on the player's state and actions
- Test cases cover:
  1. Default state (1 turn)
  2. Zero turns boundary
  3. Multiple turns scenario
  4. Skip card effect
  5. Attack card effect
