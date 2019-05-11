package it.polimi.se2019.network.client;

import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.CLIPrinter;
import it.polimi.se2019.view.client.CLIView;
import it.polimi.se2019.view.client.GUIView;
import it.polimi.se2019.view.client.RemoteView;

import java.util.Locale;
import java.util.Random;


/**
 * Implements the Client.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Client {

	public static void main(String[] args) {
		// Set English language.
		Locale.setDefault(Locale.ENGLISH);

		CLIPrinter.printLoginScrean();

		if (Utils.DEBUG_BYPASS_CONFIGURATION) {
			RemoteView remoteView = new CLIView();
			if(new Random().nextBoolean())
				remoteView.startConnectionWithRMI();
			else
				remoteView.startConnectionWithSocket();
			return;
		}
		// Start with CLI and ask if the user wants to use CLI or GUI.
		CLIView cliView = new CLIView();
		boolean isGUI = cliView.askForGUI();

		// Start GUI if requested.
		RemoteView remoteView;
		if (isGUI)
			remoteView = new GUIView();
		else
			remoteView = cliView;

		// Ask which connection to use and start it.
		remoteView.askForConnectionAndStartIt();
	}


	/**
	 * Terminate the client.
	 */
	public static void terminateClient() {
		System.exit(0);
	}
}
