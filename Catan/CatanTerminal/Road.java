package Catan.CatanTerminal;

import java.util.ArrayList;

final class Road extends Path {

    ////////// Attributs //////////

    final Player player;
    final Location startPoint;
    final Location endPoint;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    Road(Player player, char position, Location point1, Location point2, int n) {
        super(position, point1, point2);
        this.player = player;
        if (n!=1 && n!=2) throw new IllegalArgumentException();
        if (n==1) {
            this.startPoint = this.point1;
            this.endPoint = this.point2;
        } else {
            this.startPoint = this.point2;
            this.endPoint = this.point1;
        }
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        if (this.position=='H')
            return (this.player.color+"RRRRRRR"+CatanTerminal.RESET);
        else 
            return (this.player.color+"RR"+CatanTerminal.RESET);
    }


    ////////// Route la plus longue du joueur courant //////////

    // Renvoie true si la route courante est liée à d'autres routes du même joueur :
    final boolean hasLinkedRoads() {
        return (this.getLinkedRoads()!=null);
    }

    // Renvoie la liste des routes d'un même joueur qui sont liées à this :
    final ArrayList<Road> getLinkedRoads() {
        ArrayList<Road> yourRoads = new ArrayList<Road>();
        yourRoads.addAll(this.getLinkedRoads(this.point1));
        yourRoads.addAll(this.getLinkedRoads(this.point2));
        if (yourRoads.isEmpty()) return null;
        return yourRoads;
    }

    private ArrayList<Road> getLinkedRoads(Location point) {
        ArrayList<Road> roads = new ArrayList<Road>();
        // Colonie adverse
        if (point instanceof Colony 
        && ((Colony) point).player!=this.player) return roads;
        for (int i=0; i<point.neighborPaths.length; i++) {
            if (point.neighborPaths[i] instanceof Road
            &&  ((Road) point.neighborPaths[i]).player==this.player
            &&  ((Road) point.neighborPaths[i])!=this)
                roads.add((Road) point.neighborPaths[i]);
        }
        return roads;
    }

}