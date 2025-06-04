# BVA Analysis for TacoCatCard

## Card Creation Tests
Method under test: TacoCatCard constructor

| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|-----------------|
| 1.1 | Valid card creation | new TacoCatCard() | Card with type TACOCAT |
| 1.2 | Card type verification | getCatType() | TACOCAT |
| 1.3 | Card base type verification | getType() | CAT_CARD |

## Card Effect Tests
Method under test: effect(List<Player> turnOrder, Deck gameDeck)

| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|-----------------|
| 2.1 | No cat cards | Empty hand | IllegalStateException |
| 2.2 | One cat card | One TacoCatCard | IllegalStateException |
| 2.3 | Two same type cards | Two TacoCatCards | CatCardEffect |
| 2.4 | Two different type cards | TacoCatCard + BeardCatCard | IllegalStateException |
| 2.5 | No other players | Single player | IllegalStateException |
| 2.6 | Dead target player | Dead player | IllegalStateException |
| 2.7 | Empty target hand | Target has no cards | IllegalStateException |
| 2.8 | No turns left | 0 turns | IllegalStateException |
| 2.9 | No input handler | null handler | IllegalStateException |

## Card Validation Tests
Method under test: validateTargetPlayer and validateCardIndex

| Test Case | Description | Input | Expected Output |
|-----------|-------------|-------|-----------------|
| 3.1 | Valid target player | Alive player | No exception |
| 3.2 | Invalid target player | null player | IllegalArgumentException |
| 3.3 | Valid card index | 0 | No exception |
| 3.4 | Invalid card index | -1 | IllegalArgumentException |
