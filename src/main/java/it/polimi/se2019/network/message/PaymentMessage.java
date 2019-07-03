package it.polimi.se2019.network.message;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.List;

/**
 * Message used for requesting payments with ammo cubes or powerups.
 * @author MarcerAndrea
 */
public class PaymentMessage extends Message {
	private List<AmmoType> priceToPay;
	private List<Integer> powerupsUsed;
	private boolean canAffordAlsoWithAmmo;


	/**
	 * Constructs a message.
	 *
	 * @param priceToPay a list containing the price the player has to pay.
	 * @param subtype    the messageSubtype of the message.
	 */
	public PaymentMessage(List<AmmoType> priceToPay, MessageSubtype subtype) {
		super(MessageType.PAYMENT, subtype);
		this.priceToPay = priceToPay;
	}


	/**
	 * Returns the list of powerups to use as payment.
	 *
	 * @return the list of powerups to use as payment.
	 */
	public List<Integer> getPowerupsUsed() {
		return powerupsUsed;
	}

	/**
	 * Sets the list containing the powerups to use for payment.
	 *
	 * @param powerupsUsed the list containing the powerups to use for payment.
	 * @return the PaymentMessage itself.
	 */
	public PaymentMessage setPowerupsUsed(List<Integer> powerupsUsed) {
		this.powerupsUsed = powerupsUsed;
		return this;
	}

	/**
	 * Sets if the message can be afforded also with only ammo cubes.
	 *
	 * @param canAffordAlsoWithAmmo true if the message can be afforded also with only ammo cubes.
	 * @return the PaymentMessage itself.
	 */
	public PaymentMessage setCanAffordAlsoWithAmmo(boolean canAffordAlsoWithAmmo) {
		this.canAffordAlsoWithAmmo = canAffordAlsoWithAmmo;
		return this;
	}

	/**
	 * Returns true if the payment can also be afforded with only ammo cubes.
	 *
	 * @return true if the payment can also be afforded with only ammo cubes.
	 */
	public boolean canAffordAlsoWithAmmo() {
		return canAffordAlsoWithAmmo;
	}

	/**
	 * Returns a list containing the price to pay.
	 *
	 * @return a list containing the price to pay.
	 */
	public List<AmmoType> getPriceToPay() {
		return priceToPay;
	}
}
