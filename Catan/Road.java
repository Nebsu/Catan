package Catan;

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


    ////////// Route la plus longue du joueur courant //////////

    // Renvoie le point qui est en contact avec d'autres routes du joueur :
    final Location mainPoint() {
        boolean b = this.isAStartRoad(this.point1);
        if (!b) return this.point1;
        else return this.point2;
    }

    // Renvoie true si l'un des deux points (celui d'arrivée ou celui de départ) de la route courante
    // est en contact avec aucune autre route du joueur :
    final boolean isAStartRoad() {
        boolean point1 = this.isAStartRoad(this.point1);
        boolean point2 = this.isAStartRoad(this.point2);
        return (point1 || point2);
    }

    // Pour cela on analyse les chemins voisins :
    private final boolean isAStartRoad(Location point) {
        ArrayList<Path> neighbors = getNeighborsPaths(point);
        for (Path path : neighbors) {
            if (path instanceof Road && ((Road)path).player==this.player 
            && ((Road)path)!=this) return false;
        }
        return true;
    }

    // Renvoie true si la route courante est liée à d'autres routes du même joueur :
    final boolean hasLinkedRoads() {
        return (!this.getLinkedRoads().isEmpty());
    }

    // Renvoie la liste des routes d'un même joueur qui sont liées à this :
    final ArrayList<Road> getLinkedRoads() {
        ArrayList<Path> neighbors = new ArrayList<Path>();
        neighbors.addAll(getNeighborsPaths(this.mainPoint()));
        ArrayList<Road> yourRoads = new ArrayList<Road>();
        for (Path path : neighbors) {
            if (path instanceof Road && ((Road)path).player==this.player 
            && ((Road)path)!=this) yourRoads.add((Road)path);
        }
        return yourRoads;
    }

}