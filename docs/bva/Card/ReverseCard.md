# ReverseCard Boundary Value Analysis

## Overview

The ReverseCard reverses the turn order and ends the current player's turn without drawing a card. If the player was attacked, only 1 of 2 turns is ended.

## Boundary Values

### 1. Turn Order

- **Null Turn Order**: Throws IllegalArgumentException
- **Empty Turn Order**: Throws IllegalArgumentException
- **Single Player**: Reverses order (no change) and ends turn
- **Multiple Players**: Reverses order correctly

### 2. Player Turns

- **Normal Turn (leftTurns = 1)**: Ends turn completely (setLeftTurns(0))
- **Attacked Turn (leftTurns > 1)**: Ends only 1 turn (decrementLeftTurns)

### 3. Edge Cases

- **Player with 0 leftTurns**: Should not occur, but handled by else clause
- **Player with negative leftTurns**: Should not occur, but handled by else clause

## Test Cases

1. **Null Turn Order**: Throws IllegalArgumentException
2. **Empty Turn Order**: Throws IllegalArgumentException
3. **Single Player**: Reverses order (no change) and ends turn
4. **Multiple Players**: Reverses order correctly
5. **Normal Turn**: Ends turn completely
6. **Attacked Turn**: Ends only 1 turn
