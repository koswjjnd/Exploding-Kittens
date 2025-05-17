# BVA Analysis for dealService

## Method 1: ```dealDefuses(deck, players)```

### Step 1-3 Results

|        | Input                                    | (if more to consider for input)  | Output                                            |
|--------|------------------------------------------|----------------------------------|---------------------------------------------------|
| Step 1 | deck: Deck object, players: List<Player> | -                                | each player gets one defuse card                  |
| Step 2 | deck: Object, players: Collection        | -                                | number of defuse cards in each player's hand: int |
| Step 3 | deck: null, empty, normal                | players: null, empty, 1, 2, 4, 5 | successful deal or throw exception                |

### Step 4:

##### All-combination or each-choice: each-choice

|              | System under test                       | Expected output                  | Implemented? |
|--------------|-----------------------------------------|----------------------------------|--------------|
| Test Case 1  | deck=null, players=2                    | InvalidDeckException             | Yes          |
| Test Case 2  | deck=empty, players=2                   | EmptyDeckException               | Yes          |
| Test Case 3  | deck=normal, players=2                  | successful deal                  | Yes          |
| Test Case 4  | deck=normal, players=5                  | TooManyPlayersException          | Yes          |
| Test Case 5  | deck=normal, players=1                  | TooFewPlayersException           | Yes          |

## Method 2: ```dealInitialHands(deck, players)```

### Step 1-3 Results

|        | Input                                    | (if more to consider for input)  | Output                                     |
|--------|------------------------------------------|----------------------------------|--------------------------------------------|
| Step 1 | deck: Deck object, players: List<Player> | number of cards in deck          | each player gets 4 cards                   |
| Step 2 | deck: Object, players: Collection        | number of cards: int             | number of cards in each player's hand: int |
| Step 3 | deck: null, empty, normal                | players: null, empty, 1, 2, 4, 5 | successful deal or throw exception         |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test        | Expected output         | Implemented? |
|-------------|--------------------------|-------------------------|--------------|
| Test Case 1 | deck=null, players=2     | InvalidDeckException    | Yes          |
| Test Case 2 | deck=empty, players=2    | EmptyDeckException      | Yes          |
| Test Case 3 | deck=8 cards, players=2  | successful deal         | Yes          |
| Test Case 4 | deck=20 cards, players=5 | TooManyPlayersException | Yes          |
| Test Case 5 | deck=20 cards, players=1 | TooFewPlayersException  | Yes          |


