# BVA Analysis for Player

## Method 1: `public void receiveCard(Card card)`

### Step 1-3 Results

|        | Input                              | Output                           |
| ------ | ---------------------------------- | -------------------------------- |
| Step 1 | Card object                        | void (adds card to hand)         |
| Step 2 | Card (object)                      | void                             |
| Step 3 | Input boundaries: null, valid Card | Output: hand size increases by 1 |

### Step 4:

##### Strategy: each-choice (testing different card scenarios)

|             | System under test                | Expected output             | Implemented? |
| ----------- | -------------------------------- | --------------------------- | ------------ |
| Test Case 1 | Receive null card                | IllegalArgumentException    | No           |
| Test Case 2 | Receive one card                 | hand size = 1               | No           |
| Test Case 3 | Receive multiple cards           | hand size = number of cards | No           |
| Test Case 4 | Receive same card twice          | hand size = 2               | No           |
| Test Case 5 | Receive different types of cards | hand size = number of cards | No           |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

- Input: Card object to be added to player's hand
- Output: void (modifies internal state)
- Domain: Adds a card to the player's hand collection

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

- Input: Card (object)
- Output: void
- Internal state: List<Card> (player's hand)

### Step 3: Select concrete values along the edges for the input and the output.

- Input boundaries:
  - null (invalid input)
  - Single valid card
  - Multiple valid cards
  - Same card multiple times
  - Different types of cards
- Output boundaries:
  - Hand size increases by 1 for each card
  - Hand contains all received cards

### Step 4: Determine the test cases using either all-combination or each-choice strategy.

- Strategy: each-choice
- Rationale: Testing different card scenarios and edge cases
- Test cases cover:
  1. Invalid input (null)
  2. Single card addition
  3. Multiple card additions
  4. Duplicate card addition
  5. Different card types addition
