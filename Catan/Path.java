package Catan;

import java.util.ArrayList;

class Path {

    ////////// Attributs //////////
    
    protected final char position;
    protected Location point1;
    protected Location point2;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    Path(char position, Location point1, Location point2) {
        if (position!='H' && position!='V') throw new IllegalArgumentException();
        this.position = position;
        this.point1 = point1;
        this.point2 = point2;
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        return ((this.position=='H')? "-------" : "||");
    }

    // Renvoie les chemins "voisins" (qui sont à côté) du chemin courant :
    protected static final ArrayList<Path> getNeighborsPaths(Location point) {
        ArrayList<Path> neighbors = new ArrayList<Path>();
        // Route à gauche :
        try {
            neighbors.add(CatanTerminal.PLAYBOARD.horizontalPaths[point.indexI][point.indexJ-1]);
        } catch (Exception e) {}
        // Route à droite :
        try {
            neighbors.add(CatanTerminal.PLAYBOARD.horizontalPaths[point.indexI][point.indexJ]);
        } catch (Exception e) {}
        // Route en haut :
        try {
            neighbors.add(CatanTerminal.PLAYBOARD.verticalPaths[point.indexI-1][point.indexJ]);
        } catch (Exception e) {}
        // Route en bas :
        try {
            neighbors.add(CatanTerminal.PLAYBOARD.verticalPaths[point.indexI][point.indexJ]);
        } catch (Exception e) {}
        return neighbors;
    }

}