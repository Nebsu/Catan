package Catan;

import java.util.ArrayList;
import java.util.Random;

import Catan.Exceptions.InexistantColonyException;
import Catan.Exceptions.InexistantRoadException;
import Catan.Exceptions.WrongInputException;

final class IA extends Player {
    
    public IA(String name, int s) {
        super(name, s);
        // TODO
    }

    protected static int throwDices() { 
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        return (dice1+dice2);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // Fonction booleennes :
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
        System.out.println("Au tour de "+this.name+" :");
        int dice = throwDices();
        System.out.println("Resultat du lancer des des : " + dice);
        if (dice!=7) earnResources(dice);
        else {
            System.out.println("Voleur active");
            this.thief();
        }
        CatanTerminal.p.display();
        this.proposeToUseSpecialCard();
        this.proposeToConstructColony();
        this.proposeToConstructCity();
        this.proposeToConstructRoad();
        this.proposeToBuySpecialCard();
    }


    // Fonctions du voleur :
    @Override
    protected void thief() {
        super.thief();
    } 

    @Override
    protected void giveRessources(int n) {
        System.out.println("Vous avez "+n+" ressources");
        n /= 2;
        System.out.println("Veuillez donner "+n+" ressources au voleur");
        boolean notOk = true;
        Random random = new Random();
        String line;
        for (int i=0; i<n; i++) {
            int r = random.nextInt(5);
            if(r == 0){
                line = "Roche";
            }else if(r == 1){
                line = "Laine";
            }else if(r == 2){
                line = "Ble";
            }else if(r == 3){
                line = "Argile";
            }else line = "Bois";
            notOk = false;
            Integer a = this.inventory.get(line);
            this.inventory.put(line, a-Integer.valueOf(1));
        }
    }

    @Override
    protected Box moveThief() {
        boolean notOk;
        Box res = null;
        do {
            try {
                Random random1 = new Random();
                Random random2 = new Random();
                int n1 = random1.nextInt(6)+1;
                int n2 = random2.nextInt(6)+1;
                int[]indexs = {n1,n2};
                int k = indexs[0]-1; int l = indexs[1]-1;
                if (k<0 || k>4 || l<0 || l>4) throw new WrongInputException();
                notOk = false;
                CatanTerminal.p.boxes[k][l].hasThief = true;
                res = CatanTerminal.p.boxes[k][l];
            } catch (Exception e) {
                notOk = true;
            }
        } while (notOk);
        CatanTerminal.p.updatePaths();
        CatanTerminal.p.display();
        return res;
    }

    @Override
    protected Player selectPlayerToStealFrom(Box b) {
        // On recupère les emplacement adjacents de la case :
        ArrayList<Location> locations = b.getLocations();
        // On va verifier si le joueur possède une colonie sur l'un des ces emplacements :
        ArrayList<Colony> colonies = new ArrayList<Colony>();
        for (Location l : locations) {
            if (l instanceof Colony) colonies.add((Colony) l);
        }
        if (colonies.isEmpty()) {
            System.out.println("Aucune colonie à proximite de cette case");
            return null;
        }
        ArrayList<Player> nearPlayers = new ArrayList<Player>();
        for (Colony c : colonies) {
            System.out.println(c);
            if (!nearPlayers.contains(c.player)) nearPlayers.add(c.player);
        }
        ArrayList<String> playersNames = new ArrayList<String>();
        for (Player p : nearPlayers) playersNames.add(p.name);
        boolean notOk = true;
        Player selectedPlayer = null;
        do {
            try {
                Random random = new Random();
                int r = random.nextInt(nearPlayers.size());
                String name = nearPlayers.get(r).name;
                System.out.println(name + " a été volé");
                if (!playersNames.contains(name)) throw new WrongInputException();
                if (name.equals(this.name)) throw new WrongInputException();
                notOk = false;
                for (Player p : nearPlayers) {
                    if (p.name.equals(name)) {
                        selectedPlayer = p;
                        break;
                    }
                }
            } catch (Exception e) {
                notOk = true;
            }
        } while (notOk);
        return selectedPlayer;
    }

    @Override
    protected void steal(Player victim) {
        super.steal(victim);
    }


    // Fonctions de proposition :
    // Proposition de construction d'une colonie : 
    @Override
    protected void proposeToConstructColony() {
        if (this.canConstructColony()) {
            Random r = new Random();
            int n = r.nextInt(2);
            if(n==0){
                this.buildColony(false);
            }else{
                return;
            }
        }
    }

    // Proposition de construction d'une ville :
    @Override
    protected void proposeToConstructCity() {
        if (this.canConstructCity()) {
            Random r = new Random();
            int n = r.nextInt(2);
            if(n==0){
                this.buildCity();
            }else{
                return;
            }
        }
    }

    // Proposition de construction d'une route :
    @Override
    protected void proposeToConstructRoad() {
        if (this.canConstructRoad()) {
            Random r = new Random();
            int n = r.nextInt(2);
            if(n==0){
                this.buildRoad(false);
            }else{
                return;
            }
        }
    }

    // Proposition d'utilisation d'une carte developpement :
    @Override
    protected void proposeToUseSpecialCard() {
        if (!this.specialCards.isEmpty()) {
            Random r = new Random();
            int n = r.nextInt(2);
            if(n==0){
                this.useSpecialCard();
            }else{
                return;
            }
        }
    }

    // Proposition d'achat d'une carte developpement :
    @Override
    protected void proposeToBuySpecialCard() {
        if (!this.specialCards.isEmpty()) {
            Random r = new Random();
            int n = r.nextInt(2);
            if(n==0){
                this.buySpecialCard();
            }else{
                return;
            }
        }
    }


    // Fonctions de construction :
    // Construction d'une colonie :
    @Override
    protected void buildColony(boolean isFree) {
        while(true){
            try{
                Random random1 = new Random();
                Random random2 = new Random();
                int n1 = random1.nextInt(6)+1;
                int n2 = random2.nextInt(6)+1;
                int[]indexs = {n1,n2};
                int k = indexs[0]; int l = indexs[1];
                if (k-1<0 || k-1>4 || l-1<0 || l-1>4) throw new IndexOutOfBoundsException();
                if (CatanTerminal.p.locations[k-1][l-1] instanceof Colony) throw new WrongInputException();
                CatanTerminal.p.locations[k-1][l-1] = new Colony(CatanTerminal.p.locations[k-1][l-1].boxes, k-1, l-1, this);
                this.coloniesOnPlayBoard.add((Colony) CatanTerminal.p.locations[k-1][l-1]);
                if (!isFree) {
                    this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                    this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
                    this.inventory.replace("Ble", this.inventory.get("Ble")-1); 
                    this.inventory.replace("Argile", this.inventory.get("Argile")-1);
                }
                this.victoryPoints++;
                CatanTerminal.p.updatePaths();
                CatanTerminal.p.display();
                return;
            }catch(Exception e){}
        }
    }

    // Construction d'une ville :

    //Pas sur si ça marche
    @Override
    protected void buildCity() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Colony col : this.coloniesOnPlayBoard) {
            ids.add(col.id);
        }
        boolean notOk = true;
        do {   
            try {   
                Random random1 = new Random();
                int selectedId = random1.nextInt(ids.get(ids.size()-1))+1;
                if (!ids.contains(selectedId)) throw new WrongInputException();
                notOk = false;
                for (Colony col : this.coloniesOnPlayBoard) {
                    if (col.id==selectedId) {
                        col.isCity = true;
                        ((Colony) CatanTerminal.p.locations[col.indexI][col.indexJ]).isCity = true;
                        this.inventory.replace("Roche", this.inventory.get("Roche")-3); 
                        this.inventory.replace("Ble", this.inventory.get("Ble")-2); 
                        this.victoryPoints++;
                        break;
                    }
                }
            } catch (Exception e) {}
        } while (notOk);
        CatanTerminal.p.display();
    }

    // Construction d'une route :
    @Override
    protected void buildRoad(boolean isFree) {
        boolean notOk = true;
        char c;
        do {
            try {
                Random random1 = new Random();
                Random random2 = new Random();
                Random random3 = new Random();
                int n1 = random1.nextInt(5);
                int n2 = random2.nextInt(5);
                int n3 = random3.nextInt(2);
                int[]indexs = {n1,n2};
                int k = indexs[0]; int l = indexs[1];
                if(n3 == 0){
                    c = 'H';
                    if (k<0 || k>4 || l<0 || l>3) throw new IndexOutOfBoundsException();
                }else{
                    c = 'V';
                    if (k<0 || k>3 || l<0 || l>4) throw new IndexOutOfBoundsException();
                }

                if ((c=='H' && CatanTerminal.p.horizontalPaths[k][l] instanceof Road) || (c=='V' && CatanTerminal.p.verticalPaths[k][l] instanceof Road)) {
                    notOk = true;
                } else {
                    try {
                        Road r = this.buildRoadNextToColony(c, (c=='H')? CatanTerminal.p.horizontalPaths[k][l] : CatanTerminal.p.verticalPaths[k][l]);
                        if (c=='H') CatanTerminal.p.horizontalPaths[k][l] = r;
                        else CatanTerminal.p.verticalPaths[k][l] = r;
                        this.roads.add(r);
                        if (!isFree) {
                            this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                            this.inventory.replace("Argile", this.inventory.get("Argile")-1); 
                        }
                        notOk = false;
                    } catch (InexistantColonyException ice) {
                        notOk = true;
                        try {
                            Road r = this.buildRoadNextToRoad(c, (c=='H')? CatanTerminal.p.horizontalPaths[k][l] : CatanTerminal.p.verticalPaths[k][l]);
                            if (c=='H') CatanTerminal.p.horizontalPaths[k][l] = r;
                            else CatanTerminal.p.verticalPaths[k][l] = r;
                            this.roads.add(r);
                            if (!isFree) {
                                this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                                this.inventory.replace("Argile", this.inventory.get("Argile")-1); 
                            }
                            notOk = false;
                        } catch (InexistantRoadException ire) {
                            notOk = true;
                        }
                    }
                }
            } catch (Exception e) {
                notOk = true;
            }
        } while (notOk);
        CatanTerminal.p.display();
    }

    protected Road buildRoadNextToColony(char c, Path selectedPath) throws InexistantColonyException {
        return super.buildRoadNextToColony(c, selectedPath);
    }

    protected Road buildRoadNextToRoad(char c, Path selectedPath) throws InexistantRoadException {
        return super.buildRoadNextToRoad(c, selectedPath);
    }

    protected ArrayList<Location> getEndPoints() {
        return super.getEndPoints();
    }


    // Fonctions des cartes developpement :
    // Utilisation d'une carte developpement :
    @Override
    protected void useSpecialCard() {
        boolean notOk = true;
        do {
            Random random = new Random();
            int n = random.nextInt(5)+1;
            notOk = false;
            for(Card c : this.specialCards){
                if(c.id==1 && n==1){
                    // Bouge le voleur
                    this.specialCards.remove(c);
                    this.knights++;
                    if (this.knights==3 && !CatanTerminal.army) {
                        CatanTerminal.army = true;
                        this.victoryPoints += 2;
                        CatanTerminal.hasTheStrongestArmy = this;
                    }
                }else if(c.id==3 && n==3){
                    this.specialCards.remove(c);
                    invention();
                }else if(c.id==2 && n==2){
                    this.specialCards.remove(c);
                    carteRoute();
                }else if(c.id==4 && n==4){
                    this.specialCards.remove(c);
                    monopole();
                }
            }
        } while (notOk);
    }
    // Carte progrès invention :
    @Override
    protected void invention() {
        boolean notOk = true;
        int acc = 1;
        String s;
        do{
            Random random = new Random();
            int n = random.nextInt(5);
            if(n == 0){
                s = "Roche";
            }else if(n == 1){
                s = "Laine";
            }else if(n == 2){
                s = "Ble";
            }else if(n == 3){
                s = "Argile";
            }else s = "Bois";
            this.inventory.replace(s, this.inventory.get(s)+1);
            acc = 2;
            if(acc == 2)notOk = false;
        }while (notOk && acc != 2);
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
        int acc = 0;
        String s;
        while(true){
            Random random = new Random();
            int n = random.nextInt(5);
            if(n == 0){
                s = "Roche";
            }else if(n == 1){
                s = "Laine";
            }else if(n == 2){
                s = "Ble";
            }else if(n == 3){
                s = "Argile";
            }else s = "Bois";
            for(Player p : CatanTerminal.players){
                if(p!=this){
                    acc += p.inventory.get(s);
                    p.inventory.replace(s, 0);
                }
            }
            this.inventory.replace(s, this.inventory.get(s) + acc);
            return;
        }
    }

    // Achat d'une carte developpement :
    @Override
    protected void buySpecialCard() {
        super.buySpecialCard();
    }

    // Route la plus longue du joueur courant :
    @Override
    protected int longestRoad() {
        return super.longestRoad();
    }

}