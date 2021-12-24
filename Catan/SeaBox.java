package Catan;

class SeaBox {
 
    protected final Location loc1;
    protected final Location loc2;

    SeaBox(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    @Override
    public String toString() {
        return (CatanTerminal.CYAN_BOLD_BRIGHT+"~~~"+CatanTerminal.RESET);
    }

}