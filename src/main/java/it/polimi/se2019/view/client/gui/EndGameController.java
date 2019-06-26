package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.PlayersPosition;
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
	private TableView tablePoints;
	@FXML
	private TableColumn rankColumn;
	@FXML
	private TableColumn nicknameColumn;
	@FXML
	private TableColumn pointsColumn;

	private Stage stage;
	private ObservableList<Scores> finalScores = FXCollections.observableArrayList();

	public void setValues(Stage stage, List<PlayersPosition> finalPlayersInfo) {
		this.stage = stage;
		for (int i = 0; i < finalPlayersInfo.size(); i++) {
			for (int j = 0; j < finalPlayersInfo.get(i).getPlayerReps().size(); j++) {
				PlayerRep playerRep = finalPlayersInfo.get(i).getPlayerReps().get(j);
				finalScores.add(new Scores(playerRep.getPlayerName(), i, playerRep.getPoints()));
			}
		}
		rankColumn.setCellValueFactory(new PropertyValueFactory<Scores, String>("rank"));
		nicknameColumn.setCellValueFactory(new PropertyValueFactory<Scores, String>("nickname"));
		pointsColumn.setCellValueFactory(new PropertyValueFactory<Scores, String>("points"));
	}

	@FXML
	private void closeStage() {
		stage.close();
	}

	class Scores {
		private String nickname;
		private int rank;
		private int points;

		public Scores(String nickname, int rank, int points) {
			this.nickname = nickname;
			this.rank = rank;
			this.points = points;
		}
	}
}
