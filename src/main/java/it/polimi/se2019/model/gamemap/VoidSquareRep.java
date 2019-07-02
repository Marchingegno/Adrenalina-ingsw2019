package it.polimi.se2019.model.gamemap;


public class VoidSquareRep extends SquareRep {

    public VoidSquareRep(VoidSquare voidSquareToRepresent) {
        super(voidSquareToRepresent);
    }

    @Override
    public String[] getElementsToPrint() {
        return new String[]{" ", " ", " "};
    }
}
