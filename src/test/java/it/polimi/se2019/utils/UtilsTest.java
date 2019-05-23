package it.polimi.se2019.utils;

import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * @author Desno365
 */
public class UtilsTest {

	@Test
	public void getGlobalLogger_normalState_correctOutput() {
		assertEquals(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), Utils.getGlobalLogger());
	}

	@Test
	public void getColoredString() {
		assertEquals((char)27 + "[30;40mTEST" + (char)27 + "[39;49m", Color.getColoredString("TEST", Color.CharacterColorType.BLACK, Color.BackgroundColorType.BLACK));
	}

	@Test
	public void fillWithSpaces_test() {
		System.out.println(Utils.fillWithSpaces("p", 45) + "|");
		System.out.println(Utils.fillWithSpaces("pp", 45) + "|");
		System.out.println(Utils.fillWithSpaces("ppp", 45) + "|");
		System.out.println(Utils.fillWithSpaces("pppp", 45) + "|");
		System.out.println(Utils.fillWithSpaces("ppppp", 45) + "|");
		System.out.println(Utils.fillWithSpacesColored("pppppp", 45, Color.CharacterColorType.RED) + "|");
		System.out.println(Utils.fillWithSpaces("pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp", 45));
	}
}