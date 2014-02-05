package com.tz.quiz;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tz.quiz.domain.Player;
import com.tz.quiz.domain.StatusContext;
import com.tz.quiz.service.DrinkingService;
import com.tz.quiz.support.Constants;
import com.tz.quiz.support.Logger;
import com.tz.test.TestSupport;

/**
 * 
 * @author
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
// @Ignore
public class PlayerTest extends TestSupport {

	@Test
	public void runtTest() {
		DrinkingService service = new DrinkingService();
		StatusContext context = new StatusContext();

		// 1) making game
		List<Player> players = new ArrayList<Player>();

		Constants.randomPlay = false;
		Constants.debug = false;

		given("with @ players,", "3");
		{
			service.setMaxDrinkingCnt(2);
			service.setPausetime(2);

			// 2) define players
			Player player1 = new Player(0, "Alex", 2);
			Logger.debug("0 / ready:" + player1.getName() + " ("
					+ player1.getDrinkingTime() + ")");
			players.add(player1);
			Player player2 = new Player(1, "Bob", 3);
			Logger.debug("0 / ready:" + player2.getName() + " ("
					+ player2.getDrinkingTime() + ")");
			players.add(player2);
			Player player3 = new Player(2, "Chris", 5);
			Logger.debug("0 / ready:" + player3.getName() + " ("
					+ player3.getDrinkingTime() + ")");
			players.add(player3);
		}

		when("run service.drinkingPlayer");
		{
			context = service.playDrinkingGame(players);
		}

		then(context.getPlayers().size() + "'s winner is only 1.");
		{
			assertThat(context.getPlayers().size(), is(1));
		}
	}

}