package com.tz.quiz.support;

import java.io.PrintWriter;

import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.Status;

public class Logger extends PrintWriter {

	private PrintWriter out = null;
	private StringBuffer logs = null;

	/**
	 * <pre>
	 * constructor for logger
	 * </pre>
	 */
	public Logger() {
		super(System.out, true);
		out = new PrintWriter(System.out);
		logs = new StringBuffer();
	}

	/**
	 * <pre>
	 * append logs to StringBuffer
	 * </pre>
	 * 
	 * @param strInput
	 */
	public void println(String strInput) {
		synchronized (lock) {
			logs.append(strInput + "\n");
		}
	}

	/**
	 * <pre>
	 * prinlnt StringBuffer to console
	 * </pre>
	 * 
	 */
	public void flush() {
		synchronized (lock) {
			out.print(logs);
			out.flush();
			logs = new StringBuffer();
		}
	}

	/**
	 * <pre>
	 * print log to console for debugging
	 * </pre>
	 * 
	 * @param strInput
	 */
	public static void debug(String strInput) {
		if (Constants.debug) {
			System.out.println("\t" + strInput);
		}
	}

	/**
	 * <pre>
	 * logging status
	 * </pre>
	 * 
	 */
	public void logStatus(Status status) {
		// if (Constants.debug)
		// return;

		if (status.isbWin() || status.getAddedDrinker() != null
				|| status.getFinishedDrinker() != null
				|| status.getDropedDrinker() != null) {
		} else {
			return;
		}

		boolean bSpecial = false;
		Player curPlayer = status.getCurPlayer();
		this.println("==== STATUS ====");
		this.println("There are " + status.getPlayers().size() + " players.");
		this.println("It is " + curPlayer.getPlayerName() + "'s turn.");
		for (int i = 0; i < status.getPlayers().size(); i++) {
			Player player = status.getPlayers().get(i);
			if (player.isNextDrinking()
					&& player.getLeftDrinkingTime() != player.getDrinkingTime()) {
				this.println(player.getPlayerName() + " has had "
						+ player.getDrunkCnt()
						+ " drinks and is currently drinking "
						+ player.getLeftDrinkingCnt() + " more.");
				bSpecial = true;
			} else {
				this.println(player.getPlayerName() + " has had "
						+ player.getDrunkCnt() + " drinks.");
			}
		}
		this.println("\n");
		this.println(curPlayer.getPlayerName() + "'s turn.");
		this.println("\n");
		if (curPlayer.getDiceDisplayVale() != null) {
			this.println(curPlayer.getPlayerName() + " rolled "
					+ curPlayer.getDiceDisplayVale());
		}
		// got assignment
		if (status.getAddedDrinker() != null) {
			this.println(curPlayer.getPlayerName() + " says: '"
					+ status.getAddedDrinker() + ", drink!'");
			bSpecial = true;
		}
		if (status.getFinishedDrinker() != null) {
			this.println(status.getFinishedDrinker() + " is done drinking.");
			bSpecial = true;
		}
		if (status.getDropedDrinker() != null) {
			this.println(status.getDropedDrinker()
					+ " says: 'I've had too many. I need to stop.'");
			bSpecial = true;
		}
		this.println("\n");
		if (bSpecial) {
			this.flush();
		}
		status.setAddedDrinker(null);
		status.setFinishedDrinker(null);
		status.setDropedDrinker(null);
	}


}
