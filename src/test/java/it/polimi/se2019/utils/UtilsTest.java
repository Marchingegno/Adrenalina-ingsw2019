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
		assertEquals((char)27 + "[30;40mTEST" + (char)27 + "[39;49m", Utils.getColoredString("TEST", 30, 40));
	}

	@Test
	public void setColorString_correctInput_correctOutput() {
		assertEquals((char)27 + "[30;40m", Utils.setColorString(30, 40));
	}

	@Test (expected = IllegalArgumentException.class)
	public void setColorString_illegalCharacterColor_shouldThrowException() {
		Utils.setColorString(40, 40);
	}

	@Test (expected = IllegalArgumentException.class)
	public void setColorString_illegalBackgroundColor_shouldThrowException() {
		Utils.setColorString(39, 39);
	}

	@Test
	public void resetColorString_correctInput_correctOutput() {
		assertEquals((char)27 + "[39;49m", Utils.resetColorString());
	}
}