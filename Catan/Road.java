package Catan;

final class Road extends Path {

    protected final Player player;
    protected final Location startPoint;
    protected final Location endPoint;

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

}