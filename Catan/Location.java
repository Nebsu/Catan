package Catan;

class Location {
    
    protected final Box[] boxes;
    protected final int indexI;
    protected final int indexJ;

    Location(Box[] boxes, int i, int j) {
        if (boxes.length<=0 || boxes.length==3) throw new IllegalArgumentException();
        // boxes.length ne peut pas être négatif, nul, ou égal à 3
        this.boxes = boxes;
        this.indexI = i;
        this.indexJ = j;
    }

    @Override
    public String toString() {
        return ("L'emplacement "+(this.indexI+1)+(this.indexJ+1));
    }

}