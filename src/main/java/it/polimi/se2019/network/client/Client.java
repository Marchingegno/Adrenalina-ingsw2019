package it.polimi.se2019.network.client;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.client.rmi.RMIClient;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.network.message.UpperCaseInputMessage;
import it.polimi.se2019.network.message.UpperCaseOutputMessage;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Client implements ClientInterface{

	private ConnectionInterface connection;

	public static void main(String[] args) {
		boolean isRMI = true; // TODO if user requested RMI start RMI otherwise socket
		if(isRMI) {
			Client client = new Client();
			client.startConnectionWithRMI();
		} else {
			Client client = new Client();
			client.startConnectionWithSocket();
		}
	}

	/**
	 * Receive and process the message sent by the server both by socket or by RMI.
	 * @param message the message received.
	 */
	@Override
	public void processMessage(Message message) {
		// TODO process the message and update the view

		// ********** JUST FOR TEST ************
		try {
			if(message.getMessageType() == MessageType.REQUEST_FOR_UPPER_CASE) {
				System.out.println("Type something and it will converted in upper case.");
				Scanner s = new Scanner(System.in);
				String input = s.nextLine();
				connection.sendMessage(new UpperCaseInputMessage(input));
			}
			if(message.getMessageType() == MessageType.OUTPUT_FOR_UPPER_CASE) {
				System.out.println(((UpperCaseOutputMessage)message).getContent());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void startConnectionWithRMI() {
		try {
			RMIClient rmiClient = new RMIClient();
			rmiClient.startRMIClient(this);
			connection = rmiClient;
			connection.registerClient();
		} catch (Exception e) {
			Utils.logError("Failed connection to server.", e);
		}
	}

	public void startConnectionWithSocket() {
		// TODO create a socket connection and save it in the attribute "connection"
	}
}
