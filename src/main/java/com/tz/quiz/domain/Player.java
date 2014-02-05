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
	private Status status = null;

	private List<Drinking> drinkings = null; // drinkings

	/**
	 * <pre>
	 * constructor for player
	 * </pre>
	 * 
	 * @param int sn player's sn
	 * @param String
	 *            name player's name
	 * @param int drinkingTime player's time to drink
	 */
	public Player(int sn, String name, int drinkingTime) {
		this.sn = sn;
		this.playerName = name;
		this.drinkingTime = drinkingTime;
	}

	/**
	 * <pre>
	 * callable method
	 * </pre>
	 * 
	 * @return Status
	 */
	@Override
	public Status call() throws Exception {
		try {
			synchronized (status) {
				int pauseTime = status.getPausetime();
				int nSecond = status.getnSecond();
				// Logger.debug(nSecond + " : I'm " + playerName);
				if (bTurn) {
					if (nSecond == 0 || (nSecond % pauseTime) == 0) {
						dice();
						boolean bWin = Constants.isWin(diceVale);
						if (bWin) {
							status.setbWin(true);
							// choose driker at ramdon
							int indx = getDrinkers(sn);
							if (indx >= 0) {
								status.getPlayerBySn(indx).addDrinking();
								status.addLeftDrintCnt();
								status.setAddedDrinker(status.getPlayerBySn(
										indx).getPlayerName());

								// print status
								logStatus();
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

							// print status
							logStatus();
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

		// print status
		logStatus();

		return false;
	}

	/**
	 * <pre>
	 * get next drinker's sn except self
	 * </pre>
	 * 
	 * @param int self dicer's sn
	 * @return int next drinker's sn
	 */
	public int getDrinkers(int self) {
		int maxDrintCnt = status.getMaxDrinkingCnt();

		List<Player> players = new ArrayList<Player>();
		Iterator<Player> e = status.getPlayers().iterator();
		while (e.hasNext()) {
			Player player = e.next();
			if (player.getSn() != self
					&& player.getDrinkings().size() < maxDrintCnt) {
				players.add(player);
			}
		}

		// choose driker at ramdon
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
		if (diceVale == null)
			return null;
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
	 * logging status
	 * </pre>
	 * 
	 */
	public void logStatus() {
		// if (Constants.debug)
		// return;

		if (status.isbWin() || status.getAddedDrinker() != null
				|| status.getFinishedDrinker() != null
				|| status.getDropedDrinker() != null) {
		} else {
			return;
		}

		Player curPlayer = status.getCurPlayer();
		logger.println("==== STATUS ====");
		logger.println("There are " + status.getPlayers().size() + " players.");
		logger.println("It is " + curPlayer.getPlayerName() + "'s turn.");
		for (int i = 0; i < status.getPlayers().size(); i++) {
			Player player = status.getPlayers().get(i);
			if (player.isNextDrinking()
					&& player.getLeftDrinkingTime() != player.getDrinkingTime()) {
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
		if (curPlayer.getDiceDisplayVale() != null) {
			logger.println(curPlayer.getPlayerName() + " rolled "
					+ curPlayer.getDiceDisplayVale());
		}
		// got assignment
		if (status.getAddedDrinker() != null) {
			logger.println(curPlayer.getPlayerName() + " says: '"
					+ status.getAddedDrinker() + ", drink!'");
		}
		if (status.getFinishedDrinker() != null) {
			logger.println(status.getFinishedDrinker() + " is done drinking.");
		}
		if (status.getDropedDrinker() != null) {
			logger.println(status.getDropedDrinker()
					+ " says: 'I've had too many. I need to stop.'");
		}
		logger.println("\n");
		logger.flush();
		status.setAddedDrinker(null);
		status.setFinishedDrinker(null);
		status.setDropedDrinker(null);
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

	private Logger logger = new Logger(); // print the logging
}
