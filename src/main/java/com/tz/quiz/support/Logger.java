package com.tz.quiz.support;

import java.io.PrintWriter;

import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.StatusContext;

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
	 * logging context
	 * </pre>
	 * 
	 */
	public void logStatus(StatusContext context) {
		// if (Constants.debug)
		// return;

		String addedDrinker = context.getAddedDrinker(); // added drinker name at last
		String finishedDrinker = context.getFinishedDrinker(); // finished drinker name at last
		String dropedDrinker = context.getDropedDrinker(); // dropped drinker name at last
		
		if (addedDrinker != null
				|| finishedDrinker != null
				|| dropedDrinker != null) {
		} else {
			return;
		}

		boolean bSpecial = false;
		Player curPlayer = context.getCurPlayer();
		this.println("==== STATUS ====");
		this.println("There are " + context.getPlayers().size() + " players.");
		this.println("It is " + curPlayer.getPlayerName() + "'s turn.");
		for (int i = 0; i < context.getPlayers().size(); i++) {
			Player player = context.getPlayers().get(i);
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
		if (addedDrinker != null) {
			this.println(curPlayer.getPlayerName() + " says: '"
					+ addedDrinker + ", drink!'");
			bSpecial = true;
		}
		if (finishedDrinker != null) {
			this.println(finishedDrinker + " is done drinking.");
			bSpecial = true;
		}
		if (dropedDrinker != null) {
			this.println(dropedDrinker
					+ " says: 'I've had too many. I need to stop.'");
			bSpecial = true;
		}
		this.println("\n");
		if (bSpecial) {
			this.flush();
		}
		//context.setDropedDrinker(null);
	}

	/**
	 * <pre>
	 * logging end
	 * </pre>
	 * 
	 */
	public void logEnd(StatusContext context) {
		// if (Constants.debug)
		// return;

		if (context.getFinishedDrinker() != null) {
			this.println(context.getFinishedDrinker() + " is done drinking.");
		}
		if (context.getDropedDrinker() != null) {
			this.println(context.getDropedDrinker()
					+ " says: 'I've had too many. I need to stop.'");
		}
		this.println("\n");

		Player curPlayer = context.getCurPlayer();
		this.println("==== STATUS ====");
		this.println("The game is over. " + curPlayer.getPlayerName()
				+ "is the winner.");
		this.println("\n");
		this.println(curPlayer.getPlayerName() + "is the winner!");
		this.flush();
	}

}
