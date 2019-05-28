package it.polimi.se2019.network.message;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.List;

public class PaymentMessage extends Message {
    private List<AmmoType> priceToPay;
    private List<Integer> powerupsUsed;
    private boolean canAffordAlsoWithAmmo;

    public PaymentMessage(List<AmmoType> priceToPay, MessageSubtype subtype){
        super(MessageType.PAYMENT, subtype);
        this.priceToPay = priceToPay;
    }

    public List<Integer> getPowerupsUsed() {
        return powerupsUsed;
    }

    public PaymentMessage setPowerupsUsed(List<Integer> powerupsUsed) {
        this.powerupsUsed = powerupsUsed;
        return this;
    }

    public PaymentMessage setCanAffordAlsoWithAmmo(boolean canAffordAlsoWithAmmo) {
        this.canAffordAlsoWithAmmo = canAffordAlsoWithAmmo;
        return this;
    }

    public boolean canAffordAlsoWithAmmo() {
        return canAffordAlsoWithAmmo;
    }

    public List<AmmoType> getPriceToPay() {
        return priceToPay;
    }
}
