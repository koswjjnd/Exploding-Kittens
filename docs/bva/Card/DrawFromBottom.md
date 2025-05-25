# Draw From Bottom Card - BVA (Boundary Value Analysis)

## Card Description

End your turn by drawing the bottom card from the Draw Pile.

## Mechanics

- If you play a Draw From Bottom as a defense to an Attack Card, it only ends 1 of the 2 turns. 2 Draw From Bottom Cards would end both turns.

---

## Boundary Value Analysis

| Scenario                                   | Input/State                       | Expected Behavior                             |
| ------------------------------------------ | --------------------------------- | --------------------------------------------- |
| Draw pile is empty                         | deck.size() == 0                  | Throw exception or handle as per game rules   |
| Draw pile has 1 card                       | deck.size() == 1                  | Player draws the only card                    |
| Draw pile has multiple cards               | deck.size() > 1                   | Player draws the bottom card                  |
| Player hand is empty                       | player.hand.size() == 0           | Player receives card as normal                |
| Player hand is full (if hand limit exists) | player.hand.size() == hand limit  | Handle as per hand limit rules                |
| Played as normal card                      | Not during Attack                 | End turn, draw from bottom                    |
| Played as defense to Attack (1 left turn)  | Under Attack, 1 Draw From Bottom  | Only 1 attack turn is ended                   |
| Played as defense to Attack (2 left turns) | Under Attack, 2 Draw From Bottom  | Both attack turns are ended                   |
| Multiple Draw From Bottom in a row         | Player plays multiple in sequence | Each ends 1 turn, draws from bottom each time |
| Drawn card is Exploding Kitten             | Bottom card is Exploding Kitten   | Handle as per Exploding Kitten rules          |
| Drawn card is Defuse                       | Bottom card is Defuse             | Player receives Defuse card                   |
