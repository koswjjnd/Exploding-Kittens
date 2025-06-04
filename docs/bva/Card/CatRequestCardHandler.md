# BVA Analysis for CatRequestCard.setInputHandler()

## Method: ```public static void setInputHandler(CardStealInputHandler handler)```
### Step 1-3 Results
|        | Input (handler) | Output |
|--------|----------------|--------|
| Step 1 | Input handler  | void   |
| Step 2 | CardStealInputHandler | void |
| Step 3 | null, invalid, valid, multiple, exception | void |

### Step 4:
##### Each-choice strategy

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | handler = null | No exception | Yes |
| Test Case 2  | handler = invalid implementation | No exception | Yes |
| Test Case 3  | handler = valid implementation | No exception | Yes |
| Test Case 4  | handler = multiple instances | No exception | Yes |
| Test Case 5  | handler = exception throwing | No exception | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- Input: Card steal input handler
- Output: void

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- Input: CardStealInputHandler
- Output: void

### Step 3: Select concrete values along the edges for the input and the output.
- handler: null, invalid, valid, multiple, exception
- Output: void

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy because:
  1. Each input parameter has clear boundary values
  2. Test cases already cover all key scenarios 