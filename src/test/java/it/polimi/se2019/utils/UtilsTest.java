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
}