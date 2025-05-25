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
| Test Case 1 | peekTopTwoCards(deck) when deck size = 0         | returns empty list                           |    yes          |
| Test Case 2 | peekTopTwoCards(deck) when deck size = 1         | returns list of 1 card                       |      yes        |
| Test Case 3 | peekTopTwoCards(deck) when deck size = 2         | returns list of 2 cards                      |     yes         |
| Test Case 4 | peekTopTwoCards(deck) when deck size > 2         | returns top 2 cards                          |     yes         |
| Test Case 5 | peekTopTwoCards(null)                            | throws IllegalArgumentException              |     yes         |

# BVA Analysis for SeeTheFutureCard.effect()

## Method: ```public void effect(List<Player> turnOrder, Deck deck)```

### Step 1-3 Results
|        | Input                                      | (if more to consider for input) | Output                                  |
| ------ | ------------------------------------------ | ------------------------------- | -------------------------------------- |
| Step 1 | turnOrder, deck                            |  |示        |
| Step 2 | List<Player>, Deck                         |                                 | None (only prints or calls view)      |
| Step 3 | deck.size: 0, 1, 2, >2; view: null / not null |                                 | None                                   |

### Step 4:
##### All-combination or each-choice: your decision

|             | System under test                               | Expected output                                                   | Implemented? |
| ----------- | ------------------------------------------------ | ----------------------------------------------------------------- | ------------ |
| Test Case 1 | effect() with deck.size = 0, view != null        | view.display() called with empty list (or fewer cards if logic)  | Yes          |
| Test Case 2 | effect() with deck.size = 1, view != null        | view.display() called with list of 1 card                         | Yes          |
| Test Case 3 | effect() with deck.size = 2, view != null        | view.display() called with list of 2 cards                         | Yes          |
| Test Case 4 | effect() with deck.size > 2, view != null        | view.display() called with list of 2 cards                         | Yes          |
| Test Case 5 | effect() with deck.size > 2, view == null        | prints "No view available to display future cards!"               | Yes          |
