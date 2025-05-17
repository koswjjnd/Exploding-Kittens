# BVA Analysis for Deck

## Method 1: ```public Deck addExplodingKittens(int count)```

### Step 1-3 Results

|        | Input                                     | (if more to consider for input)                   | Output |
|--------|-------------------------------------------|---------------------------------------------------|--------|
| Step 1 | count: number of exploding kittens to add | deck state (empty/non-empty)                      | Deck   |
| Step 2 | int                                       | none                                              | Deck   |
| Step 3 | -1, 0, 1, 2, n-1                          | empty deck, single card deck, multiple cards deck | Deck   |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test            | Expected output                  | Implemented? |
|-------------|------------------------------|----------------------------------|--------------|
| Test Case 1 | deck.addExplodingKittens(0)  | no exploding kittens in deck     | Yes          |
| Test Case 2 | deck.addExplodingKittens(-1) | throws IllegalArgumentException  | Yes          |
| Test Case 3 | deck.addExplodingKittens(2)  | adds 2 exploding kittens to deck | Yes          |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
