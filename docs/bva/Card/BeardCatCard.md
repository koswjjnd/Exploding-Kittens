# BVA Analysis for BeardCatCard

## Method 1: ```public BeardCatCard()```
### Step 1-3 Results
|        | Input | Output |
|--------|-------|--------|
| Step 1 | No input parameters | BeardCatCard object with CatType.BEARD_CAT |
| Step 2 | No input parameters | BeardCatCard object with CardType.CAT_CARD |
| Step 3 | No input parameters | BeardCatCard object with all default values |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | Create new BeardCatCard() | BeardCatCard object with CatType.BEARD_CAT | Yes |
| Test Case 2  | Call getType() on BeardCatCard | CAT_CARD | Yes |

## Method 2: ```public void effect(List<Player> turnOrder, Deck gameDeck)```
### Step 1-3 Results
|        | Input | Output |
|--------|-------|--------|
| Step 1 | Two BeardCatCards | Throws CatCardEffect |
| Step 2 | One BeardCatCard + One RainbowCatCard | Throws IllegalStateException |
| Step 3 | Three BeardCatCards | Throws IllegalStateException |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | Two BeardCatCards | Throws CatCardEffect | Yes |
| Test Case 2  | One BeardCatCard + One RainbowCatCard | Throws IllegalStateException | Yes |
| Test Case 3  | Three BeardCatCards | Throws IllegalStateException | Yes |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
- Input: 
  - No input parameters for constructor
  - turnOrder list and gameDeck for effect method (inherited from CatCard)
- Output: 
  - BeardCatCard object (constructor)
  - void (effect method, inherited from CatCard)

### Step 2: Choose the data type for the input and the output from the BVA Catalog.
- Input: 
  - None (constructor)
  - List<Player> and Deck (effect method, inherited from CatCard)
- Output: 
  - BeardCatCard object
  - void (effect method, inherited from CatCard)

### Step 3: Select concrete values along the edges for the input and the output.
- Input boundaries:
  - BeardCatCard combinations: 0, 1, 2, 3
  - Mixed cat card combinations: BeardCatCard + other cat types
- Output boundaries:
  - Constructor: Creates valid BeardCatCard object with correct type
  - effect(): Inherits behavior from CatCard with BeardCatCard-specific validation

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
- Using each-choice strategy for both methods
- Test cases focus on BeardCatCard-specific scenarios
- Common scenarios (input handler, turns, target validation) are inherited from CatCard 