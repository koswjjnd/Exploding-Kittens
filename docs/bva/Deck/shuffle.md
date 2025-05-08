# BVA Analysis for deck

## Method: public void shuffle()

### Step 1–3: Input Identification and Equivalence Partitioning

| Step | Input (Deck contents)         | Additional Considerations       | Output                          |
|------|-------------------------------|----------------------------------|----------------------------------|
| 1    | Deck                          |                                  | No return; modifies internal list |
| 2    | List<Card> cards              |                                  | List reordered or unchanged       |
| 3    | cards.size = 0, 1, 2, 52   | Focus on size edge cases         | Shuffle result should preserve all cards |

---

### Step 4: Test Cases (Each-Choice)

| Test Case ID | System Under Test      | Input Description                   | Expected Output                                                            | Implemented |
|--------------|------------------------|--------------------------------------|-----------------------------------------------------------------------------|-------------|
| TC1          | deck.shuffle()       | Empty deck (size = 0)              | No exception; deck remains empty                                           |      |
| TC2          | deck.shuffle()       | One card in deck (size = 1)        | No exception; order remains unchanged                                      |     |                           
| TC3          | deck.shuffle()       | Five cards (all different types)     | Cards preserved; order likely changed                                      |     |
| TC4          | deck.shuffle()       | Full initialized deck (e.g., 52 cards) | Cards preserved (count & type); order changed                             |      |
| TC5          | deck.shuffle() called multiple times | Deck with >1 card        | At least one shuffle results in different order                            |      |

---

### Notes

- Shuffle should never throw an exception regardless of deck size.
- Card list content (types and counts) should always be preserved after shuffling.
- For deterministic tests, Deck accepts an injected Random to produce predictable shuffle results (for testability).
