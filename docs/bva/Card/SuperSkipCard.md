# BVA Analysis for SuperSkipCard

## Method: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input (turnOrder size) | Output (turnOrder change) |
|--------|----------------------|---------------------------|
| Step 1 | size = 1             | No change in turnOrder    |
| Step 2 | size = 2             | Player moves to end       |
| Step 3 | size = 3             | Player moves to end       |

### Step 4:
##### Strategy: each-choice (since we're testing a single input parameter with clear boundaries)

| Test Case | System under test | Expected output | Implemented? |
|-----------|-------------------|-----------------|--------------|
| Test Case 1 | turnOrder size = 1 | No change in turnOrder | Yes |
| Test Case 2 | turnOrder size = 2 | Player moves to end | Yes |
| Test Case 3 | turnOrder size = 3 | Player moves to end | Yes |
| Test Case 4 | turnOrder size = 0 | Throw IndexOutOfBoundsException | Yes |
| Test Case 5 | turnOrder size = Integer.MAX_VALUE | Player moves to end | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- Input: turnOrder list size (number of players in the game)
- Output: Changes to turnOrder list (player position)

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- Input: Integer (turnOrder size)
- Output: 
  - Boolean (whether turnOrder changed)
  - List<Player> (modified turnOrder)

### Step 3: Select concrete values along the edges for the input and the output.
- Input boundaries:
  - Minimum: 1 (single player)
  - Normal: 2 (two players)
  - Maximum: 3 or more (multiple players)
- Output boundaries:
  - turnOrder change: true/false
  - player position: front/end

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy since we're testing a single input parameter
- Test cases cover all boundary conditions and typical scenarios 