package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.CoordinatesAnswerMessage;
import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.CardinalDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the Newton powerup
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public class Newton extends PowerupCard {

	private int progress = 0;
	private Player targetPlayer;

	private static final String DESCRIPTION =
			"You may play this card on your turn before or\n" +
					"after any action. Choose any other player's\n" +
					"figure and move it 1 or 2 squares in one\n" +
					"direction. (You can't use this to move a figure\n" +
					"after it respawns at the end of your turn. That\n" +
					"would be too late.)";

	public Newton(AmmoType associatedAmmo) {
		super("Newton", associatedAmmo, DESCRIPTION, PowerupUseCaseType.ON_TURN);
	}


	@Override
	public PowerupInfo doPowerupStep(Message answer) {
		if(progress == 0) {
			progress++;
			List<String> playerNames = getGameBoard().getPlayers().stream()
					.filter(player -> player != getOwnerPlayer())
					.map(Player::getPlayerName)
					.collect(Collectors.toList());

			PowerupInfo powerupInfo = new PowerupInfo();
			powerupInfo.setAskOptions("Choose the player to move.", playerNames);
			return powerupInfo;
		} else if(progress == 1) {
			progress++;
			IntMessage intMessage = (IntMessage) answer;
			targetPlayer = getGameBoard().getPlayers().get(intMessage.getContent());
			Coordinates startingPoint = getGameBoard().getGameMap().getPlayerCoordinates(targetPlayer);

			PowerupInfo powerupInfo = new PowerupInfo();
			powerupInfo.setAskCoordinates("Enter where to move " + targetPlayer.getPlayerName() + ".", getMovingCoordinates(startingPoint));
			return powerupInfo;
		} else if(progress == 2) {
			progress = 0;
			Coordinates targetCoordinates = ((CoordinatesAnswerMessage) answer).getSingleCoordinates();
			List<Coordinates> allowedCoordinates = getMovingCoordinates(getGameBoard().getGameMap().getPlayerCoordinates(targetPlayer));
			if(allowedCoordinates.contains(targetCoordinates))
				getGameBoard().getGameMap().movePlayerTo(targetPlayer, targetCoordinates);
		}
		progress = 0;
		return null;
	}

	/**
	 * Returns true if there is at least one not dead player different from the activating player.
	 * Needs gameBoard != null and activatingPlayer != null
	 * @return true if there is at least one not dead player different from the activating player.
	 */
	@Override
	public boolean canBeActivated() {
		for(Player player : getGameBoard().getPlayers()) {
			if(player != getOwnerPlayer() && !player.getPlayerBoard().isDead()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Newton";
	}


	private List<Coordinates> getMovingCoordinates(Coordinates startingPoint){
		ArrayList<Coordinates> possibleMoves = new ArrayList<>();
		for (CardinalDirection direction : CardinalDirection.values()) {
			Coordinates nextSquare = getGameBoard().getGameMap().getCoordinatesFromDirection(startingPoint, direction);
			if(nextSquare != null) {
				possibleMoves.add(nextSquare);
				Coordinates nextNextSquare = getGameBoard().getGameMap().getCoordinatesFromDirection(nextSquare, direction);
				if (nextNextSquare != null)
					possibleMoves.add(nextNextSquare);
			}
		}
		return possibleMoves;
	}

}