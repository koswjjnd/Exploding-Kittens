# BVA Analysis for GameSetupController

## Method 1: ```public void setupGame()```

### Step 1-3 Results

|        | Input                      | (if more to consider for input) | Output                      |
|--------|----------------------------|---------------------------------|-----------------------------|
| Step 1 | Interactive inputs:        | Player count (2-4)              | void (completes game setup) |
|        | - Player count             | Nicknames (non-empty strings)   | Game state updated          |
|        | - Player nicknames         |                                 |                             |
| Step 2 | - count: int               | Valid ranges:                   | Game state updated          |
|        | - nicknames: String[]      | - count: [2,4]                  |                             |
|        |                            | - nicknames: non-empty strings  |                             |
| Step 3 | Edge cases:                | Invalid inputs:                 | Game setup completed or     |
|        | - count: 1, 2, 4, 5        | - count: 1, 5, 0, -1            | exception thrown            |
|        | - nicknames: "", " ", null | - nicknames: "", " ", null      |                             |

### Step 4:

##### All-combination or each-choice: each-choice

|              | System under test                    | Expected output                      | Implemented? |
|--------------|--------------------------------------|--------------------------------------|--------------|
| Test Case 1  | setupGame()                          | Successfully completes game setup    | Yes          |
|              | count=2                              |                                      |              |
|              | nicknames=["P1","P2"]                |                                      |              |
| Test Case 2  | setupGame()                          | Successfully completes game setup    | Yes          |
|              | count=3                              |                                      |              |
|              | nicknames=["P1","P2","P3"]           |                                      |              |
| Test Case 3  | setupGame()                          | Successfully completes game setup    | Yes          |
|              | count=4                              |                                      |              |
|              | nicknames=["P1","P2","P3","P4"]      |                                      |              |
| Test Case 4  | setupGame()                          | Throws InvalidPlayerCountException   | Yes          |
|              | count=1                              | when count < 2                       |              |
|              | nicknames=["P1"]                     |                                      |              |
| Test Case 5  | setupGame()                          | Throws InvalidPlayerCountException   | Yes          |
|              | count=5                              | when count > 4                       |              |
|              | nicknames=["P1","P2","P3","P4","P5"] |                                      |              |
| Test Case 6  | setupGame()                          | Throws InvalidNicknameException      | Yes          |
|              | count=2                              | when nickname is empty               |              |
|              | nicknames=["P1",""]                  |                                      |              |
| Test Case 7  | setupGame()                          | Throws InvalidNicknameException      | Yes          |
|              | count=2                              | when nickname is whitespace          |              |
|              | nicknames=["P1"," "]                 |                                      |              |
| Test Case 8  | setupGame()                          | Throws InvalidNicknameException      | Yes          |
|              | count=2                              | when nickname is null                |              |
|              | nicknames=["P1",null]                |                                      |              |
| Test Case 9  | setupGame()                          | Throws InvalidDeckException          | Yes          |
|              | count=2                              | when deck preparation fails          |              |
|              | nicknames=["P1","P2"]                |                                      |              |
| Test Case 10 | setupGame()                          | Throws IllegalArgumentException      | Yes          |
|              | count=2                              | when turn order initialization fails |              |
|              | nicknames=["P1","P2"]                |                                      |              |