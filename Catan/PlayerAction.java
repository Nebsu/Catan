package Catan.Joueur;

import Catan.Plateau.PlayBoard;

interface PlayerAction {
    
    void play(PlayBoard p);
    int throwDices();
    void earnResources(int dice, PlayBoard p);
    void inventorySetup();
    void useSpecialCard();
    void buildRoad();
    void buildColony(PlayBoard p);
    void buildCity();

}