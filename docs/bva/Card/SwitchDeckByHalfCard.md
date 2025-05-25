# BVA Analysis for SwitchDeckByHalfCard.effect()

## Method: ```public void effect(List<Player> turnOrder, Deck deck)```

### Step 1-3 Results
|        | Input                                      | (if more to consider for input) | Output                                  |
| ------ | ------------------------------------------ | ------------------------------- | --------------------------------------- |
| Step 1 | turnOrder, deck                            | deck will be modified, turnOrder is unused | deck order is changed              |
| Step 2 | List<Player>, Deck                         |                                 | None (deck is modified)              |
| Step 3 | deck: null / non-null, deck.size: 0, 1, 2, >3 |                                 | deck modified or IllegalArgumentException |

### Step 4:
##### All-combination or each-choice: your decision

|             | System under test                                  | Expected output                                                              | Implemented? |
| ----------- | --------------------------------------------------- | ---------------------------------------------------------------------------- | ------------ |
| Test Case 1 | effect() with deck == null                          | IllegalArgumentException thrown                                              |    yes       |
| Test Case 2 | effect() with deck.size = 0                         | No changes to deck                                                           |   yes       |
| Test Case 3 | effect() with deck.size = 1                         | No changes to deck                                                           |     yes      |
| Test Case 4 | effect() with deck.size = 2                         | Two cards are swapped                                                        |    yes       |
| Test Case 5 | effect() with deck.size > 3 and even                | Top half and bottom half of deck are swapped                                 |           |
| Test Case 6 | effect() with deck.size > 3 and odd                 | Middle card remains unchanged; top half and bottom half (excluding middle) are swapped |           |
