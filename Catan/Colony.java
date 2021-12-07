package Plateau;

import .Joueur;

class Colony extends Location {
    
    protected Player player;
    protected boolean isCity;

    Colony(Box[] boxes, Player player) {
        super(boxes);
        this.player = player;
        this.isCity = false;
    }

    void convertIntoCity() {this.isCity = true;}

    @Override
    public String toString() {
        String type = (this.isCity)? "Ville" : "Colonie";
        return (type+" appartenant à "+this.player.name);
    }

}