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
	SHOOT, // Equivalent to ACTIVATE_POWERUP but for weapons.
	WEAPON, // Equivalent to POWERUP but for weapons.
	ACTIVATE_POWERUP, // Equivalent to SHOOT but for powerups.
	POWERUP, // Equivalent to WEAPON but for powerups.
	END_TURN,
}