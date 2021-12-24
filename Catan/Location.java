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


    ////////// Fonctions des ports ////////// 

    protected boolean hasAnHarbor() {
        return (this.getHarbor()!=null);
    }
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