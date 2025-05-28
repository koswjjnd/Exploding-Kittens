# TimeRewindCard Boundary Value Analysis

## Overview

The TimeRewindCard allows a player to move the top 3 cards from the deck to the bottom, effectively avoiding potentially dangerous cards (like Exploding Kittens) temporarily.

## Boundary Values

### 1. Deck Size

- **Empty Deck (0 cards)**: Throws IllegalStateException
- **Single Card (1 card)**: Moves 1 card to bottom
- **Two Cards (2 cards)**: Moves 2 cards to bottom
- **Three Cards (3 cards)**: Moves all 3 cards to bottom
- **More Than Three Cards**: Moves exactly 3 cards to bottom

### 2. Card Types

- **Normal Cards**: Should work with any card type
- **Exploding Kitten**: Should work with Exploding Kitten cards
- **Defuse Card**: Should work with Defuse cards
- **Mixed Types**: Should work with any combination of card types

### 3. Edge Cases

- **Null Deck**: Throws IllegalArgumentException
- **Null Cards in Deck**: Should not occur, but handled by Deck class
- **Concurrent Access**: Should be handled by Deck's thread safety

## Test Cases

1. **Empty Deck**: Throws IllegalStateException
2. **Single Card**: Moves 1 card to bottom
3. **Two Cards**: Moves 2 cards to bottom
4. **Three Cards**: Moves all 3 cards to bottom
5. **More Than Three Cards**: Moves exactly 3 cards to bottom
6. **Null Deck**: Throws IllegalArgumentException

## Implementation Notes

- Should use Deck's moveTopToBottom method
- Should handle edge cases gracefully
- Should maintain deck's integrity
- Should not affect turn order or player's hand
