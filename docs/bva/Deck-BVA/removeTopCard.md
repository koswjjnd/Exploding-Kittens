# BVA Analysis for Deck

## Method 1: ```public Card removeTopCard()```
### Step 1-3 Results
|        | Input                                                   | (if more to consider for input) | Output                                                             |
|--------|---------------------------------------------------------|---------------------------------|--------------------------------------------------------------------|
| Step 1 | deck state (empty/single/multiple)                      | none                            | Card: removed card or EmptyDeckException                           |
| Step 2 | Deck state                                              | none                            | Card or EmptyDeckException                                         |
| Step 3 | empty deck, single card deck, multiple cards deck       | none                            | first card from deck or EmptyDeckException                         |

### Step 4:
##### All-combination or each-choice: each-choice

|             | System under test                              | Expected output                                     | Implemented? |
|-------------|------------------------------------------------|-----------------------------------------------------|--------------|
| Test Case 1 | deck.removeTopCard() with empty deck           | throws EmptyDeckException                           | Yes          |
| Test Case 2 | deck.removeTopCard() with single card deck     | returns the card and deck becomes empty             | Yes          |
| Test Case 3 | deck.removeTopCard() with multiple cards deck  | returns the first card and deck size decreases by 1 | Yes          |

## Recall the 4 steps of BVA
### Step 1: Describe the input and output in terms of the domain.
### Step 2: Choose the data type for the input and the output from the BVA Catalog.
### Step 3: Select concrete values along the edges for the input and the output.
### Step 4: Determine the test cases using either all-combination or each-choice strategy.
