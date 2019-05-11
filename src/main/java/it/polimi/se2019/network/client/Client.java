package it.polimi.se2019.network.client;

import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.CLIPrinter;
import it.polimi.se2019.view.client.CLIView;
import it.polimi.se2019.view.client.GUIView;
import it.polimi.se2019.view.client.RemoteView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static it.polimi.se2019.view.client.CLIPrinter.printChooseView;
import static it.polimi.se2019.view.client.CLIPrinter.waitForChoiceInMenu;


/**
 * Implements the Client.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Client {

	public static void main(String[] args) {
		// Set English language.
		Locale.setDefault(Locale.ENGLISH);

		CLIPrinter.printChooseView();

		if (Utils.DEBUG_BYPASS_CONFIGURATION) {
			RemoteView remoteView = new CLIView();
			if(new Random().nextBoolean())
				remoteView.startConnectionWithRMI();
			else
				remoteView.startConnectionWithSocket();
			return;
		}
		// Start with CLI and ask if the user wants to use CLI or GUI.
		printChooseView();
		ArrayList<String> possibleChoices = new ArrayList<>();
		possibleChoices.add("1");
		possibleChoices.add("2");
		boolean isGUI = waitForChoiceInMenu(possibleChoices).equals("1");

		// Start GUI if requested.
		RemoteView remoteView;
		if (isGUI)
			remoteView = new GUIView();
		else
			remoteView = new CLIView();

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
