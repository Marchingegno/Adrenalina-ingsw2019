package it.polimi.se2019.network.client;

import it.polimi.se2019.utils.Utils;

public class Client {

	public static void main(String[] args) {

		// TODO if user requested RMI start RMI
		// Start RMI client.
		RMIClientInterface rmiClient = new RMIClient();
		try {
			rmiClient.startRMIClient();
		} catch (Exception e) {
			Utils.logError("Failed connection to server.", e);
		}

		// TODO if user requested Socket start Socket
	}

}
