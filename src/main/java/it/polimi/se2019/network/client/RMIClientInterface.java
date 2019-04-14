package it.polimi.se2019.network.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface RMIClientInterface {

	void startRMIClient() throws RemoteException, NotBoundException;
}
