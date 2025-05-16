# BVA Analysis for PlayerService

## Method 1: `public Player createPlayer(String rawNickname)`

### Step 1-3 Results

|        | Input                                         | Output                                           |
| ------ | --------------------------------------------- | ------------------------------------------------ |
| Step 1 | rawNickname (player name)                     | Player object or throws InvalidNicknameException |
| Step 2 | String (object type)                          | Player (object type)                             |
| Step 3 | null, "", " ", "P", "Player1", "VeryLongName" | Player object or exception                       |

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

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: rawNickname represents the player's name
- Output: Player object if name is valid, throws InvalidNicknameException if invalid
- Valid name requirements:
  - Not null
  - Not empty
  - Not only whitespace
  - No specific length limit

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: String (object type)
- Output: Player (object type)

### Step 3: Select concrete values along the edges for the input and the output.

- Null value: null
- Empty string: ""
- Whitespace only: " "
- Minimum length: "P"
- Normal value: "Player1"
- Long value: "VeryLongName"

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Using each-choice strategy since we have a single input parameter
- Testing all boundary values and normal cases
- Testing both valid and invalid cases
- Testing null and empty string cases
- Testing whitespace-only case
- Testing minimum and normal length cases
