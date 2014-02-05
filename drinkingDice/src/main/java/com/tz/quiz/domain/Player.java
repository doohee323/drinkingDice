package com.tz.quiz.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import com.tz.quiz.support.Constants;
import com.tz.quiz.support.Logger;

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
	private Status status = new Status();

	private List<Drinking> drinkings = new ArrayList<Drinking>(); // drinkings

	public Player(int sn, String name, int drinkingTime) {
		this.sn = sn;
		this.playerName = name;
		this.drinkingTime = drinkingTime;
	}

	@Override
	public Status call() throws Exception {
		try {
			synchronized (status) {
				int pauseTime = status.getPausetime();
				int nSecond = status.getnSecond();
				//Logger.debug(nSecond + " : I'm " + playerName);
				if (bTurn) {
					if (nSecond == 0 || (nSecond % pauseTime) == 0) {
						dice();
						boolean bWin = Constants.isWin(diceVale);
						if (bWin) {
							status.setbWin(true);
							// choose driker at ramdon
							int indx = getDrinkers(status, sn);
							if (indx >= 0) {
								status.getPlayerBySn(indx).addDrinking();
								status.addLeftDrintCnt();
								status.setAddedDrinker(status.getPlayerBySn(
										indx).getPlayerName());
							}
						}
					}
				} else {
					// calculate for drinker's drinking time
					if (drinkings.size() > 0) {
						if (drinking()) { // true => finished
							status.redueLeftDrintCnt();
							// once finished drinking, can join rolling again
							status = Constants.findNextDicer(status);

							status.setFinishedDrinker(playerName);
						}
						if (drunkCnt == status.getMaxDrinkingCnt()
								&& getLeftDrinkingTime() == 0) {
							Logger.debug(nSecond + " / droped off :"
									+ playerName);
							status.removePlayer(playerName);
							status = Constants.findNextDicer(status);

							status.setDropedDrinker(playerName);
						}
					}
				}

				// check exist drinking player
				boolean bDrinking = status.getLeftDrintCnt() > 0 ? true : false;
				if (!bDrinking) {
					// if else, find the next player who is'nt drinking
					status = Constants.findNextDicer(status);
				}
				
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return status;
	}

	/**
	 * <pre>
	 * drinking status of players
	 * </pre>
	 */
	public class Drinking {
		private boolean added = false; // added drinking
		private boolean finished = false; // finished drinking
		private int secondToDrink = 0;

		public Drinking() {
			secondToDrink = drinkingTime;
			added = true;
		}

		public int getSecondToDrink() {
			return secondToDrink;
		}

		public int drinking() {
			if (this.secondToDrink > 0)
				this.secondToDrink--;
			added = false;
			return this.secondToDrink;
		}

		public boolean isAdded() {
			return added;
		}

		public void setAdded(boolean added) {
			this.added = added;
		}

		public boolean isFinished() {
			return finished;
		}

		public void setFinished(boolean finished) {
			this.finished = finished;
		}
	}

	/**
	 * <pre>
	 * check next drinking to drink
	 * </pre>
	 * 
	 * @return boolean whether is thing to drink
	 */
	public boolean isNextDrinking() {
		if (curDrunkSeq < 0)
			return false;
		if (!drinkings.get(curDrunkSeq).isAdded()
				&& !drinkings.get(curDrunkSeq).isFinished()) {
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
	public boolean drinking() {
		// when nothing to drink, return false
		if (getLeftDrinkingTime() == 0) {
			return false;
		}

		// skill at first second
		if (this.drinkings.get(curDrunkSeq).isAdded()) {
			this.drinkings.get(curDrunkSeq).setAdded(false);
			return false;
		}

		// get the left time to drink this drinking
		int dringLeftTime = this.drinkings.get(curDrunkSeq).drinking();
		Logger.debug(status.getnSecond() + " / " + this.playerName
				+ " is drinking up to :" + dringLeftTime
				+ ". and has next turn " + (this.drinkings.size() - 1));

		// recalculate current drinking sequence (curDrunkSeq)
		if (dringLeftTime == 0) {
			Logger.debug(status.getnSecond() + " / finished drinking:"
					+ playerName + " (" + curDrunkSeq + ")");
			this.drinkings.get(curDrunkSeq).setFinished(true);
			if ((this.drinkings.size() - 1) > curDrunkSeq) {
				curDrunkSeq++;
			}
			drunkCnt++;
			return true;
		}
		return false;
	}

	public int getDrinkers(Status status, int self) {
		int maxDrintCnt = status.getMaxDrinkingCnt();

		// choose driker at ramdon
		List<Player> players = new ArrayList<Player>();
		Iterator<Player> e = status.getPlayers().iterator();
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
	 * add a drinking to drink to the list
	 * </pre>
	 * 
	 * @param int nSecond second for logging
	 * @return boolean add drinking to list or not
	 */
	public boolean addDrinking() {
		if (this.drinkings.size() < status.getMaxDrinkingCnt()) {
			// when finished this drinking, move to the next drinking
			if ((this.drinkings.size() - 1) == curDrunkSeq
					&& getLeftDrinkingTime() == 0) {
				curDrunkSeq++;
			}

			this.drinkings.add(new Drinking());
			Logger.debug(status.getnSecond() + " / add drinking:" + playerName
					+ " (" + (this.drinkings.size() - 1) + ")");

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

	/**
	 * <pre>
	 * get dice vale for display
	 * </pre>
	 * 
	 * @return String
	 */
	public String getDiceDisplayVale() {
		String tmp[] = diceVale.split(",");
		if (tmp[0].equals(tmp[1])) {
			return "double " + tmp[0] + "'s";
		}
		return "a "
				+ Integer.toString(Integer.parseInt(tmp[0])
						+ Integer.parseInt(tmp[1]));
	}

	/**
	 * <pre>
	 * get the left sequence to drink this drinking
	 * </pre>
	 * 
	 * @return left time to drink
	 */
	public int getLeftDrinkingCnt() {
		return drinkings.size() - curDrunkSeq;
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
