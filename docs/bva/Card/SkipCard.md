# BVA Analysis for SkipCard

## Method: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input (leftTurn) | Output (turnOrder change) |
|--------|------------------|---------------------------|
| Step 1 | leftTurn = 0     | Throws IllegalStateException |
| Step 2 | leftTurn = 1     | leftTurn becomes 0, player moves to end |
| Step 3 | leftTurn = 2     | leftTurn becomes 1, player stays at front |

### Step 4:
##### Strategy: each-choice (since we're testing a single input parameter with clear boundaries)

| Test Case | System under test | Expected output | Implemented? |
|-----------|-------------------|-----------------|--------------|
| Test Case 1 | leftTurn = 0 | Throws IllegalStateException | Yes |
| Test Case 2 | leftTurn = 1 | leftTurn becomes 0, player moves to end | Yes |
| Test Case 3 | leftTurn = 2 | leftTurn becomes 1, player stays at front | Yes |
| Test Case 4 | leftTurn = 3 | leftTurn becomes 2, player stays at front | Yes |
| Test Case 5 | leftTurn = -1 | Throws IllegalStateException | Yes |
| Test Case 6 | leftTurn = Integer.MAX_VALUE | leftTurn becomes Integer.MAX_VALUE - 1, player stays at front | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- Input: Player's leftTurn value (number of turns remaining)
- Output: 
  - Exception (when leftTurn <= 0)
  - Changes to turnOrder list and leftTurn value (when leftTurn > 0)

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- Input: Integer (leftTurn)
- Output: 
  - Exception (IllegalStateException)
  - Boolean (whether turnOrder changed)
  - Integer (new leftTurn value)

### Step 3: Select concrete values along the edges for the input and the output.
- Input boundaries:
  - Minimum: 0 or negative (throws exception)
  - Normal: 1-2 (one or two turns left)
  - Maximum: 3 or more (multiple turns left)
- Output boundaries:
  - Exception: IllegalStateException
  - turnOrder change: true/false
  - leftTurn change: decrease by 1 or no change

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy since we're testing a single input parameter
- Test cases cover all boundary conditions and typical scenarios 