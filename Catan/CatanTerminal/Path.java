package Catan.CatanTerminal;

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

}