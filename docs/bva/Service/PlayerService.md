# BVA Analysis for PlayerService

## Method 1: `public void validateCount(int count)`

### Step 1-3 Results

|        | Input                | (if more to consider for input) | Output                                        |
| ------ | -------------------- | ------------------------------- | --------------------------------------------- |
| Step 1 | player count (count) |                                 | nothing (valid) or throws exception (invalid) |
| Step 2 | Count (integer)      |                                 | Cases: Void (no return), exception            |
| Step 3 | 1, 2, 3, 4, 5        |                                 | Void, InvalidPlayerCountException             |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test | Expected output                    | Implemented? |
| ----------- | ----------------- | ---------------------------------- | ------------ |
| Test Case 1 | validateCount(1)  | throws InvalidPlayerCountException | Yes          |
| Test Case 2 | validateCount(2)  | completes normally                 | Yes          |
| Test Case 3 | validateCount(3)  | completes normally                 | Yes          |
| Test Case 4 | validateCount(4)  | completes normally                 | Yes          |
| Test Case 5 | validateCount(5)  | throws InvalidPlayerCountException | Yes          |

## Method 2: `public Player createPlayer(String rawNickname)`

### Step 1-3 Results

|        | Input                                         | Output                                           |
| ------ | --------------------------------------------- | ------------------------------------------------ |
| Step 1 | rawNickname (player name)                     | Player object or throws InvalidNicknameException |
| Step 2 | String (object type)                          | Cases: Player (object type), exception           |
| Step 3 | null, "", " ", "P", "Player1", "VeryLongName" | Player object, InvalidNicknameException          |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test            | Expected output                    | Implemented? |
| ----------- | ---------------------------- | ---------------------------------- | ------------ |
| Test Case 1 | rawNickname = null           | throws InvalidNicknameException    | Yes          |
| Test Case 2 | rawNickname = ""             | throws InvalidNicknameException    | Yes          |
| Test Case 3 | rawNickname = " "            | throws InvalidNicknameException    | Yes          |
| Test Case 4 | rawNickname = "P"            | returns Player with name "P"       | Yes          |
| Test Case 5 | rawNickname = "Player1"      | returns Player with name "Player1" | Yes          |
| Test Case 6 | rawNickname = "VeryLongName" | returns Player with same name      | Yes          |
