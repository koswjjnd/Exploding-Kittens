# BVA Analysis for CardEffectService

## Method 1: ```public void handleCardEffect(Card card, GameContext ctx)```

### Step 1-3 Results

|        | Input                                                          | (if more to consider for input) | Output |
|--------|----------------------------------------------------------------|---------------------------------|--------|
| Step 1 | card: Card type                                                | ctx: GameContext type           | void   |
| Step 2 | card: Card enum type                                           | ctx: GameContext object         | void   |
| Step 3 | card: null, ATTACK, SKIP, SEE_THE_FUTURE, SHUFFLE, CAT, DEFUSE | ctx: null, valid GameContext    | void   |

### Step 4:

##### Using each-choice strategy as there is no combination relationship between input parameters

|             | System under test                              | Expected output                    | Implemented? |
|-------------|------------------------------------------------|------------------------------------|--------------|
| Test Case 1 | card = null, ctx = null                        | IllegalArgumentException           |              |
| Test Case 2 | card = null, ctx = valid GameContext           | IllegalArgumentException           |              |
| Test Case 3 | card = ATTACK, ctx = null                      | IllegalArgumentException           |              |
| Test Case 4 | card = ATTACK, ctx = valid GameContext         | Execute attack effect normally     |              |
| Test Case 5 | card = SKIP, ctx = valid GameContext           | Execute skip effect normally       |              |
| Test Case 6 | card = SEE_THE_FUTURE, ctx = valid GameContext | Execute see future effect normally |              |
| Test Case 7 | card = SHUFFLE, ctx = valid GameContext        | Execute shuffle effect normally    |              |
| Test Case 8 | card = CAT, ctx = valid GameContext            | Execute cat card effect normally   |              |
| Test Case 9 | card = DEFUSE, ctx = valid GameContext         | Execute defuse effect normally     |              |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain

- Input 1: Card type, representing the card to be processed (excluding Nope card)
- Input 2: GameContext type, representing the game context
- Output: void, method executes card effect

### Step 2: Choose the data type for the input and the output from the BVA Catalog

- Card: enum type (CardType, excluding NOPE)
- GameContext: object type
- Output: void

### Step 3: Select concrete values along the edges for the input and the output

- Card: null, various card types (excluding NOPE)
- GameContext: null, valid GameContext object

### Step 4: Determine the test cases using each-choice strategy

- Test null inputs
- Test each card type (excluding NOPE)
- Test invalid GameContext
