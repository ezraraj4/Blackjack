import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BlackjackGUI extends JFrame {

    // Game components
    private Deck deck;
    private Hand playerHand, dealerHand;

    // UI components
    private JPanel dealerPanel, playerPanel;
    private JLabel messageLabel;
    private JButton hitButton, standButton, restartButton;

    public BlackjackGUI() {
        setTitle("Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Create panels for dealer and player
        dealerPanel = new JPanel();
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));

        // Message label at the top
        messageLabel = new JLabel("Welcome to Blackjack!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel, BorderLayout.NORTH);

        // Center panel holds both dealer and player panels
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(dealerPanel);
        centerPanel.add(playerPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel();
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        restartButton = new JButton("Restart");
        restartButton.setEnabled(false);
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(restartButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerHit();
            }
        });

        standButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerStand();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });

        // Start a new game
        newGame();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Starts a new game
    private void newGame() {
        deck = new Deck();
        deck.shuffle();
        playerHand = new Hand();
        dealerHand = new Hand();

        // Deal two cards to player and dealer
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        messageLabel.setText("Your turn.");
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        restartButton.setEnabled(false);

        updateUIComponents();
    }

    // Update the panels to show current hands
    private void updateUIComponents() {
        // Update Dealer Panel
        dealerPanel.removeAll();
        // Show first dealer card and hide second card until player stands
        for (int i = 0; i < dealerHand.getCards().size(); i++) {
            Card card = dealerHand.getCards().get(i);
            JLabel cardLabel = createCardLabel();
            if (i == 1 && hitButton.isEnabled()) {
                cardLabel.setText("??");
            } else {
                cardLabel.setText(card.toString());
            }
            dealerPanel.add(cardLabel);
        }
        // Display dealer's value if player's turn is over
        if (!hitButton.isEnabled()) {
            JLabel valueLabel = new JLabel("Value: " + dealerHand.getValue());
            dealerPanel.add(valueLabel);
        } else {
            // Show only the value of the first card as a hint
            Card firstCard = dealerHand.getCards().get(0);
            JLabel valueLabel = new JLabel("Value: " + firstCard.getValue() + " + ?");
            dealerPanel.add(valueLabel);
        }

        // Update Player Panel
        playerPanel.removeAll();
        for (Card card : playerHand.getCards()) {
            JLabel cardLabel = createCardLabel();
            cardLabel.setText(card.toString());
            playerPanel.add(cardLabel);
        }
        JLabel playerValueLabel = new JLabel("Value: " + playerHand.getValue());
        playerPanel.add(playerValueLabel);

        dealerPanel.revalidate();
        dealerPanel.repaint();
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    // Creates a styled label for cards
    private JLabel createCardLabel() {
        JLabel label = new JLabel();
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setPreferredSize(new Dimension(60, 90));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        return label;
    }

    // Called when the player clicks "Hit"
    private void playerHit() {
        playerHand.addCard(deck.drawCard());
        updateUIComponents();
        if (playerHand.getValue() > 21) {
            messageLabel.setText("You bust! Dealer wins.");
            endGame();
        }
    }

    // Called when the player clicks "Stand"
    private void playerStand() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        dealerTurn();
        updateUIComponents();
        determineWinner();
        endGame();
    }

    // Dealer draws cards until reaching at least 17
    private void dealerTurn() {
        while (dealerHand.getValue() < 17) {
            dealerHand.addCard(deck.drawCard());
        }
    }

    // Determines the winner of the game
    private void determineWinner() {
        int playerValue = playerHand.getValue();
        int dealerValue = dealerHand.getValue();
        if (dealerValue > 21) {
            messageLabel.setText("Dealer busts! You win!");
        } else if (playerValue > dealerValue) {
            messageLabel.setText("You win!");
        } else if (playerValue < dealerValue) {
            messageLabel.setText("Dealer wins!");
        } else {
            messageLabel.setText("It's a tie!");
        }
    }

    // Ends the current game and enables Restart
    private void endGame() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        restartButton.setEnabled(true);
    }

    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BlackjackGUI();
            }
        });
    }

    // --- Inner classes for Card, Deck, and Hand ---

    // Represents a playing card
    class Card {
        private String suit;
        private String rank;

        public Card(String rank, String suit) {
            this.rank = rank;
            this.suit = suit;
        }

        // Returns the Blackjack value of the card
        public int getValue() {
            switch (rank) {
                case "K":
                case "Q":
                case "J":
                    return 10;
                case "A":
                    return 11;
                default:
                    return Integer.parseInt(rank);
            }
        }

        @Override
        public String toString() {
            return rank + suit;
        }
    }

    // Represents a deck of 52 cards
    class Deck {
        private List<Card> cards;
        private int currentCardIndex;

        public Deck() {
            String[] suits = {"♠", "♥", "♦", "♣"};
            String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
            cards = new ArrayList<>();
            for (String suit : suits) {
                for (String rank : ranks) {
                    cards.add(new Card(rank, suit));
                }
            }
            currentCardIndex = 0;
        }

        public void shuffle() {
            Collections.shuffle(cards);
            currentCardIndex = 0;
        }

        public Card drawCard() {
            if (currentCardIndex >= cards.size()) {
                shuffle();
            }
            return cards.get(currentCardIndex++);
        }
    }

    // Represents a hand of cards for a player or dealer
    class Hand {
        private List<Card> cards;

        public Hand() {
            cards = new ArrayList<>();
        }

        public void addCard(Card card) {
            cards.add(card);
        }

        // Calculates the hand value, treating Aces as 11 or 1 appropriately
        public int getValue() {
            int sum = 0;
            int aceCount = 0;
            for (Card card : cards) {
                sum += card.getValue();
                if (card.toString().startsWith("A")) {
                    aceCount++;
                }
            }
            // If we're over 21 and have Aces counted as 11, reduce them to 1
            while (sum > 21 && aceCount > 0) {
                sum -= 10; // Switch one Ace from 11 down to 1
                aceCount--;
            }
            return sum;
        }

        public List<Card> getCards() {
            return cards;
        }
    }
}
