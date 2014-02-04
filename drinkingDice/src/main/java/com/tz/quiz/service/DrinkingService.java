package com.tz.quiz.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.Roll;
import com.tz.quiz.domain.Status;
import com.tz.quiz.support.Constants;

/**
 * <pre>
 * business class for the app.
 * </pre>
 * 
 */
public class DrinkingService {

	private static volatile boolean gameOn = true;
	private Status status = new Status();

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
	 * @throws InterruptedException
	 */
	public Roll playDrinkingGame(Roll roll, List<Player> players)
			throws InterruptedException {

		// play with random roll or not
		if (Constants.radomPlay) {
			Collections.shuffle(players);
		}
		roll.setPlayers(players);

		int nSize = roll.getPlayers().size();
		boolean bDrinking = false; // whether exist currently drinking person

		while (gameOn) {
			int nSecond = 0; // time by second

			Thread.sleep(1000);

			ExecutorService pool = Executors.newFixedThreadPool(nSize);
			Set<Future<Status>> set = new HashSet<Future<Status>>();

			int nTurn = roll.getnTurn();
			for (int i = 0; i < nSize; i++) {
				Player player = roll.getPlayers().get(i);
				if (nTurn == player.getSn()) {
					player.setbTurn(true);
				} else {
					player.setbTurn(false);
				}
				player.setStatus(status);
				Callable<Status> callable = player;
				Future<Status> future = pool.submit(callable);
				set.add(future);
			}

			for (Future<Status> future : set) {
				try {
					// check exist drinking player
					bDrinking = roll.getLeftDrintCnt() > 0 ? true : false;
					
					Status curStatus = future.get();
					String diceVale = curStatus.getDiceVale();
					int nSn = curStatus.getSn();
					System.out.println(nSn + ":" + diceVale);

					boolean bWin = Constants.isWin(diceVale);
					if (bWin) {
						// choose driker at ramdon
						int indx = getDrinkers(roll, nSn);
						if (indx >= 0) {
							roll.getPlayers().get(indx).addDrinking(nSecond);
							roll.addLeftDrintCnt();
							roll.setAddedDrinker(roll.getPlayers().get(indx)
									.getName());
						}

					}

					if (!bDrinking) {
						// if else, find the next player who is'nt drinking
						roll = findNextDicer(roll);
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			nSecond++;
		}
		return roll;
	}

	public int getDrinkers(Roll roll, int self) {
		int maxDrintCnt = roll.getMaxDrinkingCnt();

		// choose driker at ramdon
		List<Player> players = new ArrayList<Player>();
		Iterator<Player> e = roll.getPlayers().iterator();
		while (e.hasNext()) {
			Player player = e.next();
			if (player.getSn() != self
					&& player.getDrinkings().size() < maxDrintCnt) {
				players.add(player);
			}
		}

		// play with random roll or not
		if (Constants.radomPlay) {
			Collections.shuffle(players);
		}
		if (players.size() == 0)
			return -1;
		return players.get(0).getSn();
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
