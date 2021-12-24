package Catan;

final class Colony extends Location {
    
    protected final Player player;
    protected boolean isCity;
    protected final int id;
    private static int acc = 1;

    Colony(Box[] boxes, int i, int j, Player player) {
        super(boxes, i, j);
        this.player = player;
        this.isCity = false;
        this.id = acc++;
    }

    @Override
    public String toString() {
        String type = (this.isCity)? "La ville" : "La colonie";
        return (type+" numero "+this.id+" appartenant a "+this.player.name);
    }

    @Override
    protected final boolean hasAnHarbor() {
        return super.hasAnHarbor();
    }

}