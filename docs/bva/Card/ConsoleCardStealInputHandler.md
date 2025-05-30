# BVA Analysis for ConsoleCardStealInputHandler

## Method 1: ```public Player selectTargetPlayer(List<Player> availablePlayers)```
### Step 1-3 Results
|        | Input (availablePlayers) | Output |
|--------|-------------------------|--------|
| Step 1 | List of players         | Player |
| Step 2 | List<Player>            | Player |
| Step 3 | null, empty, [1], [2], [3], [>3] | Player or Exception |

### Step 4:
##### Each-choice strategy

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | Select first player | First player | Yes |
| Test Case 2  | Select last player | Last player | Yes |
| Test Case 3  | Select middle player | Middle player | Yes |
| Test Case 4  | Invalid player selection | IllegalArgumentException | Yes |
| Test Case 5  | Negative player selection | IllegalArgumentException | Yes |
| Test Case 6  | Empty player list | IllegalArgumentException | Yes |
| Test Case 7  | Null player list | IllegalArgumentException | Yes |

## Method 2: ```public int selectCardIndex(int handSize)```
### Step 1-3 Results
|        | Input (handSize) | Output |
|--------|-----------------|--------|
| Step 1 | Hand size       | int    |
| Step 2 | int             | int    |
| Step 3 | 0, 1, 2, 3, >3  | int or Exception |

### Step 4:
##### Each-choice strategy

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | Select first card | 0 | Yes |
| Test Case 2  | Select last card | handSize-1 | Yes |
| Test Case 3  | Select middle card | middle index | Yes |
| Test Case 4  | Invalid card selection | IllegalArgumentException | Yes |
| Test Case 5  | Negative card selection | IllegalArgumentException | Yes |
| Test Case 6  | Invalid hand size | IllegalArgumentException | Yes |
| Test Case 7  | Negative hand size | IllegalArgumentException | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- selectTargetPlayer method:
  - Input: List of available players
  - Output: Selected player
- selectCardIndex method:
  - Input: Size of player's hand
  - Output: Index of selected card

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- selectTargetPlayer method:
  - Input: List<Player>
  - Output: Player
- selectCardIndex method:
  - Input: int
  - Output: int

### Step 3: Select concrete values along the edges for the input and the output.
- selectTargetPlayer method:
  - availablePlayers: null, empty, [1], [2], [3], [>3]
  - Output: Player or Exception
- selectCardIndex method:
  - handSize: 0, 1, 2, 3, >3
  - Output: int or Exception

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy because:
  1. Input parameters have clear boundary values
  2. Test cases cover all key scenarios
  3. Each-choice strategy provides sufficient coverage 