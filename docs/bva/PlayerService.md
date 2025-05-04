# BVA Analysis for PlayerService

## Method 1: ```public void validateCount(int count)```

### Step 1-3 Results

|        | Input                | (if more to consider for input) | Output                                        |
|--------|----------------------|---------------------------------|-----------------------------------------------|
| Step 1 | player count (count) |                                 | nothing (valid) or throws exception (invalid) |
| Step 2 | Count (integer)      |                                 | Cases: Void (no return), exception            |
| Step 3 | 1, 2, 3, 4, 5        |                                 | 1,5 → exception; 2,3,4 → no exception         |

### Step 4:

##### All-combination or each-choice: each-choice

|              | System under test | Expected output                 | Implemented? |
|--------------|-------------------|---------------------------------|--------------|
| Test Case 1  | validateCount(1)  | throws IllegalArgumentException | Yes          |
| Test Case 2  | validateCount(2)  | completes normally              | Yes          |
| Test Case 3  | validateCount(3)  | completes normally              | Yes          |
| Test Case 4  | validateCount(4)  | completes normally              |              |
| Test Case 5  | validateCount(5)  | throws IllegalArgumentException |              |
| Test Case 6  |                   |                                 |              |
| Test Case 7  |                   |                                 |              |
| Test Case 8  |                   |                                 |              |
| Test Case 9  |                   |                                 |              |
| Test Case 10 |                   |                                 |              |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
