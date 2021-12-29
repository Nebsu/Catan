package Catan.CatanUI;

import java.util.ArrayList;

import javax.swing.JPanel;

final class BoxIG {

    ////////// Attributs ////////// 
    
    protected final String name;
    protected final String ressource;
    protected final int number;
    protected final int indexI;
    protected final int indexJ;
    protected boolean hasThief;
    protected JPanel panel;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    BoxIG(String name, String ressource, int number, int indexI, int indexJ, boolean hasThief, JPanel panel) {
        this.name = name;
        this.ressource = ressource;
        this.number = number;
        this.indexI = indexI;
        this.indexJ = indexJ;
        this.hasThief = hasThief;
        this.panel = panel;
    }

    ////////// Fonctions auxiliaires //////////

    // Récupération de toutes les intersections adjacentes à la case courante :
    final ArrayList<LocationIG> getLocations() {
        ArrayList<LocationIG> loc = new ArrayList<LocationIG>();
        loc.add(Game.PLAYBOARD.locations[this.indexI][this.indexJ]);
        loc.add(Game.PLAYBOARD.locations[this.indexI][this.indexJ+1]);
        loc.add(Game.PLAYBOARD.locations[this.indexI+1][this.indexJ]);
        loc.add(Game.PLAYBOARD.locations[this.indexI+1][this.indexJ+1]);
        return loc;
    }

}