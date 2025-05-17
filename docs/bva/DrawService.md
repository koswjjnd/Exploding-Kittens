# BVA Analysis for DrawService

## Method 1: ```public Card drawCardFromTop(Deck deck)```

### Step 1-3 Results

|        | Input                                                   | (if more to consider for input)         | Output                                                             |
|--------|---------------------------------------------------------|-----------------------------------------|--------------------------------------------------------------------|
| Step 1 | deck: deck to draw from                                 | deck state (null/empty/single/multiple) | Card: drawn card or InvalidDeckException or EmptyDeckException     |
| Step 2 | Deck                                                    | none                                    | Card or InvalidDeckException or EmptyDeckException                 |
| Step 3 | null, empty deck, single card deck, multiple cards deck | none                                    | first card from deck or InvalidDeckException or EmptyDeckException |

### Step 4:

##### All-combination or each-choice: each-choice

|             | System under test                              | Expected output                                     | Implemented? |
|-------------|------------------------------------------------|-----------------------------------------------------|--------------|
| Test Case 1 | drawService.drawCardFromTop(null)              | throws InvalidDeckException                         | Yes          |
| Test Case 2 | drawService.drawCardFromTop(emptyDeck)         | throws EmptyDeckException                           | Yes          |
| Test Case 3 | drawService.drawCardFromTop(singleCardDeck)    | returns the card and deck becomes empty             | Yes          |
| Test Case 4 | drawService.drawCardFromTop(multipleCardsDeck) | returns the first card and deck size decreases by 1 | Yes          |

## Recall the 4 steps of BVA

### Step 1: Describe the input and output in terms of the domain.

### Step 2: Choose the data type for the input and the output from the BVA Catalog.

### Step 3: Select concrete values along the edges for the input and the output.

### Step 4: Determine the test cases using either all-combination or each-choice strategy.
