package Catan;

class Path {
    
    protected Player player;
    protected final char position;
    protected final Location startPoint;
    protected final Location endPoint;

    Path(Player player, char position, Location startPoint, Location endPoint) {
        this.player = player;
        this.position = position;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
    Path(char position, Location startPoint, Location endPoint) {
        this(null, position, startPoint, endPoint);
    }

}