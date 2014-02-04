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
	
	private int sn = 0; // current turn of player

	private String diceVale = "";

	public String getDiceVale() {
		return diceVale;
	}

	public void setDiceVale(String diceVale) {
		this.diceVale = diceVale;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

}
