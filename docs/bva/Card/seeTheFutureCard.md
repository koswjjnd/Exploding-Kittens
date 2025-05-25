# BVA Analysis for SeeTheFutureCard

## Method: ```public List<Card> peekTopTwoCards(Deck deck)```

### Step 1-3 Results
|        | Input               | (if more to consider for input) | Output        |
| ------ | --------------------| ------------------------------- | ------------- |
| Step 1 | deck                |                                 | cardList   |
| Step 2 | Deck deck           |                                 | List<Card>    |
| Step 3 | deck size: 0, 1, 2, >2                      |                                 | None (peek)   |

### Step 4:
##### All-combination or each-choice: your decision

|             | System under test                               | Expected output                              | Implemented? |
| ----------- | ------------------------------------------------ | -------------------------------------------- | ------------ |
| Test Case 1 | peekTopTwoCards(deck) when deck size = 0         | returns empty list                           |              |
| Test Case 2 | peekTopTwoCards(deck) when deck size = 1         | returns list of 1 card                       |              |
| Test Case 3 | peekTopTwoCards(deck) when deck size = 2         | returns list of 2 cards                      |              |
| Test Case 4 | peekTopTwoCards(deck) when deck size > 2         | returns top 2 cards                          |              |
| Test Case 5 | peekTopTwoCards(null)                            | throws IllegalArgumentException              |              |

