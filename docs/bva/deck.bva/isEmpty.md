# BVA Analysis for Deck

## Method: ```isEmpty()```

### Step 1-3 Results

|        | Input                                    | (if more to consider for input) | Output                              |
|--------|------------------------------------------|---------------------------------|-------------------------------------|
| Step 1 | deck: Deck object                        | cards list state                | boolean indicating if deck is empty |
| Step 2 | deck: Object                             | cards: List<Card>               | isEmpty: boolean                    |
| Step 3 | cards: null, empty, one card, many cards | N/A                             | true/false                          |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test    | Expected output      | Implemented? |
|-------------|----------------------|----------------------|--------------|
| Test Case 1 | cards=null           | NullPointerException | No           |
| Test Case 2 | cards=empty list     | true                 | No           |
| Test Case 3 | cards=one card       | false                | No           |
| Test Case 4 | cards=multiple cards | false                | No           |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy. 