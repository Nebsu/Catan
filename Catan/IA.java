package Catan;

final class IA extends Player {
    
    public IA(String name, int s) {
        super(name, s);
        // TODO
    }

    @Override
    public boolean isWinner() {return super.isWinner();}

    @Override
    public void play() {
        // TODO Auto-generated method stub
    }

    @Override
    protected boolean canConstructColony() {return super.canConstructColony();}

    @Override
    protected boolean canConstructCity() {return super.canConstructCity();}

    @Override
    protected boolean canConstructRoad() {return super.canConstructRoad();}

    @Override
    protected boolean hasEnoughToBuyACard() {return super.hasEnoughToBuyACard();}

    @Override
    protected void buildColony(boolean isFree) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void buildCity() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void buildRoad(boolean isFree) {
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