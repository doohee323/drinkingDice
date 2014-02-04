package com.tz.quiz.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.tz.quiz.domain.Player;
import com.tz.quiz.service.DrinkingService;
import com.tz.quiz.support.Constants;

public class DrinkingController {
	public static void main(String[] args) {

		DrinkingService service = new DrinkingService();
		Constants.radomPlay = false;
		Constants.debug = false;

		// 2) define players
		// run with screen input
		// List<Player> players = console(service);

		// // run with predefined input
		List<Player> players = manual(service);

		// 3) run the game
		service.playDrinkingGame(players);

		return;
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
	public static List<Player> console(DrinkingService service) {
		List<Player> players = new ArrayList<Player>();

		Scanner s = new Scanner(System.in);
		System.out.print("Waiting for players...\n");

		int nPlayerInx = 0;

		while (true) {
			String cmd = s.nextLine();

			cmd = cmd.trim();
			Pattern blank = Pattern.compile("\\s+|\b+|\t+|\n+|\f+|\r+");
			cmd = blank.matcher(cmd).replaceAll(" ");
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
				Player player = new Player(nPlayerInx, strArry[1], drinkingTime);
				players.add(player);
				System.out.print(strArry[0] + ", who can finish a drink in "
						+ strArry[1] + " seconds, has joined the game.\n");
				nPlayerInx++;
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
				service.setPausetime(nPausetime);
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
				service.setMaxDrinkingCnt(nMaxDrinkingCnt);
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
	public static List<Player> manual(DrinkingService service) {

		// 1) making game
		Constants.radomPlay = false;
		Constants.debug = true;
		service.setMaxDrinkingCnt(2);
		service.setPausetime(2);

		// 2) define players
		List<Player> players = new ArrayList<Player>();
		Player player1 = new Player(0, "Alex", 2);
		players.add(player1);

		Player player2 = new Player(1, "Bob", 3);
		players.add(player2);
		//
		// Player player3 = new Player(2, "Chris", 5);
		// players.add(player3);

		return players;
	}
}
