package Catan.CatanUI;

import java.util.ArrayList;

import javax.swing.JPanel;

final class RoadIG extends PathIG {

    ////////// Attributs //////////

    final PlayerIG player;
    final LocationIG startPoint;
    final LocationIG endPoint;

    ////////// Constructeur et fonctions associées à ce dernier //////////

    RoadIG(PlayerIG player, char position, LocationIG point1, LocationIG point2, int n, JPanel panel) {
        super(position, point1, point2, panel);
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


    ////////// Route la plus longue du joueur courant //////////

    // Renvoie le point qui est en contact avec d'autres routes du joueur :
    final LocationIG mainPoint() {
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
    private final boolean isAStartRoad(LocationIG point) {
        ArrayList<PathIG> neighbors = getNeighborsPaths(point);
        for (PathIG path : neighbors) {
            if (path instanceof RoadIG && ((RoadIG)path).player==this.player 
            && ((RoadIG)path)!=this) return false;
        }
        return true;
    }

    // Renvoie true si la route courante est liée à d'autres routes du même joueur :
    final boolean hasLinkedRoads() {
        if (this.getLinkedRoads()==null) return false;
        return (!this.getLinkedRoads().isEmpty());
    }

    // Renvoie la liste des routes d'un même joueur qui sont liées à this :
    final ArrayList<RoadIG> getLinkedRoads() {
        LocationIG mainPoint = this.mainPoint();
        if (mainPoint instanceof ColonyIG
        && ((ColonyIG) mainPoint).player!=this.player) return null;
        ArrayList<PathIG> neighbors = new ArrayList<PathIG>();
        neighbors.addAll(getNeighborsPaths(mainPoint));
        ArrayList<RoadIG> yourRoads = new ArrayList<RoadIG>();
        for (PathIG path : neighbors) {
            if (path instanceof RoadIG && ((RoadIG)path).player==this.player 
            && ((RoadIG)path)!=this) yourRoads.add((RoadIG)path);
        }
        return yourRoads;
    }

}