package Catan;

import java.util.ArrayList;

final class Box {

    ////////// Attributs ////////// 
    
    protected final String name;
    protected final String ressource;
    protected final int number;
    protected final int indexI;
    protected final int indexJ;
    protected boolean hasThief;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    Box(String name, String ressource, int number, int indexI, int indexJ, boolean hasThief) {
        this.name = name;
        this.ressource = ressource;
        this.number = number;
        this.indexI = indexI;
        this.indexJ = indexJ;
        this.hasThief = hasThief;
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        String color = null;
        switch (this.name) {
            case "Foret": color = CatanTerminal.GREEN; break;
            case "Pre": color = CatanTerminal.GREEN_BOLD_BRIGHT; break;
            case "Champs": color = CatanTerminal.YELLOW_BOLD_BRIGHT; break;
            case "Colline": color = CatanTerminal.RED_BOLD; break;
            case "Montagne": color = CatanTerminal.BLACK_BOLD_BRIGHT; break;
            case "Desert": color = CatanTerminal.WHITE_BOLD_BRIGHT; break;
        }
        String s1 = (String.valueOf(this.number).length()==1)? "0" : "";
        String s2 = (this.hasThief)? "^" : " ";
        return (color+s1+String.valueOf(this.number)+CatanTerminal.RESET+s2+color+this.name.substring(0, 2)+CatanTerminal.RESET);
    }

    // Récupération de toutes les intersections adjacentes à la case courante :
    final ArrayList<Location> getLocations() {
        ArrayList<Location> loc = new ArrayList<Location>();
        loc.add(CatanTerminal.PLAYBOARD.locations[this.indexI][this.indexJ]);
        loc.add(CatanTerminal.PLAYBOARD.locations[this.indexI][this.indexJ+1]);
        loc.add(CatanTerminal.PLAYBOARD.locations[this.indexI+1][this.indexJ]);
        loc.add(CatanTerminal.PLAYBOARD.locations[this.indexI+1][this.indexJ+1]);
        return loc;
    }

}