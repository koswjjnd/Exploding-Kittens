# BVA Analysis for dealService

## Method 1: ```dealDefuses(deck, players)```

### Step 1-3 Results

|        | Input                                    | (if more to consider for input)  | Output                                            |
|--------|------------------------------------------|----------------------------------|---------------------------------------------------|
| Step 1 | deck: Deck object, players: List<Player> | number of defuse cards in deck   | each player gets one defuse card                  |
| Step 2 | deck: Object, players: Collection        | number of defuse cards: int      | number of defuse cards in each player's hand: int |
| Step 3 | deck: null, empty, normal                | players: null, empty, 1, 2, 4, 5 | successful deal or throw exception                |

### Step 4:

##### All-combination or each-choice: each-choice

|              | System under test                       | Expected output                  | Implemented? |
|--------------|-----------------------------------------|----------------------------------|--------------|
| Test Case 1  | deck=null, players=2                    | InvalidDeckException             | Yes          |
| Test Case 2  | deck=empty, players=2                   | EmptyDeckException               | Yes          |
| Test Case 3  | deck=1 defuse card, players=2           | InsufficientDefuseCardsException | Yes          |
| Test Case 4  | deck=2 defuse cards, players=2          | successful deal                  | Yes          |
| Test Case 5  | deck=4 defuse cards, players=4          | successful deal                  | Yes          |
| Test Case 6  | deck=4 defuse cards, players=5          | TooManyPlayersException          | Yes          |
| Test Case 7  | deck=4 defuse cards, players=null       | InvalidPlayersListException      | Yes          |
| Test Case 8  | deck=4 defuse cards, players=empty list | EmptyPlayersListException        | Yes          |
| Test Case 9  | deck=4 defuse cards, players=1          | TooFewPlayersException           | Yes          |
| Test Case 10 | deck=no defuse cards, players=2         | NoDefuseCardsException           | Yes          |


