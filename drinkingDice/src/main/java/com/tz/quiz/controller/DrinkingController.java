package com.tz.quiz.controller;

import java.util.ArrayList;
import java.util.List;

import com.tz.quiz.domain.Player;
import com.tz.quiz.service.DrinkingService;
import com.tz.quiz.support.Constants;

public class DrinkingController {
	public static void main(String[] args) {

		Constants.radomPlay = false;
		Constants.debug = true;

		// 2) define players
		List<Player> players = new ArrayList<Player>();
		Player player1 = new Player(0, "Alex", 2);
		players.add(player1);

		Player player2 = new Player(1, "Bob", 3);
		players.add(player2);

//		Player player3 = new Player(2, "Chris", 5);
//		players.add(player3);

		// 3) run the game
		DrinkingService game = new DrinkingService();
		try {
			game.playDrinkingGame(players);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
