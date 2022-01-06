package Catan.CatanTerminal;

final class Colony extends Location {

    ////////// Attributs //////////
    
    final Player player;
    boolean isCity;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    Colony(Box[] boxes, int i, int j, Player player) {
        super(boxes, i, j);
        this.player = player;
        this.isCity = false;
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        String type = (this.isCity)? "V" : "C";
        return (this.player.color+type+this.player.symbol+CatanTerminal.RESET);
    }


    ////////// Fonctions des ports ////////// 

    // Renvoie true si la colonie est à côté d'un port :
    @Override
    protected final boolean hasAnHarbor() {return super.hasAnHarbor();}

    // Si ce dernier existe, cette fonction renvoie le port en question :
    @Override
    protected Harbor getHarbor() {return super.getHarbor();}

}