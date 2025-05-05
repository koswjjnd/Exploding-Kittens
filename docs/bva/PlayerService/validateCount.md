# BVA Analysis for PlayerService

## Method 1: `public void validateCount(int count)`

### Step 1-3 Results

|        | Input                     | Output                                               |
| ------ | ------------------------- | ---------------------------------------------------- |
| Step 1 | count (number of players) | void (throws InvalidPlayerCountException if invalid) |
| Step 2 | int (primitive type)      | void (no return value)                               |
| Step 3 | 1, 2, 3, 4, 5             | throws exception or no exception                     |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test | Expected output                    | Implemented? |
| ----------- | ----------------- | ---------------------------------- | ------------ |
| Test Case 1 | count = 1         | throws InvalidPlayerCountException | Yes          |
| Test Case 2 | count = 2         | no exception                       | Yes          |
| Test Case 3 | count = 3         | no exception                       | Yes          |
| Test Case 4 | count = 4         | no exception                       | Yes          |
| Test Case 5 | count = 5         | throws InvalidPlayerCountException | Yes          |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: count represents the number of players in the game
- Output: void method that throws InvalidPlayerCountException if count is invalid
- Valid range: 2-4 players

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: int (primitive type)
- Output: void (no return value)

### Step 3: Select concrete values along the edges for the input and the output.

- Below minimum: 1
- Minimum: 2
- Normal value: 3
- Maximum: 4
- Above maximum: 5

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Using each-choice strategy since we have a single input parameter
- Testing all boundary values and one normal value
- Testing both valid and invalid cases
