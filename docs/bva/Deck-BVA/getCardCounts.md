# BVA Analysis for deck

## Method 1: ```public TYPE getCardCounts```

### Step 1-3 Results

|        | Input                    | (if more to consider for input) | Output                       |
|--------|--------------------------|---------------------------------|------------------------------|
| Step 1 | cards                    |                                 | counts                       |
| Step 2 | List<Card> cards         |                                 | hashmap                      |
| Step 3 | cards has different type |                                 | {"Skip": 3, "Shuffle":3,...} |

### Step 4:

##### All-combination or each-choice: YOUR-DECISION

|             | System under test                         | Expected output                | Implemented? |
|-------------|-------------------------------------------|--------------------------------|--------------|
| Test Case 1 | getCardCounts() empty cards               | empty hashmap                  | yes          |
| Test Case 2 | getCardCounts() only 1 skip card in cards | {"Skip": 1}                    | yes          |
| Test Case 3 | dgetCardCounts()  1 card for each type    | {"Skip": 1, "Shuffle", 1, ...} | yes          |
