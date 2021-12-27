package Catan;

import java.util.ArrayList;

class Path {
    
    protected final char position;
    protected Location point1;
    protected Location point2;

    Path(char position, Location point1, Location point2) {
        if (position!='H' && position!='V') throw new IllegalArgumentException();
        this.position = position;
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public String toString() {
        String pos = (this.position=='H')? "horizontal" : "vertical";
        return ("Chemin "+pos+" qui relie "+this.point1.toString()+
                " et "+this.point2.toString());
    }


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