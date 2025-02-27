# Blackjack
This Java program implements a graphical user interface (GUI) for the classic card game Blackjack using the Swing framework.

Game Logic:

Uses object-oriented classes for Card, Deck, and Hand.
A Deck consists of 52 standard playing cards, which can be shuffled and drawn.
A Hand tracks the cards held by the player or dealer, calculating the total value with appropriate Ace handling (11 or 1).
User Interface:

The main frame (BlackjackGUI) is set up using JFrame with a BorderLayout.
The top section displays messages (e.g., "Your turn", "You win!").
The center section shows the cards for the Dealer and Player in labeled panels.
The bottom section has buttons for player actions: Hit, Stand, and Restart.

Gameplay Features:

Players can Hit to draw a card or Stand to end their turn.
The Dealer automatically plays by drawing cards until reaching at least 17.
The game determines the winner by comparing hand values, handling busts and ties.
A Restart button allows for a new game session after each round.

Visual Elements:

Cards are displayed using JLabel components with styled text.
The second dealer card is hidden during the player's turn to maintain suspense.
This GUI Blackjack game effectively combines Java Swing for the interface with robust object-oriented programming principles for game mechanics, providing an interactive and engaging user experience.
