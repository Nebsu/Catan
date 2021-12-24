package Catan;

final class Harbor extends SeaBox {

    final String type;
    final String ressource;
    final int id;
    private static int count = 1;

    Harbor(Location loc1, Location loc2, String type, String ressource) {
        super(loc1, loc2);
        if (!type.equals("Simple") && !type.equals("Special")) 
            throw new IllegalArgumentException();
        this.type = type;
        this.ressource = ressource;
        this.id = count++;
    }
    Harbor(Location loc1, Location loc2, String type) {
        this(loc1, loc2, type, null);
    }

    @Override
    public String toString() {
        return (CatanTerminal.CYAN_BOLD_BRIGHT+"P"+
        ((this.type=="Simple")? "S?" : this.ressource.substring(0, 2))+
        CatanTerminal.RESET);
    }
    
}