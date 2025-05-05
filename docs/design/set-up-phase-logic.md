1.Game Setup Module Overview
<table>
  <thead>
    <tr>
      <th>Class / Interface</th>
      <th>Method Signature</th>
      <th>Input</th>
      <th>Output</th>
      <th>Responsibility</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>GameSetupController</td>
      <td>void setupGame()</td>
      <td>—</td>
      <td>—</td>
      <td>Orchestrates the entire setup flow by invoking methods in sequence.</td>
    </tr>
    <tr>
      <td>GameSetupController</td>
      <td>int promptPlayerCount()</td>
      <td>—</td>
      <td>count</td>
      <td>Prompts for player count; loops between View and PlayerService until a valid count is returned.</td>
    </tr>
    <tr>
      <td>GameSetupController</td>
      <td>List&lt;Player&gt; createPlayers(int count)</td>
      <td>count</td>
      <td>players</td>
      <td>For each of the <code>count</code> players, reads a nickname via View and creates a <code>Player</code> via PlayerService.</td>
    </tr>
    <tr>
      <td>GameSetupController</td>
      <td>Deck prepareDeck(int playerCount)</td>
      <td>playerCount</td>
      <td>deck</td>
      <td>Builds &amp; shuffles deck: init base cards, deal defuses, deal initial hands, add bombs, reshuffle.</td>
    </tr>
    <tr>
      <td>GameSetupController</td>
      <td>void initializeTurnOrder(List&lt;Player&gt; players)</td>
      <td>players</td>
      <td>—</td>
      <td>Generates &amp; stores turn order (e.g. in <code>GameContext</code>).</td>
    </tr>
    <tr>
      <td>GameSetupView (interface)</td>
      <td>int promptPlayerCount()</td>
      <td>—</td>
      <td>count</td>
      <td>Reads &amp; returns player count from console or GUI.</td>
    </tr>
    <tr>
      <td>GameSetupView</td>
      <td>String promptNickname(int index)</td>
      <td>index</td>
      <td>nickname</td>
      <td>Reads &amp; returns nickname for the player at <code>index</code>.</td>
    </tr>
    <tr>
      <td>GameSetupView</td>
      <td>void showError(String msg)</td>
      <td>msg</td>
      <td>—</td>
      <td>Displays an error message (console or popup).</td>
    </tr>
    <tr>
      <td>PlayerService</td>
      <td>void validateCount(int count)</td>
      <td>count</td>
      <td>—</td>
      <td>Throws <code>InvalidPlayerCountException</code> if <code>count</code> is out of range.</td>
    </tr>
    <tr>
      <td>PlayerService</td>
      <td>Player createPlayer(String rawNickname)</td>
      <td>rawNickname</td>
      <td>Player</td>
      <td>Validates nickname; throws <code>InvalidNicknameException</code>; returns an immutable <code>Player</code>.</td>
    </tr>
    <tr>
      <td>DealService</td>
      <td>void dealInitialHands(Deck deck, List&lt;Player&gt; players, int n)</td>
      <td>deck, players, n</td>
      <td>—</td>
      <td>Deals <code>n</code> cards to each player in round-robin from the deck.</td>
    </tr>
    <tr>
      <td>DealService</td>
      <td>void dealDefuses(Deck deck, List&lt;Player&gt; players)</td>
      <td>deck, players</td>
      <td>—</td>
      <td>Deals one Defuse card to each player in round-robin from the deck.</td>
    </tr>
    <tr>
      <td>Deck</td>
      <td>void initializeBaseDeck(int playerCount)</td>
      <td>playerCount</td>
      <td>—</td>
      <td>Adds all base cards plus <code>(5 – playerCount)</code> Defuse cards.</td>
    </tr>
    <tr>
      <td>Deck</td>
      <td>void shuffle()</td>
      <td>—</td>
      <td>—</td>
      <td>Randomly shuffles the internal card list.</td>
    </tr>
    <tr>
      <td>Deck</td>
      <td>Card drawOne()</td>
      <td>—</td>
      <td>Card</td>
      <td>Removes and returns the top card from the deck.</td>
    </tr>
    <tr>
      <td>Deck</td>
      <td>void addExplodingKittens(int count)</td>
      <td>count</td>
      <td>—</td>
      <td>Inserts the specified number of Exploding Kitten cards into the deck.</td>
    </tr>
    <tr>
      <td>Player</td>
      <td>Player(String name)</td>
      <td>name</td>
      <td>—</td>
      <td>Constructs an immutable <code>Player</code> with validated <code>name</code>.</td>
    </tr>
    <tr>
      <td>Player</td>
      <td>String getName()</td>
      <td>—</td>
      <td>name</td>
      <td>Returns the player’s nickname (read-only).</td>
    </tr>
    <tr>
      <td>Player</td>
      <td>void receiveCard(Card c)</td>
      <td>c</td>
      <td>—</td>
      <td>Adds the given card to the player’s hand.</td>
    </tr>
    <tr>
      <td>Player</td>
      <td>List&lt;Card&gt; getHand()</td>
      <td>—</td>
      <td>hand list</td>
      <td>Returns an unmodifiable view of the player’s hand.</td>
    </tr>
    <tr>
      <td>Card (abstract)</td>
      <td>protected Card(CardType type)</td>
      <td>type</td>
      <td>—</td>
      <td>Initializes the card’s type internally.</td>
    </tr>
    <tr>
      <td>Card</td>
      <td>CardType getType()</td>
      <td>—</td>
      <td>type</td>
      <td>Returns the card’s enum type.</td>
    </tr>
    <tr>
      <td>XCard subclasses</td>
      <td>public XCard()</td>
      <td>—</td>
      <td>—</td>
      <td>Subclass constructors; call <code>super(CardType.X)</code> (e.g. <code>DefuseCard()</code>, <code>ExplodingKittenCard()</code>).</td>
    </tr>
  </tbody>
</table>

2.Call Sequence (Setup Flow Mapping)
2.1 setupGame()
Calls, in order:

promptPlayerCount()

createPlayers(count)

prepareDeck(count)

initializeTurnOrder(players)

2.2 promptPlayerCount()
Loop until a valid count is returned

Call view.promptPlayerCount()

Call playerService.validateCount(count)

If invalid:

Catch exception

Call view.showError(msg)

Retry

2.3 createPlayers(count)
For i = 1…count:

Call view.promptNickname(i)

Call playerService.createPlayer(rawNick) → Player

Collect all Player objects into a List<Player> and return it.

2.4 prepareDeck(count)
Instantiate: new Deck()

Call deck.initializeBaseDeck(count)

Call dealService.dealDefuses(deck, players)

Call deck.shuffle()

Call dealService.dealInitialHands(deck, players, 5)

Call deck.addExplodingKittens(count – 1)

Call deck.shuffle()

Return the prepared Deck

2.5 initializeTurnOrder(players)
Generate the turn order (e.g. round-robin or random)

Save it into the game context (or return it)