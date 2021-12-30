package Catan.CatanUI;

import javax.swing.JPanel;

final class HarborIG extends SeaBoxIG {

    ////////// Attributs //////////

    final int price;
    final String ressource;
    final String type;
    final int id;
    private static int count = 1;

    ////////// Constructeurs et fonctions associées à ce dernier //////////

    HarborIG(LocationIG loc1, LocationIG loc2, int price, String ressource, JPanel panel) {
        super(loc1, loc2, panel);
        if (ressource==null && price==3) this.type = "Simple";
        else if (ressource!=null && price==2) this.type = "Special";
        else throw new IllegalArgumentException();
        this.price = price;
        this.ressource = ressource;
        this.id = count++;
    }
    HarborIG(LocationIG loc1, LocationIG loc2, JPanel panel) {
        this(loc1, loc2, 3, null, panel);
    }

    ////////// Fonctions auxiliaires //////////
    
}