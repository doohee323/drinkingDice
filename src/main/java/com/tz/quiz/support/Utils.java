package com.tz.quiz.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.StatusContext;

/**
 * <pre>
 * utility class
 * </pre>
 * 
 */
public class Utils {

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
		winSet.addAll(Arrays.asList(Constants.winningNumberSet));
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
	public static StatusContext findNextDicer(StatusContext context) {
		int sn = context.getSn();

		Iterator<Player> e = context.getPlayers().iterator();
		while (e.hasNext()) {
			Player player = e.next();
			if (player.getSn() > sn) {
				sn = player.getSn();
				break;
			}
		}
		if (sn >= context.getPlayers().size()) {
			sn = context.getPlayers().get(0).getSn();
		}

		context.setSn(sn);
		return context;
	}

}
