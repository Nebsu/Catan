package Catan;

final class IA extends Player {
    
    public IA(String name, int s) {
        super(name, s);
        // TODO
    }

    @Override
    public boolean isWinner() {return super.isWinner();}

    @Override
    public void play(PlayBoard p) {
        // TODO Auto-generated method stub
    }

    @Override
    protected boolean canConstructColony(PlayBoard p) {return super.canConstructColony(p);}

    @Override
    protected boolean canConstructCity() {return super.canConstructCity();}

    @Override
    protected boolean canConstructRoad(PlayBoard p) {return super.canConstructRoad(p);}

    @Override
    protected boolean hasEnoughToBuyACard() {return super.hasEnoughToBuyACard();}

    @Override
    protected void buildColony(PlayBoard p) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void buildCity(PlayBoard p) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void buildRoad(PlayBoard p) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void useSpecialCard() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void buySpecialCard() {
        // TODO Auto-generated method stub
    }

}