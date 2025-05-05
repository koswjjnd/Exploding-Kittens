# BVA Analysis for deck

## Method 1: ```public TYPE initializeBaseDeck```
### Step 1-3 Results
|        | Input | (if more to consider for input) | Output |
|--------|-------|---------------------------------|--------|
| Step 1 |  playerCount     |                                 |  None      |
| Step 2 |  int     |                                 |  None      |
| Step 3 |  0,1,2,3     |                                 |   None     |
### Step 4:
##### All-combination or each-choice: YOUR-DECISION

|              | System under test      | Expected output                    | Implemented? |
|--------------|------------------------|------------------------------------|--------------|
| Test Case 1  | initializeBaseDeck(1)  | Should throw IllegalArgumentException |         yes     |
| Test Case 2  | initializeBaseDeck(2)  | add 3 defuse card                 |     yes         |
| Test Case 3  | initializeBaseDeck(4)  | add 1 defuse card                 |     yes         |
| Test Case 4  | initializeBaseDeck(5)  | Should throw IllegalArgumentException |   yes           |
