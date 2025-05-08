# BVA Analysis for deck

## Method 1: ```public TYPE dealDefuses```
### Step 1-3 Results
|        | Input                          | (if more to consider for input) | Output |
| ------ | ------------------------------ | ------------------------------- | ------ |
| Step 1 | players                        |                                 | None   |
| Step 2 | List<Player> players           |                                 | None   |
| Step 3 | size of players =0, 1, 2, 3, 4 |                                 | None   |
### Step 4:
##### All-combination or each-choice: YOUR-DECISION

|             | System under test            | Expected output                       | Implemented? |
| ----------- | ---------------------------- | ------------------------------------- | ------------ |
| Test Case 1 | dealDefuses(players size =0) | Should throw IllegalArgumentException |              |
| Test Case 2 | dealDefuses(players size =1) | player.getHand() return a card list containg only one defuse card                    |              |
| Test Case 3 | dealDefuses(players size =4) | each player.getHand() return a card list containg only one defuse card                     |              |
| Test Case 4 | dealDefuses(players size =5) | Should throw IllegalArgumentException |              |