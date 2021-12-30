package Catan.CatanUI;

import javax.swing.JPanel;

class SeaBoxIG {
 
    ////////// Attributs //////////

    protected final LocationIG loc1;
    protected final LocationIG loc2;
    protected final JPanel panel;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    SeaBoxIG(LocationIG loc1, LocationIG loc2, JPanel panel) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.panel = panel;
    }


}