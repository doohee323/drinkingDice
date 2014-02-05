package com.tz.quiz.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tz.quiz.support.Constants;
import com.tz.quiz.support.Logger;
import com.tz.quiz.support.Utils;

/**
 * <pre>
 * model class for status of rolling
 * </pre>
 * 
 */
public class Status {

	private int sn = 0; // current turn's player's sn
	private int nSecond = 0; // time (seconds)
	private int pausetime = Constants.defaultRollSpeed; // rolling time
	private int maxDrinkingCnt = Constants.defaultMaxDrinkingCnt; // maximum
	private int leftDrintCnt = 0; // number of drinking player at this moment
	private List<Player> players = new ArrayList<Player>();
	private boolean bWin = false; // whether has winning value
	private String addedDrinker = null; // added drinker name at last
	private String finishedDrinker = null; // finished drinker name at last
	private String dropedDrinker = null; // droped drinker name at last
	private Logger logger = new Logger(); // print the logging

	/**
	 * <pre>
	 * get from list by player's sn
	 * </pre>
	 * 
	 * @param int player's sn
	 * @return Player
	 */
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

	/**
	 * <pre>
	 * logging end
	 * </pre>
	 * 
	 */
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

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public Player getCurPlayer() {
		return getPlayerBySn(sn);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public void removePlayer(String name) {
		List<Player> newPlayers = Utils.clonePlayers(players, name);
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

	public int getPausetime() {
		return pausetime;
	}

	public void setPausetime(int pausetime) {
		this.pausetime = pausetime;
	}

}
