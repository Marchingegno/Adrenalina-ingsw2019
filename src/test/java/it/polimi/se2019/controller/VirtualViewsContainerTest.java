package it.polimi.se2019.controller;

import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.view.server.VirtualView;
import it.polimi.se2019.view.server.VirtualViewDriverSync;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VirtualViewsContainerTest {

	private static List<VirtualView> virtualViews = new ArrayList<>();

	private VirtualViewsContainer virtualViewsContainer;

	@BeforeClass
	public static void oneTimeSetUp() {
		for (int i = 0; i < GameConstants.MIN_PLAYERS; i++) {
			VirtualView virtualView = new VirtualViewDriverSync("player" + i, false, false, false);
			virtualView.onClientDisconnected();
			virtualViews.add(virtualView);
		}
	}

	@Before
	public void setUp() throws Exception {
		virtualViewsContainer = new VirtualViewsContainer(virtualViews);
	}

	@Test
	public void getVirtualViews_noInput_correctOutput() {
		for(VirtualView virtualView : virtualViewsContainer.getVirtualViews()) {
			Assert.assertTrue(virtualViews.contains(virtualView));
		}
	}

	@Test
	public void getVirtualViewFromPlayerName_correctInput_correctOutput() {
		for(VirtualView virtualView : virtualViews) {
			Assert.assertEquals(virtualView, virtualViewsContainer.getVirtualViewFromPlayerName(virtualView.getNickname()));
		}
	}
}