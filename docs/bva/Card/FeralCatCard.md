# Boundary Value Analysis for Feral Cat Card

## Overview
The Feral Cat card is a special cat card that can be paired with any other cat card to steal a card from another player. This document outlines the boundary value analysis for testing the Feral Cat card's functionality.

## Test Cases

### 1. Card Pairing
| Test Case | Input | Expected Result |
|-----------|-------|-----------------|
| TC1.1 | Empty hand | Throws IllegalStateException |
| TC1.2 | Only Feral Cat cards | Throws IllegalStateException |
| TC1.3 | Only other cat cards | Throws IllegalStateException |
| TC1.4 | One Feral Cat + One other cat | Successfully pairs |
| TC1.5 | Multiple Feral Cats + One other cat | Successfully pairs with first valid combination |

### 2. Target Player Selection
| Test Case | Input | Expected Result |
|-----------|-------|-----------------|
| TC2.1 | No other players | Throws IllegalStateException |
| TC2.2 | One other player | Successfully selects target |
| TC2.3 | Multiple players | Successfully selects target from available players |

### 3. Card Stealing
| Test Case | Input | Expected Result |
|-----------|-------|-----------------|
| TC3.1 | Target player has empty hand | Throws IllegalStateException |
| TC3.2 | Target player has one card | Successfully steals the card |
| TC3.3 | Target player has multiple cards | Successfully steals selected card |

### 4. Turn Validation
| Test Case | Input | Expected Result |
|-----------|-------|-----------------|
| TC4.1 | Current player has 0 turns left | Throws IllegalStateException |
| TC4.2 | Current player has 1 turn left | Successfully executes effect |
| TC4.3 | Current player has multiple turns left | Successfully executes effect |

## Test Implementation Notes
1. All test cases should be implemented using JUnit 5
2. Mock objects should be used to simulate player hands and game state
3. Each test case should verify both successful scenarios and error conditions
4. Input validation should be tested for all user inputs
5. Card effect execution should be verified for all valid scenarios

## Edge Cases
1. Player attempts to steal from themselves
2. Player attempts to use Feral Cat without a valid cat card pair
3. Player attempts to steal a card from an invalid index
4. Player attempts to use Feral Cat when it's not their turn
5. Player attempts to use Feral Cat with invalid input handler

## Test Coverage
The test suite should achieve:
- 100% method coverage
- 100% branch coverage
- 100% statement coverage
- All boundary conditions tested
- All error conditions tested 