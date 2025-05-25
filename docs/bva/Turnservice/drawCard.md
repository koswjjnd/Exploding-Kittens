# BVA Test Cases for drawCard Method

## Method Signature

```java
void drawCard(Player player, GameContext ctx) throws EmptyDeckException
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

### TC3: Normal Card Draw

- Input: player = valid Player, ctx = valid GameContext, deck has cards
- Expected:
  - Card is drawn from deck
  - Card is shown as drawn
  - Card is added to player's hand
- Description: Normal card draw flow

### TC4: Empty Deck

- Input: player = valid Player, ctx = valid GameContext, deck is empty
- Expected: EmptyDeckException
- Description: Should throw exception when trying to draw from empty deck

### TC5: Draw Exploding Kitten with Defuse

- Input:
  - player = valid Player with defuse card
  - ctx = valid GameContext
  - drawn card = ExplodingKittenCard
  - player chooses to use defuse
- Expected:
  - Card is drawn and shown
  - Player uses defuse card
  - Exploding Kitten is inserted back into deck at chosen position
- Description: Player successfully defuses Exploding Kitten

### TC6: Draw Exploding Kitten without Defuse

- Input:
  - player = valid Player without defuse card
  - ctx = valid GameContext
  - drawn card = ExplodingKittenCard
- Expected:
  - Card is drawn and shown
  - Player is marked as not alive
- Description: Player dies from Exploding Kitten

### TC7: Draw Exploding Kitten with Defuse but Refuses

- Input:
  - player = valid Player with defuse card
  - ctx = valid GameContext
  - drawn card = ExplodingKittenCard
  - player chooses not to use defuse
- Expected:
  - Card is drawn and shown
  - Player is marked as not alive
- Description: Player chooses not to defuse Exploding Kitten

## Test Coverage

The test cases cover:

1. Input validation (null checks)
2. Normal card draw flow
3. Empty deck handling
4. Exploding Kitten scenarios:
   - With defuse and using it
   - Without defuse
   - With defuse but refusing to use it
5. Card addition to player's hand
6. Player state changes (alive/dead)

## Notes

- Card is always shown to player after being drawn
- Exploding Kitten handling depends on player's defuse card status and choice
- Player's alive status is only changed when drawing Exploding Kitten
- Empty deck check is performed before any card operations
