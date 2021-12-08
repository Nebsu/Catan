package Catan;

class Box {
    
    protected final String name;
    protected final String ressource;
    protected final int number;
    protected final int indexI;
    protected final int indexJ;
    protected boolean hasColony;

    Box(String name, String ressource, int number, int indexI, int indexJ) {
        this.name = name;
        this.ressource = ressource;
        this.number = number;
        this.indexI = indexI;
        this.indexJ = indexJ;
        this.hasColony = false;
    }

    protected boolean hasColony() {return this.hasColony;}
    protected void setColony(boolean value) {this.hasColony = value;}

    @Override
    public String toString() {
        String s = (String.valueOf(this.number).length()==1)? "0" : "";
        return (s+String.valueOf(this.number)+" "+this.name.substring(0, 2));
    }

    Location[] getLocations(PlayBoard p) {
        Location[] res = new Location[4];
        res[0] = p.locations[this.indexI][this.indexJ];
        res[1] = p.locations[this.indexI][this.indexJ+1];
        res[2] = p.locations[this.indexI+1][this.indexJ];
        res[3] = p.locations[this.indexI+1][this.indexJ+1];
        return res;
    }

}