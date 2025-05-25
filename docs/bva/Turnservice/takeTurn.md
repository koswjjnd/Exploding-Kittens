# BVA Test Cases for takeTurn Method

## Method Signature

```java
void takeTurn(Player player, GameContext ctx)
```

## Parameters

1. player: Player

   - Valid: Non-null Player object
   - Invalid: null
   - Boundary: N/A

2. ctx: GameContext
   - Valid: Non-null GameContext object
   - Invalid: null
   - Boundary: N/A

## Test Cases

### TC1: Null Player

- Input: player = null, ctx = valid GameContext
- Expected: IllegalArgumentException
- Description: Should throw exception when player is null

### TC2: Null GameContext

- Input: player = valid Player, ctx = null
- Expected: IllegalArgumentException
- Description: Should throw exception when GameContext is null

### TC3: Valid Turn with No Cards

- Input: player with empty hand, valid GameContext
- Expected: Turn ends after draw phase
- Description: Player should only draw a card when hand is empty

### TC4: Valid Turn with Cards

- Input: player with cards in hand, valid GameContext
- Expected: Player can play cards, then draw
- Description: Normal turn flow with card playing and drawing

### TC5: Turn with Exploding Kitten

- Input: player draws ExplodingKitten card
- Expected: ExplodingKitten handling logic is triggered
- Description: Special case when drawn card is ExplodingKitten

### TC6: Turn with Attack Card Effect

- Input: player plays Attack card
- Expected: leftTurns is increased by 2
- Description: Verify Attack card effect on turn count

### TC7: Turn with Skip Card Effect

- Input: player plays Skip card
- Expected: Turn ends immediately
- Description: Verify Skip card effect on turn flow

### TC8: Turn with Multiple Cards

- Input: player plays multiple cards in sequence
- Expected: All cards are processed correctly
- Description: Verify multiple card playing in one turn

### TC9: Turn with Invalid Card Play

- Input: player attempts to play invalid card
- Expected: Card play is rejected
- Description: Verify invalid card play handling

### TC10: Turn with Empty Deck

- Input: player attempts to draw from empty deck
- Expected: Appropriate exception is thrown
- Description: Verify empty deck handling
