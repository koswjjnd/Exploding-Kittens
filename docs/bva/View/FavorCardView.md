# BVA Analysis for FavorCardView

## Method 1: ```public int promptTargetPlayer(List<Player> availablePlayers)```
### Step 1-3 Results
|        | Input | (if more to consider for input) | Output |
|--------|-------|---------------------------------|--------|
| Step 1 | availablePlayers: List<Player> | user input | int (player index) or Exception |
| Step 2 | List<Player> | String | int or Exception |
| Step 3 | - 1 player<br>- 2 players<br>- 3 players | - valid index<br>- invalid index<br>- non-numeric input | - 0 to size-1<br>- IllegalArgumentException |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | availablePlayers = 1 player, input = 0 | Returns 0 | Yes |
| Test Case 2  | availablePlayers = 2 players, input = 1 | Returns 1 | No |
| Test Case 3  | availablePlayers = 3 players, input = 2 | Returns 2 | No |
| Test Case 4  | availablePlayers = 2 players, input = 2 | Throws IllegalArgumentException | No |
| Test Case 5  | availablePlayers = 2 players, input = -1 | Throws IllegalArgumentException | No |
| Test Case 6  | availablePlayers = 2 players, input = "abc" | Throws IllegalArgumentException | No |

## Method 2: ```public int promptCardSelection(List<Card> cards)```
### Step 1-3 Results
|        | Input | (if more to consider for input) | Output |
|--------|-------|---------------------------------|--------|
| Step 1 | cards: List<Card> | user input | int (card index) or Exception |
| Step 2 | List<Card> | String | int or Exception |
| Step 3 | - 1 card<br>- 2 cards<br>- 4 cards | - valid index<br>- invalid index<br>- non-numeric input | - 0 to size-1<br>- IllegalArgumentException |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | cards = 1 card, input = 0 | Returns 0 | No |
| Test Case 2  | cards = 2 cards, input = 1 | Returns 1 | No |
| Test Case 3  | cards = 4 cards, input = 3 | Returns 3 | No |
| Test Case 4  | cards = 2 cards, input = 2 | Throws IllegalArgumentException | No |
| Test Case 5  | cards = 2 cards, input = -1 | Throws IllegalArgumentException | No |
| Test Case 6  | cards = 2 cards, input = "abc" | Throws IllegalArgumentException | No |

## Method 3: ```public void showError(String message)```
### Step 1-3 Results
|        | Input | (if more to consider for input) | Output |
|--------|-------|---------------------------------|--------|
| Step 1 | message: String | display | void |
| Step 2 | String | void | void |
| Step 3 | - empty string<br>- short message<br>- long message | - display | - void |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | message = "" | Displays empty message | No |
| Test Case 2  | message = "Error" | Displays "Error" | No |
| Test Case 3  | message = "Invalid player selection" | Displays full message | No |
| Test Case 4  | message = null | Displays "null" | No |

## Method 4: ```public void showSuccess(String message)```
### Step 1-3 Results
|        | Input | (if more to consider for input) | Output |
|--------|-------|---------------------------------|--------|
| Step 1 | message: String | display | void |
| Step 2 | String | void | void |
| Step 3 | - empty string<br>- short message<br>- long message | - display | - void |

### Step 4:
##### All-combination or each-choice: each-choice

|              | System under test | Expected output | Implemented? |
|--------------|-------------------|-----------------|--------------|
| Test Case 1  | message = "" | Displays empty message | No |
| Test Case 2  | message = "Success" | Displays "Success" | No |
| Test Case 3  | message = "Card successfully transferred" | Displays full message | No |
| Test Case 4  | message = null | Displays "null" | No |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
