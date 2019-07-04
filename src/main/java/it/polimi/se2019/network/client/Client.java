package it.polimi.se2019.network.client;

import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.cli.CLIView;
import it.polimi.se2019.view.client.gui.GUIInitializer;

import java.util.Locale;
import java.util.Random;

import static it.polimi.se2019.view.client.cli.CLILoginPrinter.printChooseView;
import static it.polimi.se2019.view.client.cli.CLILoginPrinter.waitForChoiceInMenu;


/**
 * Implements the Client.
 *
 * @author Desno365
 * @author MarcerAndrea
 */
public class Client {

	/**
	 * Starts the Client program.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// Set English language.
		Locale.setDefault(Locale.ENGLISH);

		printChooseView();

		// Bypass server configuration for debug.
		if (Utils.DEBUG_BYPASS_CONFIGURATION) {
			CLIView cliView = new CLIView();
			if (new Random().nextBoolean())
				cliView.startConnectionWithRMI("localhost");
			else
				cliView.startConnectionWithSocket("localhost");
			return;
		}

		// Start with CLI and ask if the user wants to use CLI or GUI.
		boolean isGUI;
		if (args.length == 1 && (args[0].equalsIgnoreCase("gui") || args[0].equalsIgnoreCase("cli")))
			isGUI = args[0].equalsIgnoreCase("gui");
		else isGUI = waitForChoiceInMenu("1", "2").equals("1");

		// Start GUI if requested.
		if (isGUI)
			GUIInitializer.startGUIInitializer();
		else
			new CLIView().askForConnectionAndStartIt();
	}


	/**
	 * Terminate the client.
	 */
	public static void terminateClient() {
		System.exit(0);
	}
}
