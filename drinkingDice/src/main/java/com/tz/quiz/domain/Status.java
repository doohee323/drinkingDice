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
public class Status {

	private int nTurn = 0;	// current turn's player's index
	private int nSecond = 0;	// time (seconds)
	private int maxDrinkingCnt = Constants.defaultMaxDrinkingCnt; // maximum
	private int leftDrintCnt = 0; // number of drinking player at this moment
	private List<Player> players = new ArrayList<Player>();
	private String addedDrinker = null; // added drinker name at last
	private String finishedDrinker = null; // finished drinker name at last
	private String dropedDrinker = null; // droped drinker name at last

	public int getnTurn() {
		return nTurn;
	}

	public void setnTurn(int nTurn) {
		this.nTurn = nTurn;
	}

	public Player getCurPlayer() {
		return getPlayers().get(nTurn);
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

}
