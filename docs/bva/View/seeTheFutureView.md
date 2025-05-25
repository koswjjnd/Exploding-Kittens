# BVA Analysis for SeeTheFutureView

## Method: ```public void display(List<Card> cards)```

### Step 1-3 Results
|        | Input                  | (if more to consider for input) | Output       |
| ------ | ---------------------- | ------------------------------- | ------------ |
| Step 1 | cards                  | List of Card objects            | console output |
| Step 2 | List<Card>             |                                 | None         |
| Step 3 | cards.size(): 0, 1, 2+ |                                 | None         |

### Step 4:
##### All-combination or each-choice: your decision

|             | System under test                    | Expected output                      | Implemented? |
| ----------- | ------------------------------------ | ------------------------------------ | ------------ |
| Test Case 1 | display() with empty list            | prints header, no cards listed      |    yes      |
| Test Case 2 | display() with 1 card                | prints header, 1 card type listed   |    yes       |
| Test Case 3 | display() with 2+ cards              | prints header, all card types listed |     yes      |
