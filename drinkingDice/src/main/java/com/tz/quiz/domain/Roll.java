package com.tz.quiz.domain;

import java.util.ArrayList;
import java.util.List;

import com.tz.quiz.support.Constants;

/**
 * <pre>
 * model class for roll
 * </pre>
 * 
 */
public class Roll {

	private int pausetime = Constants.defaultRollSpeed; // rolling time
	private int maxDrinkingCnt = Constants.defaultMaxDrinkingCnt; // maximum
																	// count
	// which player can
	// drink in this
	// rolling
	private int leftDrintCnt = 0; // number of drinking player at this moment
	private List<Player> players = new ArrayList<Player>();
	private int nTurn = 0; // current turn of player
	private String addedDrinker = null; // added drinker name at last
	private String finishedDrinker = null; // finished drinker name at last
	private String dropedDrinker = null; // droped drinker name at last

	private Logger logger = new Logger(); // print the logging

	public Roll() {
		logger = new Logger(); // print the logging
	}

	// logging status
	public void logStatus(boolean bWin) {
		// if (Constants.debug)
		// return;

		if (bWin || addedDrinker != null || finishedDrinker != null
				|| dropedDrinker != null) {
		} else {
			return;
		}

		Player curPlayer = getCurPlayer();
		logger.println("==== STATUS ====");
		logger.println("There are " + players.size() + " players.");
		logger.println("It is " + curPlayer.getName() + "'s turn.");
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			if (player.getLeftDrinkingTime() > 0
					&& player.getLeftDrinkingCnt() > 0) {
				logger.println(player.getName() + " has had "
						+ player.getDrunkCnt()
						+ " drinks and is currently drinking "
						+ player.getLeftDrinkingCnt() + " more.");
			} else {
				logger.println(player.getName() + " has had "
						+ player.getDrunkCnt() + " drinks.");
			}
		}
		logger.println("\n");
		logger.println(curPlayer.getName() + "'s turn.");
		logger.println("\n");
		logger.println(curPlayer.getName() + " rolled "
				+ curPlayer.getDiceDisplayVale());
		if (addedDrinker != null) {
			logger.println(curPlayer.getName() + " says: '" + addedDrinker
					+ ", drink!'");
		}
		if (finishedDrinker != null) {
			logger.println(finishedDrinker + " is done drinking.");
		}
		if (dropedDrinker != null) {
			logger.println(dropedDrinker
					+ " says: 'I've had too many. I need to stop.'");
		}
		logger.println("\n");
		logger.flush();
		addedDrinker = null;
		finishedDrinker = null;
		dropedDrinker = null;
	}

	// logging end
	public void logEnd() {
		// if (Constants.debug)
		// return;

		if (finishedDrinker != null) {
			logger.println(finishedDrinker + " is done drinking.");
		}
		if (dropedDrinker != null) {
			logger.println(dropedDrinker
					+ " says: 'I've had too many. I need to stop.'");
		}
		logger.println("\n");

		Player curPlayer = getCurPlayer();
		logger.println("==== STATUS ====");
		logger.println("The game is over. " + curPlayer.getName()
				+ "is the winner.");
		logger.println("\n");
		logger.println(curPlayer.getName() + "is the winner!");
		logger.flush();
		addedDrinker = null;
		finishedDrinker = null;
		dropedDrinker = null;
	}

	public Player getCurPlayer() {
		return getPlayers().get(nTurn);
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

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setMaxDrinkingCnt(maxDrinkingCnt);
		}
		this.players = players;
	}

	public void removePlayer(String name) {
		List<Player> newPlayers = Constants.clonePlayers(players, name);
		this.players = newPlayers;
	}

	public int getLeftDrintCnt() {
		return leftDrintCnt;
	}

	public void addLeftDrintCnt() {
		this.leftDrintCnt++;
	}

	public void redueLeftDrintCnt() {
		this.leftDrintCnt--;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger output) {
		this.logger = output;
	}

	public int getnTurn() {
		return nTurn;
	}

	public void setnTurn(int nTurn) {
		this.nTurn = nTurn;
	}

	public String getAddedDrinker() {
		return addedDrinker;
	}

	public void setAddedDrinker(String addedDrinker) {
		this.addedDrinker = addedDrinker;
	}

	public String getFinishedDrinker() {
		return finishedDrinker;
	}

	public void setFinishedDrinker(String finishedDrinker) {
		this.finishedDrinker = finishedDrinker;
	}

	public String getDropedDrinker() {
		return dropedDrinker;
	}

	public void setDropedDrinker(String dropedDrinker) {
		this.dropedDrinker = dropedDrinker;
	}
}
