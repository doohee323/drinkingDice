package com.tz.quiz.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <pre>
 * model class for player
 * </pre>
 * 
 */
public class Player extends Thread implements Callable<Status> {

	private int sn = -1; // player sn
	private boolean bTurn = false; // whether turn to dice or not 
	private String playerName = null; // player name
	private String diceVale = null; // dice result
	private int drinkingTime = 0; // left time to drink
	private int drunkCnt = 0; // number which this player's already drunk
	private int curDrunkSeq = -1; // current sequence to drink
	private int maxDrinkingCnt = 0; // maximum drinking count which this player
									// can drink
	private Status status = new Status();

	private List<Drinking> drinkings = new ArrayList<Drinking>(); // drinkings
	
	// info. to
	// drink

	public Player(int sn, String name, int drinkingTime) {
		this.sn = sn;
		this.playerName = name;
		this.drinkingTime = drinkingTime;
	}

	@Override
	public Status call() throws Exception {
		status.setSn(sn);
		if(bTurn) {
			dice();
			status.setDiceVale(diceVale);
		}
		return status;
	}

	/**
	 * <pre>
	 * drinking status of players
	 * </pre>
	 */
	public class Drinking {
		private int secondToDrink = 0;

		public Drinking() {
			secondToDrink = drinkingTime;
		}

		public int getSecondToDrink() {
			return secondToDrink;
		}

		public int drinking() {
			if (this.secondToDrink > 0)
				this.secondToDrink--;
			return this.secondToDrink;
		}
	}

	/**
	 * <pre>
	 * add a drinking to drink to the list
	 * </pre>
	 * 
	 * @param int nSecond second for logging
	 * @return boolean add drinking to list or not
	 */
	public boolean addDrinking(int nSecond) {
		if (this.drinkings.size() < maxDrinkingCnt) {
			// when finished this drinking, move to the next drinking
			if ((this.drinkings.size() - 1) == curDrunkSeq
					&& getLeftDrinkingTime() == 0)
				curDrunkSeq++;

			this.drinkings.add(new Drinking());
			Logger.debug(nSecond + " / add drinking:" + playerName + " ("
					+ (this.drinkings.size() - 1) + ")");
			return true;
		}
		return false;
	}
	
	/**
	 * <pre>
	 * get the left time to drink this drinking
	 * </pre>
	 * 
	 * @return left time to drink
	 */
	public int getLeftDrinkingTime() {
		if (this.drinkings.size() == 0)
			return 0;
		return this.drinkings.get(this.drinkings.size() - 1).getSecondToDrink();
	}

	/**
	 * <pre>
	 * dice operation
	 * </pre>
	 */
	public void dice() {
		int dice = (int) (Math.random() * 6 + 1);
		int dice2 = (int) (Math.random() * 6 + 1);
		this.diceVale = dice + "," + dice2;
	}

	public String getDiceVale() {
		return diceVale;
	}

	public void setDiceVale(String diceVale) {
		this.diceVale = diceVale;
	}

	public int getDrinkingTime() {
		return drinkingTime;
	}

	public void setDrinkingTime(int drinkingTime) {
		this.drinkingTime = drinkingTime;
	}

	public int getDrunkCnt() {
		return drunkCnt;
	}

	public void setDrunkCnt(int drunkCnt) {
		this.drunkCnt = drunkCnt;
	}

	public int getCurDrunkSeq() {
		return curDrunkSeq;
	}

	public void setCurDrunkSeq(int curDrunkSeq) {
		this.curDrunkSeq = curDrunkSeq;
	}

	public int getMaxDrinkingCnt() {
		return maxDrinkingCnt;
	}

	public void setMaxDrinkingCnt(int maxDrinkingCnt) {
		this.maxDrinkingCnt = maxDrinkingCnt;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public boolean isbTurn() {
		return bTurn;
	}

	public void setbTurn(boolean bTurn) {
		this.bTurn = bTurn;
	}

	public List<Drinking> getDrinkings() {
		return drinkings;
	}

	public void setDrinkings(List<Drinking> drinkings) {
		this.drinkings = drinkings;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
