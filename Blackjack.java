//Sarah Hoekema, Sean Chambers
//CS 145
//October 11, 2022
//Lab 4 - Deck of Cards

package blackjack;
import java.util.*;

public class Blackjack {

	//fields
	private static Deck deck;
	private static int balance;
	private static Scanner scan;

	public static void main(String[] args) {
		//introduce game
		introduction();
		//initial deck
		deck = new Deck();
		//initalize account
		final int INITIAL_ACCOUNT = establishAccount();
		balance = INITIAL_ACCOUNT;

		do{
			//initalize objects
			DealerHand dealer = new DealerHand();
			PlayerHand player = new PlayerHand();
			scan = new Scanner(System.in);

			//shuffle deck
			deck.shuffle();
			//establishes the bet
			player.setWager(establishBet(scan));
			//deals two cards to dealer and player
			dealer.addCard(deck.dealCard());
			player.addCard(deck.dealCard());
			dealer.addCard(deck.dealCard());
			player.addCard(deck.dealCard());
			//switch performs actions based on player input
			switch(getMove(scan, player)){
				case "HIT":
					player.addCard(deck.dealCard());
					System.out.println(player.toString());
					break;
				case "STAND":
					break;
				default:
					break;
			}
			//deals to dealer per preset moves
			System.out.println(dealer.toString());
			if(dealer.hitOrStand()){
				do{
					dealer.addCard(deck.dealCard());
					System.out.println(dealer.toString());
				}while(dealer.hitOrStand());
			}

			//checks if player beat the dealer
			if(checkWin(player,dealer)){
				System.out.println("You win!");
				balance += player.getWager();
				deck.addCards(player.getHand());
				deck.addCards(dealer.getHand());
			}else if(player.getValue() == dealer.getValue()){
				//DO tie logic
			}else{
				System.out.println("Better luck next time...");
				balance -= player.getWager();
				deck.addCards(player.getHand());
				deck.addCards(dealer.getHand());
			}
		}while(balance != 0 && promptReplay(scan));
		//print results
	}

	//requests user to establish inital account amount
	private static int establishAccount(){
		Scanner accountAmount = new Scanner(System.in);
		try {
			System.out.println("Please input your inital balance.");
			int amount = Integer.valueOf(accountAmount.nextLine());
			return amount;
		} catch (Exception e) {
			System.out.println("Whoops something went wrong... " + e);
			System.out.println("Setting inital balance to 100");
			return 100;
		}
	}

	//requests user to place their bet
	private static int establishBet(Scanner betAmount){
		try {
			System.out.printf("You have %d left in your account.\nWhat is your bet?\n", balance);
			String bet = betAmount.nextLine();
			return Integer.valueOf(bet);
		} catch (Exception e) {
			System.out.println("Whoops something went wrong... " + e);
			System.out.println("Setting bet to 2");
			return 2;
		}
	}
	
	/**
	 * Prints introduction to console.
	 * Explains game and established basic rules. 
	 */
	private static void introduction() {
		System.out.println("Welcome to blackjack!");
		System.out.println("This game is brought to you by Sarah Hoekema and Sean Chambers");
		System.out.println("The goal of the game is to beat the dealers score without going over 21.");
		System.out.println("Cards are equal to their face value.");
		System.out.println("All face cards are worth 10 points, Aces are either 11 or 1.");
	}

	//returns true if player scored higher that dealer but not over 21, else returns false
	private static boolean checkWin(PlayerHand player, DealerHand dealer) {
		//sets total to -1 if player is bust, else sets to card total
		int playerTotal = player.getValue() < 22 ? player.getValue() : -1;
		int dealerTotal = dealer.getValue() < 22 ? dealer.getValue() : -1;
		return playerTotal > dealerTotal ? true : false;
	}

	private static String getMove(Scanner nextMove, PlayerHand player){
		System.out.println(player.toString());
		System.out.println("Would you like to hit or stand?");
		String move = nextMove.nextLine();
		return move.toUpperCase();
		
	}

	private static boolean promptReplay(Scanner playAgainScanner) {
		System.out.print("Would you like to play again? ");
		try {
			String response = playAgainScanner.nextLine();    
			//ensures response is the upper case of the first character
			char upper = Character.toUpperCase(response.charAt(0));	
			switch(upper) {								            
				case 'Y':
					return true;
				case 'N': 
					return false;
				default: 
					System.out.println("Sorry that response is not valid...");
			}
		} catch (Exception e) {
			System.out.println("Sorry that response is not valid...");
		}
		return false;
	}
}
