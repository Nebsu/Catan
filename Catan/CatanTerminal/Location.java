package Catan.CatanTerminal;

import java.util.ArrayList;

class Location {

    ////////// Attributs ////////// 
    
    protected final Box[] boxes;
    protected Path[] neighborPaths;
    protected final int indexI;
    protected final int indexJ;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    Location(Box[] boxes, int i, int j) {
        if (boxes.length<=0 || boxes.length==3 || boxes.length>4) 
            throw new IllegalArgumentException();
        // boxes.length peut seulement être égal à 1, 2 et 4 
        this.boxes = boxes;
        this.indexI = i;
        this.indexJ = j;
        this.neighborPaths = this.setNeighborPaths();
    }
    protected Path[] setNeighborPaths() {
        ArrayList<Path> neighbors = new ArrayList<Path>();
        // Route à gauche :
        try {
            neighbors.add(CatanTerminal.PLAYBOARD.horizontalPaths[this.indexI][this.indexJ-1]);
        } catch (Exception e) {}
        // Route à droite :
        try {
            neighbors.add(CatanTerminal.PLAYBOARD.horizontalPaths[this.indexI][this.indexJ]);
        } catch (Exception e) {}
        // Route en haut :
        try {
            neighbors.add(CatanTerminal.PLAYBOARD.verticalPaths[this.indexI-1][this.indexJ]);
        } catch (Exception e) {}
        // Route en bas :
        try {
            neighbors.add(CatanTerminal.PLAYBOARD.verticalPaths[this.indexI][this.indexJ]);
        } catch (Exception e) {}
        Path[] paths = new Path[neighbors.size()];
        for (int i=0; i<neighbors.size(); i++) 
            paths[i] = neighbors.get(i);
        return paths;
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {return ("OO");}

    // Renvoie true si l'emplacement est lié à une route du joueur en argument :
    protected boolean isLinkedToYourRoads(Player player) {
        for (int k=0; k<this.neighborPaths.length; k++) {
            if (this.neighborPaths[k] instanceof Road
            && ((Road) this.neighborPaths[k]).player==player)
                return true;
        }
        return false;
    }


    ////////// Fonctions des ports ////////// 

    // Renvoie true si l'intersection est à côté d'un port :
    protected boolean hasAnHarbor() {
        return (this.getHarbor()!=null);
    }

    // Si ce dernier existe, cette fonction renvoie le port en question :
    protected Harbor getHarbor() {
        for (int i=0; i<CatanTerminal.PLAYBOARD.seaBoxes.length; i++) {
            if (CatanTerminal.PLAYBOARD.seaBoxes[i] instanceof Harbor) {
                if (CatanTerminal.PLAYBOARD.seaBoxes[i].loc1==this || 
                    CatanTerminal.PLAYBOARD.seaBoxes[i].loc2==this) 
                    return ((Harbor) CatanTerminal.PLAYBOARD.seaBoxes[i]);
            }
        }
        return null;
    }

}