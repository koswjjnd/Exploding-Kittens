# BVA Analysis for Watermelon Cat Card

## Method Description

The `effect()` method in `WatermelonCatCard` allows a player to steal any card from another player's hand by using two watermelon Cat cards.

## Parameters

- `turnOrder`: List<Player> - The list of players in turn order
- `gameDeck`: Deck - The game deck (not used in this effect)

## Preconditions

1. The input handler must be set
2. The current player must have turns left
3. The current player must have exactly two watermelon Cat cards
4. There must be at least one valid target player with cards in their hand

## Boundary Values

### Input Handler

- **Valid**: Input handler is set
- **Invalid**: Input handler is null

### Player Turns

- **Valid**: Current player has at least 1 turn left
- **Invalid**: Current player has 0 turns left

### watermelon Cat Cards

- **Valid**: Current player has exactly 2 watermelon Cat cards
- **Invalid Cases**:
  - Current player has 0 watermelon Cat cards
  - Current player has 1 watermelon Cat cards
  - 
### Target Players

- **Valid**: At least one other player has cards in their hand
- **Invalid**: No other players have cards in their hand

### Card Index Selection

- **Valid**: Selected index is between 0 and (target player's hand size - 1)
- **Invalid Cases**:
  - Selected index is -1
  - Selected index equals target player's hand size
  - Selected index is greater than target player's hand size

## Expected Behavior

1. When all preconditions are met:
   - The method should throw a `CatCardEffect` exception containing:
     - The two watermelon Cat cards used
     - The target player's name
     - The selected card index
2. When any precondition is not met:
   - The method should throw an appropriate exception:
     - `IllegalStateException` for missing input handler, no turns left, or no valid targets
     - `IllegalArgumentException` for invalid card index selection

## Test Cases

1. Test with null input handler
2. Test with 0 turns left
3. Test with no watermelon Cat cards
4. Test with one watermelon Cat cards
5. Test with two watermelon Cat cards (valid case)
6. Test with invalid card index selection
7. Test with no target players having cards
