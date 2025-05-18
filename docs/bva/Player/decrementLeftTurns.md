# BVA Analysis for Player

## Method 1: `public void decrementLeftTurns()`

### Step 1-3 Results

|        | Input               | Output                                 |
| ------ | ------------------- | -------------------------------------- |
| Step 1 | No input parameters | void (decrements leftTurns by 1)       |
| Step 2 | No input parameters | void                                   |
| Step 3 | No input parameters | State boundaries: leftTurns = 0, 1, >1 |

### Step 4:

##### Strategy: each-choice (since there's no input parameter, we'll test different state scenarios)

|             | System under test               | Expected output            | Implemented? |
| ----------- | ------------------------------- | -------------------------- | ------------ |
| Test Case 1 | Player with 1 turn left         | leftTurns = 0              | No           |
| Test Case 2 | Player with 0 turns left        | leftTurns = 0 (no change)  | No           |
| Test Case 3 | Player with multiple turns left | leftTurns decremented by 1 | No           |
| Test Case 4 | New player (default 1 turn)     | leftTurns = 0              | No           |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: No input parameters required
- Output: void (modifies internal state)
- Domain: Decrements the number of turns left for the player by 1, but never goes below 0

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: No input parameters
- Output: void
- Internal state: int (leftTurns)

### Step 3: Select concrete values along the edges for the input and the output.

- State boundaries:
  - Minimum value: 0 (no turns left)
  - Default value: 1 (new player)
  - Multiple turns: 3 (example of multiple turns)
  - Edge case: 0 turns (should not go below 0)

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Strategy: each-choice
- Rationale: Since there are no input parameters, we focus on testing different state scenarios
- Test cases cover:
  1. Normal case (1 turn to 0)
  2. Boundary case (0 turns)
  3. Multiple turns case
  4. Default state case
