package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.PlayerRepPosition;
import it.polimi.se2019.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class EndGameController {

	@FXML
	private TableView<Score> tablePoints;
	@FXML
	private TableColumn<Score, String> rankColumn;
	@FXML
	private TableColumn<Score, String> nicknameColumn;
	@FXML
	private TableColumn<Score, String> pointsColumn;

	private ObservableList<Score> finalScores = FXCollections.observableArrayList();

	void setValues(Stage stage, List<PlayerRepPosition> finalPlayersInfo) {

		stage.setOnCloseRequest(event -> System.exit(0));
		for (int i = 0; i < finalPlayersInfo.size(); i++) {
			for (int j = 0; j < finalPlayersInfo.get(i).getPlayerReps().size(); j++) {
				PlayerRep playerRep = finalPlayersInfo.get(i).getPlayerReps().get(j);
				finalScores.add(new Score(playerRep.getPlayerName(), i + 1, playerRep.getPoints()));
			}
		}
		Utils.logInfo("Final scores: " + finalScores);

		rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
		nicknameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
		pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
		tablePoints.setItems(finalScores);
	}

	@FXML
	private void closeStage() {
		System.exit(0);
	}

}
