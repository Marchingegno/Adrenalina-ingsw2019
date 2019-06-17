package it.polimi.se2019.view.client.gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class LobbyController {
	@FXML
	private static ListView nicknames;

	public static void addNickname(List<String> players) {
		ObservableList<String> playerList = observableArrayList(players);
		nicknames.setItems(playerList);
	}
}
