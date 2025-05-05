# BVA Analysis for GameContext

## Method 1: `public TYPE METHOD(TYPE)`

### Step 1-3 Results

|        | Input                                        | (if more to consider for input)          | Output                     |
| ------ | -------------------------------------------- | ---------------------------------------- | -------------------------- |
| Step 1 | List<Player> order                           | Can be null, empty, 1+, contain null     | void (sets internal state) |
| Step 2 | List of object references                    | Input length (0–N), may contain nulls    | Stored global state        |
| Step 3 | null, [], [p1], [p1, p2], [null], [p1, null] | GameContext.getTurnOrder() matches input |                            |

### Step 4:

##### All-combination or each-choice: YOUR-DECISION

|             | System under test            | Expected output                          | Implemented? |
| ----------- | ---------------------------- | ---------------------------------------- | ------------ |
| Test Case 1 | setTurnOrder(null)           | Throws IllegalArgumentException          | Yes          |
| Test Case 2 | setTurnOrder([])             | Stored list is empty                     | Yes          |
| Test Case 3 | setTurnOrder([p1])           | Stored list contains p1                  | Yes          |
| Test Case 4 | setTurnOrder([p1, p2])       | Stored list matches input                | Yes          |
| Test Case 5 | setTurnOrder([p2]) after TC3 | Stored list updated to only contain p2   | Yes          |
| Test Case 6 | setTurnOrder([null])         | Stored list contains null (not rejected) | Yes          |
| Test Case 7 |                              |                                          |              |


