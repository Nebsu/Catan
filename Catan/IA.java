package Catan;

import java.util.ArrayList;

class IA extends Player {
    
    public IA(String name, int s) {
        super(name, s);
    }

    public int throwDices() {
        return super.throwDices();
    }

    public void earnResources(int dice, PlayBoard p) {
        // TODO Auto-generated method stub
        super.earnResources(dice, p);
    }

    public void inventorySetup() {
        // TODO Auto-generated method stub
        super.inventorySetup();
    }

    public void useSpecialCard() {
        // TODO Auto-generated method stub
        super.useSpecialCard();
    }

    public void buildRoad(PlayBoard p) {
        // TODO Auto-generated method stub
        super.buildRoad(p);
    }

    public ArrayList<Location> getEndPoints() {
        // TODO Auto-generated method stub
        return super.getEndPoints();
    }   

    public void buildColony(PlayBoard p) {
        // TODO Auto-generated method stub
        super.buildColony(p);
    }

    public void buildCity() {
        // TODO Auto-generated method stub
        super.buildCity();
    }

}