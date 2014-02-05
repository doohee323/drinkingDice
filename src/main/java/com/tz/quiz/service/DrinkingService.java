package com.tz.quiz.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.StatusContext;
import com.tz.quiz.support.Constants;
import com.tz.quiz.support.Logger;

/**
 * <pre>
 * business class for the app.
 * </pre>
 * 
 */
public class DrinkingService {

	private StatusContext context = new StatusContext(); // context for app.
	private int pausetime = Constants.defaultRollSpeed; // rolling time
	private int maxDrinkingCnt = Constants.defaultMaxDrinkingCnt; // maximum
	private Logger logger = new Logger();

	/**
	 * <pre>
	 * clone players except for self in order to manipulate player list
	 * </pre>
	 * 
	 * @param List
	 *            <Player> players participants of game
	 * @return StatusContext context for app.
	 */
	public StatusContext playDrinkingGame(List<Player> players) {

		try {
			// play with random roll or not
			if (Constants.randomPlay) {
				Collections.shuffle(players);
			}
			context.setPlayers(players);
			context.setMaxDrinkingCnt(maxDrinkingCnt);
			context.setPausetime(pausetime);

			int nSecond = 0; // time by second
			while (true) {

				// if only one player is left, game finish
				if (context.getPlayers().size() < 2) {
					break;
				}
				// context.setbWin(false);
				context.setnSecond(nSecond);

				Thread.sleep(100);

				ExecutorService pool = Executors.newFixedThreadPool(context
						.getPlayers().size());
				Set<Future<StatusContext>> set = new HashSet<Future<StatusContext>>();

				int sn = context.getSn();
				for (int i = 0; i < context.getPlayers().size(); i++) {
					Player player = context.getPlayers().get(i);
					if (sn == player.getSn()) {
						player.setbTurn(true);
					} else {
						player.setbTurn(false);
					}
					player.setStatus(context);
					Callable<StatusContext> callable = player;
					Future<StatusContext> future = pool.submit(callable);
					set.add(future);
				}
				pool.shutdown();

				nSecond++;
			}

			// print context
			logger.logEnd(context);

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return context;
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
