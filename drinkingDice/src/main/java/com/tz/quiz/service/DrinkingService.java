package com.tz.quiz.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.tz.quiz.domain.Logger;
import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.Roll;
import com.tz.quiz.support.Constants;

/**
 * <pre>
 * business class for the app.
 * </pre>
 * 
 */
public class DrinkingService {

	class SimpleThread extends Thread {
		public SimpleThread(String str) {
			super(str);
		}

		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println(i + " " + getName());
				try {
					sleep((int) (Math.random() * 1000));
				} catch (InterruptedException e) {
				}
			}
			System.out.println("DONE! " + getName());
		}
	}

	/**
	 * <pre>
	 * clone players except for self in order to manipulate player list
	 * </pre>
	 * 
	 * @param List
	 *            <Player> input player to be cloned
	 * @param List
	 *            <Player> players participants of game
	 * @return roll output roll itself
	 */
	public Roll playDrinkingGame(Roll roll, List<Player> players) {

		// play with random roll or not
		if (Constants.radomPlay) {
			Collections.shuffle(players);
		}
		roll.setPlayers(players);

		int nSecond = 0; // time by second
		int nSeq = 0; // for rolling time (speed of game)
		boolean bDrinking = false; // whether exist currently drinking person
		boolean bWin = false; // case of getting winning value
		while (roll.getPlayers().size() > 1) {

			// calculate for drinker's drinking time
			for (int i = 0; i < roll.getPlayers().size(); i++) {
				Player player = roll.getPlayers().get(i);
				if (player.getDrinkings().size() > 0) {
					if (player.drinking(nSecond)) { // true => finished
						roll.redueLeftDrintCnt();
						// once finished drinking, can join rolling again
						roll = findNextDicer(roll);

						roll.setFinishedDrinker(player.getName());
					}
					if (player.getDrunkCnt() == roll.getMaxDrinkingCnt()
							&& player.getLeftDrinkingTime() == 0) {
						Logger.debug(nSecond + " / droped off :"
								+ player.getName());
						roll.removePlayer(player.getName());
						roll = findNextDicer(roll);

						roll.setDropedDrinker(player.getName());
					}
				}
			}

			// if only one player is left, game finish
			if (roll.getPlayers().size() < 2) {
				break;
			}

			// dicing considering with game speed
			if (nSecond == 0 || nSecond >= (roll.getPausetime() * nSeq)) {
				Player curPlayer = roll.getCurPlayer();
				curPlayer.dice();

				// check exist drinking player
				bDrinking = roll.getLeftDrintCnt() > 0 ? true : false;

				Logger.debug(nSecond + " / dice:" + curPlayer.getName() + " ("
						+ roll.getnTurn() + ") / bDrinking:" + bDrinking + " ("
						+ curPlayer.getDiceVale() + ")");
				bWin = Constants.isWin(curPlayer.getDiceVale());
				if (bWin) {
					// choose driker at ramdon
					List<Player> drinkers = getDrinkers(roll,
							curPlayer.getName());
					if (drinkers.size() > 0) {
						String selectedPlayer = drinkers.get(0).getName();
						for (int i = 0; i < roll.getPlayers().size(); i++) {
							// assign a drinking to drinker
							Player player = roll.getPlayers().get(i);
							if (selectedPlayer.equals(player.getName())) {
								roll.getPlayers().get(i).addDrinking(nSecond);
								roll.addLeftDrintCnt();
								roll.setAddedDrinker(roll.getPlayers().get(i)
										.getName());
								break;
							}
						}
					}
				}
				nSeq++;

				// print status
				roll.logStatus(bWin);

				// when existing drinking plaer, current winner can roll again.
				if (!bDrinking) {
					// if else, find the next player who is'nt drinking
					roll = findNextDicer(roll);
				}
			}

			nSecond++;
		}

		roll.logEnd();

		// return roll
		return roll;
	}

	/**
	 * <pre>
	 * get available drinkers
	 * </pre>
	 * 
	 * @param Roll
	 *            roll <Player> current roll
	 * @param String
	 *            self except for self
	 * @return List<Player> available drinkers
	 */
	public List<Player> getDrinkers(Roll roll, String self) {
		int maxDrintCnt = roll.getMaxDrinkingCnt();

		// choose driker at ramdon
		List<Player> players = new ArrayList<Player>();
		Iterator<Player> e = roll.getPlayers().iterator();
		while (e.hasNext()) {
			Player player = e.next();
			if (!player.getName().equals(self)
					&& player.getDrinkings().size() < maxDrintCnt) {
				players.add(player);
			}
		}

		// play with random roll or not
		if (Constants.radomPlay) {
			Collections.shuffle(players);
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
	public Roll findNextDicer(Roll roll) {
		int nTurn = roll.getnTurn();
		// if there is no one drinking, turn change.
		if (roll.getLeftDrintCnt() == 0) {
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

		if (nTurn >= roll.getPlayers().size()) {
			nTurn = 0;
		}
		roll.setnTurn(nTurn);
		return roll;
	}

}
