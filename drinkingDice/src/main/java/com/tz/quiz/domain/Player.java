package com.tz.quiz.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * model class for player
 * </pre>
 * 
 */
public class Player {

	private String name = null; // player name
	private String diceVale = null; // dice result
	private int drinkingTime = 0; // left time to drink
	private int drunkCnt = 0; // number which this player's already drunk
	private int curDrunkSeq = -1; // current sequence to drink
	private int maxDrinkingCnt = 0; // maximum drinking count which this player
									// can drink

	private List<Drinking> drinkings = new ArrayList<Drinking>(); // drinkings
																	// info. to
																	// drink

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

	public Player(String name, int drinkingTime) {
		this.name = name;
		this.drinkingTime = drinkingTime;
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
			Logger.debug(nSecond + " / add drinking:" + name + " ("
					+ (this.drinkings.size() - 1) + ")");
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * drinking operation
	 * </pre>
	 * 
	 * @param int nSecond second for logging
	 * @return boolean finished this drinking or not
	 */
	public boolean drinking(int nSecond) {
		// when nothing to drink, return false
		if (getLeftDrinkingTime() == 0)
			return false;

		// get the left time to drink this drinking
		int dringLeftTime = this.drinkings.get(curDrunkSeq).drinking();
		Logger.debug(nSecond + " / " + this.getName()
				+ " is drinking up to :" + dringLeftTime
				+ ". and has next turn " + (this.drinkings.size() - 1));

		// recalculate current drinking sequence (curDrunkSeq)
		if (dringLeftTime == 0) {
			Logger.debug(nSecond + " / finished drinking:" + name + " ("
					+ curDrunkSeq + ")");
			if ((this.drinkings.size() - 1) > curDrunkSeq) {
				curDrunkSeq++;
			}
			drunkCnt++;
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
	 * get the left sequence to drink this drinking
	 * </pre>
	 * 
	 * @return left time to drink
	 */
	public int getLeftDrinkingCnt() {
		return drunkCnt - curDrunkSeq;
	}

	/**
	 * <pre>
	 * get dice vale for display
	 * </pre>
	 * 
	 * @return String
	 */
	public String getDiceDisplayVale() {
		String tmp[] = diceVale.split(",");
		if(tmp[0].equals(tmp[1])) {
			return "double " + tmp[0] + "'s";
		}
		return "a " + Integer.toString(Integer.parseInt(tmp[0]) + Integer.parseInt(tmp[1]));
	}
	
	public int getDrunkCnt() {
		return drunkCnt;
	}

	public void setDrunkCnt(int drunkCnt) {
		this.drunkCnt = drunkCnt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDrinkingTime() {
		return drinkingTime;
	}

	public void setDrinkingTime(int drinkingTime) {
		this.drinkingTime = drinkingTime;
	}

	public String getDiceVale() {
		return diceVale;
	}

	public void setDiceVale(String diceVale) {
		this.diceVale = diceVale;
	}

	public List<Drinking> getDrinkings() {
		return drinkings;
	}

	public int getMaxDrinkingCnt() {
		return maxDrinkingCnt;
	}

	public void setMaxDrinkingCnt(int maxDrinkingCnt) {
		this.maxDrinkingCnt = maxDrinkingCnt;
	}
}
