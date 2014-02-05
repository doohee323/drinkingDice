package com.tz.quiz.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.Status;
import com.tz.quiz.support.Constants;

/**
 * <pre>
 * business class for the app.
 * </pre>
 * 
 */
public class DrinkingService {

	private Status status = new Status();
	private int pausetime = Constants.defaultRollSpeed; // rolling time
	private int maxDrinkingCnt = Constants.defaultMaxDrinkingCnt; // maximum

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
	public Status playDrinkingGame(List<Player> players) {

		try {
			// play with random roll or not
			if (Constants.radomPlay) {
				Collections.shuffle(players);
			}
			status.setPlayers(players);
			status.setMaxDrinkingCnt(maxDrinkingCnt);
			status.setPausetime(pausetime);

			int nSecond = 0; // time by second
			while (true) {

				// if only one player is left, game finish
				if (status.getPlayers().size() < 2) {
					break;
				}
//				status.setbWin(false);
				status.setnSecond(nSecond);

				Thread.sleep(100);

				ExecutorService pool = Executors.newFixedThreadPool(status
						.getPlayers().size());
				Set<Future<Status>> set = new HashSet<Future<Status>>();

				int nTurn = status.getnTurn();
				for (int i = 0; i < status.getPlayers().size(); i++) {
					Player player = status.getPlayers().get(i);
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

				boolean bWin = false;
				for (Future<Status> future : set) {
					try {
						status = future.get();
						if (status.isbWin()) {
							bWin = true;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				status.setbWin(bWin);

				pool.shutdown();
				
				nSecond++;
			}

			// print status
			status.logEnd();

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return status;
	}

	public int getPausetime() {
		return pausetime;
	}

	public void setPausetime(int pausetime) {
		this.pausetime = pausetime;
	}

	public int getMaxDrinkingCnt() {
		return maxDrinkingCnt;
	}

	public void setMaxDrinkingCnt(int maxDrinkingCnt) {
		this.maxDrinkingCnt = maxDrinkingCnt;
	}

}
