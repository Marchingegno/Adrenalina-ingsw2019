package it.polimi.se2019.network.message;

public enum MessageType {

	// MessageTypes for Match initialization.
	NICKNAME,
	WAITING_PLAYERS,
	TIMER_FOR_START,
	GAME_CONFIG,

	// MessageTypes between VirtualView <=> RemoteView
	SPAWN,
	UPDATE_REPS,
	ACTION,
	ON_TURN_POWERUP,
	MOVE,
	GRAB_WEAPON,
	GRAB_AMMO,
	SHOOT,
	RELOAD,
	WEAPON,
	END_TURN,

	// Powerups
	POWERUP_INFO_OPTIONS,
	POWERUP_INFO_COORDINATES,
}