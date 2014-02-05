package com.tz.quiz.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.Status;

/**
 * <pre>
 * constant variables and static method for utility
 * </pre>
 * 
 */
public class Constants {

	// winning cases
	final public static String[] winningNumberSet = { "1,6", "2,5", "3,4",
			"4,3", "5,2", "6,1", "5,6", "6,5", "1,1", "2,2", "3,3", "4,4",
			"5,5", "6,6" };
	// default rolling speed
	final public static int defaultRollSpeed = 2;
	// default maximum count which player can drink in this rolling
	final public static int defaultMaxDrinkingCnt = 2;
	// for development, whether using random player or not
	public static boolean radomPlay = true;
	// debugger flag
	public static boolean debug = true;

	/**
	 * <pre>
	 * whether the input value is winning value or not
	 * </pre>
	 * 
	 * @param String
	 *            input return value of rolling game
	 * @return is winning value or not
	 */
	public static boolean isWin(String input) {
		List<String> winSet = new ArrayList<String>();
		winSet.addAll(Arrays.asList(winningNumberSet));
		return winSet.contains(input);
	}

	/**
	 * <pre>
	 * clone players except for self in order to manipulate player list
	 * </pre>
	 * 
	 * @param List
	 *            <Player> input player to be cloned
	 * @param String
	 *            except except for self
	 * @return List<Player> cloned player
	 */
	public static List<Player> clonePlayers(List<Player> input, String except) {
		List<Player> players = new ArrayList<Player>();
		Iterator<Player> e = input.iterator();
		while (e.hasNext()) {
			Player player = e.next();
			if (!player.getPlayerName().equals(except)) {
				players.add(player);
			}
		}
		return players;
	}

	/**
	 * <pre>
	 * find the next player who is'nt drinking
	 * </pre>
	 * 
	 * @param Roll
	 *            roll <Player> current roll
	 * @return Roll roll <Player> current roll
	 */
	public static Status findNextDicer(Status status) {
		int nTurn = status.getnTurn();
		// if there is no one drinking, turn change.
		if (status.getLeftDrintCnt() == 0) {
			nTurn++;
		}

		// if the next player is drinking, then the turn pass to the next one.
		// nTurn++;
		// for (int i = nTurn; i < roll.getPlayers().size(); i++) {
		// if(roll.getPlayers().get(i).getLeftDrinkingTime() == 0) {
		// break;
		// } else {
		// nTurn++;
		// }
		// }

		if (nTurn >= status.getPlayers().size()) {
			nTurn = 0;
		}
		status.setnTurn(nTurn);
		return status;
	}
}
