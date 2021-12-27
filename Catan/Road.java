package Catan;

import java.util.ArrayList;

final class Road extends Path {

    final Player player;
    final Location startPoint;
    final Location endPoint;

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

    final Location mainPoint() {
        boolean b = this.isAStartRoad(this.point1);
        if (!b) return this.point1;
        else return this.point2;
    }

    final boolean isAStartRoad() {
        boolean point1 = this.isAStartRoad(this.point1);
        boolean point2 = this.isAStartRoad(this.point2);
        return (point1 || point2);
    }

    private final boolean isAStartRoad(Location point) {
        ArrayList<Path> neighbors = getNeighborsPaths(point);
        for (Path path : neighbors) {
            if (path instanceof Road && ((Road)path).player==this.player 
            && ((Road)path)!=this) return false;
        }
        return true;
    }

    protected final boolean hasLinkedRoads() {
        return (!this.getLinkedRoads().isEmpty());
    }

    protected final ArrayList<Road> getLinkedRoads() {
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