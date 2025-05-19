# BVA Analysis for CardEffectService

## Method 1: ```public void handleCardEffect(Card card, GameContext ctx)```

### Step 1-3 Results

|        | Input                                                          | (if more to consider for input) | Output |
|--------|----------------------------------------------------------------|---------------------------------|--------|
| Step 1 | card: Card type                                                | ctx: GameContext type           | void   |
| Step 2 | card: Card enum type                                           | ctx: GameContext object         | void   |
| Step 3 | card: null, ATTACK, SKIP, SEE_THE_FUTURE, SHUFFLE, CAT, DEFUSE | ctx: null, valid GameContext    | void   |

### Step 4:

##### Focus on dispatching and exceptions, not card effect details

|             | System under test                          | Expected output              | Implemented? |
|-------------|--------------------------------------------|------------------------------|--------------|
| Test Case 1 | card = null, ctx = null                    | IllegalArgumentException     |              |
| Test Case 2 | card = null, ctx = valid GameContext       | IllegalArgumentException     |              |
| Test Case 3 | card = valid Card, ctx = null              | IllegalArgumentException     |              |
| Test Case 4 | card = valid Card, ctx = valid GameContext | card.effect() is called once |              |

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

- Card: null, valid Card object
- GameContext: null, valid GameContext object

### Step 4: Determine the test cases using each-choice strategy

- Test null inputs
- Test valid inputs
- Test invalid GameContext
