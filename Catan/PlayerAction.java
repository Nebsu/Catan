package Catan;

import java.util.ArrayList;

interface PlayerAction {
    
    void play(PlayBoard p);
    int throwDices();
    void earnResources(int dice, PlayBoard p);
    void buildNativeColonies(PlayBoard p);
    void buildColony(PlayBoard p);
    void buildCity();
    void inventorySetup();
    void buildRoad(PlayBoard p);
    ArrayList<Location> getEndPoints();
    void useSpecialCard();

}