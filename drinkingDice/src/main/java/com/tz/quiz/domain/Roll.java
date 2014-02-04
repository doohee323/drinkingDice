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
	private String addedDrinker = null; // added drinker name at last

	// which player can
	// drink in this
	// rolling
	private int leftDrintCnt = 0; // number of drinking player at this moment
	private List<Player> players = new ArrayList<Player>();
	
	private int nTurn = 0;
	private int nAssing = -1;
	public int getnTurn() {
		return nTurn;
	}
	public void setnTurn(int nTurn) {
		this.nTurn = nTurn;
	}
	public int getnAssing() {
		return nAssing;
	}
	public void setnAssing(int nAssing) {
		this.nAssing = nAssing;
	}

	public Player getCurPlayer() {
		return getPlayers().get(nTurn);
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
}
