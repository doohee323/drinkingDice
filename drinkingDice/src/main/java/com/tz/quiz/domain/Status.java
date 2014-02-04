package com.tz.quiz.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tz.quiz.support.Constants;
import com.tz.quiz.support.Logger;

/**
 * <pre>
 * model class for roll
 * </pre>
 * 
 */
public class Status {

	private int nTurn = 0; // current turn's player's index
	private int nSecond = 0; // time (seconds)
	private int maxDrinkingCnt = Constants.defaultMaxDrinkingCnt; // maximum
	private int leftDrintCnt = 0; // number of drinking player at this moment
	private List<Player> players = new ArrayList<Player>();
	private boolean bWin = false; // whether has winning value
	private String addedDrinker = null; // added drinker name at last
	private String finishedDrinker = null; // finished drinker name at last
	private String dropedDrinker = null; // droped drinker name at last
	private Logger logger = new Logger(); // print the logging

	// logging status
	public void logStatus() {
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
		logger.println("It is " + curPlayer.getPlayerName() + "'s turn.");
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			if (player.getLeftDrinkingTime() > 0
					&& player.getLeftDrinkingCnt() > 0) {
				logger.println(player.getPlayerName() + " has had "
						+ player.getDrunkCnt()
						+ " drinks and is currently drinking "
						+ player.getLeftDrinkingCnt() + " more.");
			} else {
				logger.println(player.getPlayerName() + " has had "
						+ player.getDrunkCnt() + " drinks.");
			}
		}
		logger.println("\n");
		logger.println(curPlayer.getPlayerName() + "'s turn.");
		logger.println("\n");
		logger.println(curPlayer.getPlayerName() + " rolled "
				+ curPlayer.getDiceDisplayVale());
		if (addedDrinker != null) {
			logger.println(curPlayer.getPlayerName() + " says: '"
					+ addedDrinker + ", drink!'");
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
		logger.println("The game is over. " + curPlayer.getPlayerName()
				+ "is the winner.");
		logger.println("\n");
		logger.println(curPlayer.getPlayerName() + "is the winner!");
		logger.flush();
		addedDrinker = null;
		finishedDrinker = null;
		dropedDrinker = null;
	}

	public int getnTurn() {
		return nTurn;
	}

	public void setnTurn(int nTurn) {
		this.nTurn = nTurn;
	}

	public Player getCurPlayer() {
		return getPlayers().get(nTurn);
	}

	public Player getPlayerBySn(int sn) {
		Iterator<Player> e = players.iterator();
		while (e.hasNext()) {
			Player player = e.next();
			if (player.getSn() == sn) {
				return player;
			}
		}
		return null;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
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

	public String getAddedDrinker() {
		return addedDrinker;
	}

	public void setAddedDrinker(String addedDrinker) {
		this.addedDrinker = addedDrinker;
	}

	public int getMaxDrinkingCnt() {
		return maxDrinkingCnt;
	}

	public void setMaxDrinkingCnt(int maxDrinkingCnt) {
		this.maxDrinkingCnt = maxDrinkingCnt;
	}

	public int getnSecond() {
		return nSecond;
	}

	public void setnSecond(int nSecond) {
		this.nSecond = nSecond;
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

	public boolean isbWin() {
		return bWin;
	}

	public void setbWin(boolean bWin) {
		this.bWin = bWin;
	}

}
