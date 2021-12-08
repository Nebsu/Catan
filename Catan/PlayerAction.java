package Catan;

interface PlayerAction {
    
    void play(PlayBoard p);
    int throwDices();
    void earnResources(int dice, PlayBoard p);
    void inventorySetup();
    void useSpecialCard();
    void buildRoad();
    void buildNativeColonies(PlayBoard p);
    void buildColony(PlayBoard p);
    void buildCity();

}