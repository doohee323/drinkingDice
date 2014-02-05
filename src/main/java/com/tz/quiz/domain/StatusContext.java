package com.tz.quiz.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tz.quiz.domain.Player.Drinking;
import com.tz.quiz.support.Constants;
import com.tz.quiz.support.Utils;

/**
 * <pre>
 * model class for context of rolling
 * </pre>
 * 
 */
public class StatusContext {

	private int sn = 0; // current turn's player's sn
	private int nSecond = 0; // time (seconds)
	private int pausetime = Constants.defaultRollSpeed; // rolling time
	private int maxDrinkingCnt = Constants.defaultMaxDrinkingCnt; // maximum
	private int leftDrintCnt = 0; // number of drinking player at this moment
	private List<Player> players = new ArrayList<Player>();
	private boolean bWin = false; // whether has winning value
	private String dropedDrinker = null; // dropped drinker name at last

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
	 * get playerName who is pointed to drink
	 * </pre>
	 * 
	 * @return String	playerName
	 */
	public String getAddedDrinker() {
		Iterator<Player> e = players.iterator();
		while (e.hasNext()) {
			Player player = e.next();
			List<Drinking> drinkings = player.getDrinkings();
			Iterator<Drinking> e1 = drinkings.iterator();
			while (e1.hasNext()) {
				Drinking drinking = e1.next();
				if (drinking.isAdded()){
					return player.getPlayerName();
				}
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * get playerName who drunk
	 * </pre>
	 * 
	 * @return String	playerName
	 */
	public String getFinishedDrinker() {
		Iterator<Player> e = players.iterator();
		while (e.hasNext()) {
			Player player = e.next();
			List<Drinking> drinkings = player.getDrinkings();
			Iterator<Drinking> e1 = drinkings.iterator();
			while (e1.hasNext()) {
				Drinking drinking = e1.next();
				if (drinking.isFinished()){
					return player.getPlayerName();
				}
			}
		}
		return null;
	}

	public void removePlayer(String name) {
		List<Player> newPlayers = Utils.clonePlayers(players, name);
		this.players = newPlayers;
		this.setDropedDrinker(name);
	}

	public Player getCurPlayer() {
		return getPlayerBySn(sn);
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
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
