package Catan;

final class IA extends Player {
    
    public IA(String name, int s) {
        super(name, s);
        // TODO
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // Fonction booléennes :
    @Override
    public boolean isWinner() {return super.isWinner();}

    @Override
    protected boolean isRich() {
        return super.isRich();
    }

    @Override
    protected boolean canConstructColony() {return super.canConstructColony();}

    @Override
    protected boolean canConstructCity() {return super.canConstructCity();}

    @Override
    protected boolean canConstructRoad() {return super.canConstructRoad();}

    @Override
    protected boolean hasEnoughToBuyACard() {return super.hasEnoughToBuyACard();}


    // Fonction principale :
    @Override
    public void play() {
        // TODO Auto-generated method stub
    }


    // Fonctions du voleur :
    @Override
    protected void thief() {
        // TODO Auto-generated method stub
    } 

    @Override
    protected void giveRessources(int n) {
        // TODO Auto-generated method stub
    }

    @Override
    protected Box moveThief() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Player selectPlayerToStealFrom(Box b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void steal(Player victim) {
        super.steal(victim);
    }


    // Fonctions de proposition :
    // Proposition de construction d'une colonie : 
    @Override
    protected void proposeToConstructColony() {
        // TODO Auto-generated method stub
    }

    // Proposition de construction d'une ville :
    @Override
    protected void proposeToConstructCity() {
        // TODO Auto-generated method stub
    }

    // Proposition de construction d'une route :
    @Override
    protected void proposeToConstructRoad() {
        // TODO Auto-generated method stub
    }

    // Proposition d'utilisation d'une carte développement :
    @Override
    protected void proposeToUseSpecialCard() {
        // TODO Auto-generated method stub
    }

    // Proposition d'achat d'une carte développement :
    @Override
    protected void proposeToBuySpecialCard() {
        // TODO Auto-generated method stub
    }


    // Fonctions de construction :
    // Construction d'une colonie :
    @Override
    protected void buildColony(boolean isFree) {
        // TODO Auto-generated method stub
    }

    // Construction d'une ville :
    @Override
    protected void buildCity() {
        // TODO Auto-generated method stub
    }

    // Construction d'une route :
    @Override
    protected void buildRoad(boolean isFree) {
        // TODO Auto-generated method stub
    }


    // Fonctions des cartes développement :
    // Utilisation d'une carte développement :
    @Override
    protected void useSpecialCard() {
        // TODO Auto-generated method stub
    }
    // Carte progrès invention :
    @Override
    protected void invention() {
        // TODO Auto-generated method stub
    }
    // Carte progrès route :
    @Override
    protected void carteRoute() {
        this.buildRoad(true);
        this.buildRoad(true);
    }
    // Carte monopole :
    @Override
    protected void monopole() {
        // TODO Auto-generated method stub
    }

    // Achat d'une carte développement :
    @Override
    protected void buySpecialCard() {
        // TODO Auto-generated method stub
    }

    // Route la plus longue du joueur courant :
    @Override
    protected int longestRoad() {
        return super.longestRoad();
    }

}