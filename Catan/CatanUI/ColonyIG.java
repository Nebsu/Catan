package Catan.CatanUI;

import java.awt.Color;

final class ColonyIG extends LocationIG {

    ////////// Attributs //////////
    
    final PlayerIG player;
    boolean isCity;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    ColonyIG(BoxIG[] boxes, int i, int j, PlayerIG player, int x, int y, PlayBoardIG playboard) {
        super(boxes, i, j, x, y, playboard);
        this.player = player;
        this.isCity = false;
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        String type = (this.isCity)? "V" : "C";
        return (type+this.player.symbol);
    }


    ////////// Fonctions des ports ////////// 

    // Renvoie true si la colonie est à côté d'un port :
    @Override
    protected final boolean hasAnHarbor() {return super.hasAnHarbor();}

    // Si ce dernier existe, cette fonction renvoie le port en question :
    @Override
    protected HarborIG getHarbor() {return super.getHarbor();}

}