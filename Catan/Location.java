class Location {
    
    protected final Box[] boxes;

    Location(Box[] boxes) {
        if (boxes.length<=0 || boxes.length==3) throw new IllegalArgumentException();
        this.boxes = boxes;
        // boxes.length ne peut pas être négatif, nul, ou égal à 3
    }

    @Override
    public String toString() {
        String res = "";
        for (Box b : boxes) res += b.toString()+" ";
        return res;
    }

}