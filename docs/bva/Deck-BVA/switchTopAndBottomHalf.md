# BVA Analysis for Deck.switchTopAndBottomHalf()

## Method: ```public void switchTopAndBottomHalf()```

### Step 1-3 Results
|        | Input                            | Output / Effect                  |
| ------ | -------------------------------- | --------------------------------- |
| Step 1 | Deck with cards (list size)      | Cards in deck reordered          |
| Step 2 | List<Card>                       | None (deck itself is modified)   |
| Step 3 | deck.size: 0, 1, 2, >3           | Different reordering strategies  |

### Step 4:
##### All-combination or each-choice: your decision

|             | System under test                         | Expected output                                                               | Implemented? |
| ----------- | ------------------------------------------ | ----------------------------------------------------------------------------- | ------------ |
| Test Case 1 | switchTopAndBottomHalf() with deck.size = 0 | Deck remains empty                                                            | Yes          |
| Test Case 2 | switchTopAndBottomHalf() with deck.size = 1 | Deck remains unchanged (1 card stays)                                         | Yes          |
| Test Case 3 | switchTopAndBottomHalf() with deck.size = 2 | Two cards swapped                                                             | Yes          |
| Test Case 4 | switchTopAndBottomHalf() with deck.size > 3 and even | Top half and bottom half swapped                                              | Yes          |
| Test Case 5 | switchTopAndBottomHalf() with deck.size > 3 and odd  | Middle card stays in place, top/bottom halves swapped (excluding middle)      | Yes          |
