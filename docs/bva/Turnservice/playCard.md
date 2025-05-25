# BVA Test Cases for playCard Method

## Method Signature

```java
void playCard(Player player, Card card, GameContext ctx) throws InvalidCardException
```

## Parameters

1. player: Player

   - Valid: Non-null Player object
   - Invalid: null
   - Boundary: N/A

2. card: Card

   - Valid: Non-null Card object
   - Invalid: null
   - Boundary: N/A

3. ctx: GameContext
   - Valid: Non-null GameContext object
   - Invalid: null
   - Boundary: N/A

## Test Cases

### TC1: Null Player

- Input: player = null, card = valid Card, ctx = valid GameContext
- Expected: IllegalArgumentException
- Description: Should throw exception when player is null

### TC2: Null Card

- Input: player = valid Player, card = null, ctx = valid GameContext
- Expected: IllegalArgumentException
- Description: Should throw exception when card is null

### TC3: Null GameContext

- Input: player = valid Player, card = valid Card, ctx = null
- Expected: IllegalArgumentException
- Description: Should throw exception when GameContext is null

### TC4: Valid Card Play

- Input: player with card in hand, valid card, valid GameContext
- Expected:
  - Card is shown as played
  - Card effect is executed
  - Card is removed from hand
- Description: Normal card play flow

### TC5: Card Noped

- Input: player with card in hand, valid card, valid GameContext, Nope card played
- Expected:
  - Card is shown as played
  - Card is shown as noped
  - Card effect is not executed
  - Card is removed from hand
- Description: Card play is cancelled by Nope card

### TC6: Invalid Card

- Input: player with card in hand, invalid card, valid GameContext
- Expected: RuntimeException
- Description: Should throw exception when card effect cannot be executed

## Test Coverage

The test cases cover:

1. Input validation (null checks)
2. Normal card play flow
3. Card nope interaction
4. Error handling for invalid cards
5. Card removal from hand in all cases

## Notes

- Card is always removed from hand after being played, regardless of whether it was noped or not
- Card effect is only executed if the card is not noped
- Invalid card plays result in an exception being thrown
