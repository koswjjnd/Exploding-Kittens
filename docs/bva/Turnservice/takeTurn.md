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
- Expected: Player can play cards until choosing to end turn
- Description: Normal turn flow with optional card playing and drawing

### TC5: Turn with Exploding Kitten

- Input: player draws ExplodingKitten card
- Expected: ExplodingKitten handling logic is triggered
- Description: Special case when drawn card is ExplodingKitten

### TC6: Turn with Nope Card

- Input: Another player plays Nope card
- Expected: Card effect is cancelled
- Description: Verify Nope card interaction

### TC7: Turn with Invalid Card Play

- Input: player attempts to play invalid card
- Expected: Card play is rejected, error message shown
- Description: Verify invalid card play handling with error feedback

### TC8: Turn with Empty Deck

- Input: player attempts to draw from empty deck
- Expected: EmptyDeckException is thrown
- Description: Verify empty deck handling

### TC9: Turn with Defuse Card

- Input: player draws ExplodingKitten and has Defuse card
- Expected: Player can choose to use Defuse card
- Description: Verify Defuse card interaction with ExplodingKitten

### TC10: Turn with Multiple Card Plays

- Input: player plays multiple cards in sequence
- Expected: Each card is processed with Nope check
- Description: Verify multiple card playing with Nope interaction
