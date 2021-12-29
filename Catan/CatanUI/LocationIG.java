package Catan.CatanUI;

class LocationIG {

    ////////// Attributs ////////// 
    
    protected final BoxIG[] boxes;
    protected final int indexI;
    protected final int indexJ;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    LocationIG(BoxIG[] boxes, int i, int j) {
        if (boxes.length<=0 || boxes.length==3 || boxes.length>4) 
            throw new IllegalArgumentException();
        // boxes.length peut seulement être égal à 1, 2 et 4 
        this.boxes = boxes;
        this.indexI = i;
        this.indexJ = j;
    }

    ////////// Fonctions auxiliaires //////////


    ////////// Fonctions des ports ////////// 

    // // Renvoie true si l'intersection est à côté d'un port :
    // protected boolean hasAnHarbor() {
    //     return (this.getHarbor()!=null);
    // }

    // // Si ce dernier existe, cette fonction renvoie le port en question :
    // protected Harbor getHarbor() {
    //     for (int i=0; i<CatanTerminal.PLAYBOARD.seaBoxes.length; i++) {
    //         if (CatanTerminal.PLAYBOARD.seaBoxes[i] instanceof Harbor) {
    //             if (CatanTerminal.PLAYBOARD.seaBoxes[i].loc1==this || 
    //                 CatanTerminal.PLAYBOARD.seaBoxes[i].loc2==this) 
    //                 return ((Harbor) CatanTerminal.PLAYBOARD.seaBoxes[i]);
    //         }
    //     }
    //     return null;
    // }

}