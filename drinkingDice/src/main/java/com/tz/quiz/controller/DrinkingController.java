package com.tz.quiz.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.tz.quiz.domain.Logger;
import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.Roll;
import com.tz.quiz.service.DrinkingService;
import com.tz.quiz.support.Constants;

/**
 * <pre>
 * runnable class for the app.
 * </pre>
 * 
 */
public class DrinkingController {

	/**
	 * <pre>
	 * main(String[] args)
	 * </pre>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// 1) making game
		Roll roll = new Roll();
		Constants.radomPlay = true;
		Constants.debug = false;

		// 2) input
		// run with screen input
		List<Player> players = console(roll);

		// // run with predefined input
		// List<Player> players = manual(roll);

		// 3) run the game
		DrinkingService game = new DrinkingService();
		game.playDrinkingGame(roll, players);

	}

	/**
	 * <pre>
	 * enter input value from screen
	 * </pre>
	 * 
	 * @param Roll
	 *            roll roll object
	 * @return List<Player> participants
	 */
	public static List<Player> console(Roll roll) {
		List<Player> players = new ArrayList<Player>();

		Scanner s = new Scanner(System.in);
		System.out.print("Waiting for players...\n");

		while (true) {
			String cmd = s.nextLine();
			
			cmd = cmd.trim();
			Pattern blank=Pattern.compile("\\s+|\b+|\t+|\n+|\f+|\r+");
			cmd=blank.matcher(cmd).replaceAll(" ");
			if (cmd.equals("HELP")) {
				System.out.print("Commands:\n");
				System.out.print("\tHELP	Print these instructions\n");
				System.out
						.print("\tADD [player name] [drinking time]	Adds named player. \n");
				System.out
						.print("\t\t\t\t\t\tDrinking time is time to finish 1 drink\n");
				System.out
						.print("\tSPEED [seconds]	Number of seconds between rolls. \n");
				System.out.print("\t\t\t\t\t\tDefault 2.\n");
				System.out
						.print("\tMAX [drinks]	Number of drinks before player\n");
				System.out.print("\t\t\t\t\t\tdrops out. Default 5.\n");
				System.out.print("\tSTART	Start the simulation\n");
				System.out.print("\tDEBUG [Y/N]	Whether using debugging \n");
				System.out
						.print("\tRAMDOM [Y/N]	Whether using ramdom for player turn's order\n");
			} else if (cmd.startsWith("ADD")) {
				// define players
				String strArry[] = cmd.split(" ");
				if (strArry.length != 3) {
					System.out
							.print("Incorrect number of arguements for 'ADD'\n");
					continue;
				}
				int drinkingTime = 0;
				try {
					drinkingTime = Integer.parseInt(strArry[2]);
				} catch (Exception e) {
					System.out
							.print("Incorrect number of arguements for 'ADD'\n");
					continue;
				}
				Player player = new Player(strArry[1], drinkingTime);
				players.add(player);
				System.out.print(strArry[0] + ", who can finish a drink in "
						+ strArry[1] + " seconds, has joined the game.\n");
			} else if (cmd.startsWith("SPEED")) {
				// Pause time
				String strArry[] = cmd.split(" ");
				if (strArry.length != 2) {
					System.out
							.print("Incorrect number of arguements for 'SPEED'\n");
					continue;
				}
				int nPausetime = 0;
				try {
					nPausetime = Integer.parseInt(strArry[1]);
				} catch (Exception e) {
					System.out.print("Enter pasue time!\n");
					continue;
				}
				if (nPausetime == 0) {
					nPausetime = Constants.defaultRollSpeed;
				}
				System.out.print("Pause time is " + nPausetime + " seconds.\n");
				roll.setPausetime(nPausetime);
			} else if (cmd.startsWith("MAX")) {
				// maximum number of drinks
				String strArry[] = cmd.split(" ");
				if (strArry.length != 2) {
					System.out
							.print("Incorrect number of arguements for 'MAX'\n");
					continue;
				}
				int nMaxDrinkingCnt = 0;
				try {
					nMaxDrinkingCnt = Integer.parseInt(strArry[1]);
				} catch (Exception e) {
					System.out
							.print("Incorrect number of arguements for 'MAX'\n");
					continue;
				}
				if (nMaxDrinkingCnt > 4) {
					System.out.print("Maximum number of drinks is 4.\n");
					continue;
				} else if (nMaxDrinkingCnt == 0) {
					nMaxDrinkingCnt = Constants.defaultMaxDrinkingCnt;
				}
				System.out.print("Maximum number of drinks is "
						+ nMaxDrinkingCnt + ".\n");
				roll.setMaxDrinkingCnt(nMaxDrinkingCnt);
			} else if (cmd.equals("START")) {
				if (players.size() < 2) {
					System.out
							.print("At least 2 players required to start game.\n");
					continue;
				} else {
					break;
				}
			} else if (cmd.startsWith("DEBUG")) {
				// debugging
				String strArry[] = cmd.split(" ");
				if (strArry.length != 2) {
					System.out
							.print("Incorrect number of arguements for 'DEBUG'\n");
					continue;
				}
				if (strArry[1].equals("Y")) {
					Constants.debug = true;
				} else if (strArry[1].equals("N")) {
					Constants.debug = false;
				} else {
					System.out
							.print("Incorrect number of arguements for 'DEBUG'\n");
					continue;
				}
			} else if (cmd.startsWith("RANDOM")) {
				// random
				String strArry[] = cmd.split(" ");
				if (strArry.length != 2) {
					System.out
							.print("Incorrect number of arguements for 'RANDOM'\n");
					continue;
				}
				if (strArry[1].equals("Y")) {
					Constants.radomPlay = true;
				} else if (strArry[1].equals("N")) {
					Constants.radomPlay = false;
				} else {
					System.out
							.print("Incorrect number of arguements for 'RANDOM'\n");
					continue;
				}
			} else {
				System.out.print("Enter the right command.\n");
			}
		}
		s.close();
		return players;
	}

	/**
	 * <pre>
	 * enter predefined input value
	 * </pre>
	 * 
	 * @param Roll
	 *            roll roll object
	 * @return List<Player> participants
	 */
	public static List<Player> manual(Roll roll) {

		// 1) making game
		Constants.radomPlay = false;
		Constants.debug = true;
		roll.setMaxDrinkingCnt(2);
		roll.setPausetime(2);

		// 2) define players
		List<Player> players = new ArrayList<Player>();
		Player player1 = new Player("Alex", 2);
		Logger.debug("0 / ready:" + player1.getName() + " ("
				+ player1.getDrinkingTime() + ")");
		players.add(player1);
		Player player2 = new Player("Bob", 3);
		Logger.debug("0 / ready:" + player2.getName() + " ("
				+ player2.getDrinkingTime() + ")");
		players.add(player2);
		Player player3 = new Player("Chris", 5);
		Logger.debug("0 / ready:" + player3.getName() + " ("
				+ player3.getDrinkingTime() + ")");
		players.add(player3);

		return players;
	}

}
