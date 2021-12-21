package Catan;

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

}