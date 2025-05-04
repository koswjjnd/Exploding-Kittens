# BVA Analysis for deck

## Method 1: ```public void addCards```
### Step 1-3 Results
|        | Input | (if more to consider for input) | Output |
|--------|-------|---------------------------------|--------|
| Step 1 |  card, count   |                                 |  None      |
| Step 2 |  Card card, int count  |                                 |  None     |
| Step 3 |  count is 0, 1, 2  |                                 |   None    |
### Step 4:
##### All-combination or each-choice: YOUR-DECISION

|              | System under test      | Expected output                    | Implemented? |
|--------------|------------------------|------------------------------------|--------------|
| Test Case 1  | addCards(Shuffle card, 0) | no cards added |          yes    |
| Test Case 2  | addCards(Skip card, 1) | 1 card added |        yes      |
| Test Case 3  | addCards(see the future card, 2) | 2 cards added |              |
