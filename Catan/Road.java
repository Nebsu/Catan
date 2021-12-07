class Road {
    
    protected final Player player;
    protected final char position;
    protected final Location startPoint;
    protected final Location endPoint;

    Road(Player player, char position, Location startPoint, Location endPoint) {
        this.player = player;
        this.position = position;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
    Road(char position) {this(null, position, null, null);}

}