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
	MOVE,
	GRAB_WEAPON,
	SWAP_WEAPON,
	GRAB_AMMO,
	RELOAD,
	ACTIVATE_WEAPON,
	WEAPON,
	ACTIVATE_POWERUP,
	POWERUP,
	PAYMENT,
	END_TURN,
}