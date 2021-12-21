package Catan;

import java.util.ArrayList;

final class Box {
    
    protected final String name;
    protected final String ressource;
    protected final int number;
    protected final int indexI;
    protected final int indexJ;
    protected boolean hasThief;

    Box(String name, String ressource, int number, int indexI, int indexJ, boolean hasThief) {
        this.name = name;
        this.ressource = ressource;
        this.number = number;
        this.indexI = indexI;
        this.indexJ = indexJ;
        this.hasThief = hasThief;
    }

    @Override
    public String toString() {
        String s = (String.valueOf(this.number).length()==1)? "0" : "";
        return (s+String.valueOf(this.number)+" "+this.name.substring(0, 2));
    }

    final ArrayList<Location> getLocations() {
        ArrayList<Location> loc = new ArrayList<Location>();
        loc.add(CatanTerminal.p.locations[this.indexI][this.indexJ]);
        loc.add(CatanTerminal.p.locations[this.indexI][this.indexJ+1]);
        loc.add(CatanTerminal.p.locations[this.indexI+1][this.indexJ]);
        loc.add(CatanTerminal.p.locations[this.indexI+1][this.indexJ+1]);
        return loc;
    }

}