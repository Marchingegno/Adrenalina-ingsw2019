package it.polimi.se2019.network.client;

import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.cli.CLIView;
import it.polimi.se2019.view.client.gui.GUIController;
import javafx.application.Application;

import java.util.Locale;
import java.util.Random;

import static it.polimi.se2019.view.client.cli.CLIPrinter.printChooseView;
import static it.polimi.se2019.view.client.cli.CLIPrinter.waitForChoiceInMenu;


/**
 * Implements the Client.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Client {

	public static void main(String[] args) {
		// Set English language.
		Locale.setDefault(Locale.ENGLISH);

		printChooseView();

		// Bypass server configuration for debug.
		if (Utils.DEBUG_BYPASS_CONFIGURATION) {
			CLIView cliView = new CLIView();
			if (new Random().nextBoolean())
				cliView.startConnectionWithRMI();
			else
				cliView.startConnectionWithSocket();
			return;
		}

		// Start with CLI and ask if the user wants to use CLI or GUI.
		boolean isGUI = waitForChoiceInMenu("1", "2").equals("1");

		// Start GUI if requested.
		if (isGUI)
			new Thread(() -> {
				Application.launch(GUIController.class);
			}).start();
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
