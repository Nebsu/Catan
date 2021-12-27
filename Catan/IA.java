package Catan;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import Catan.Exceptions.*;

final class IA extends Player {

    ////////// Constructeur et fonctions associées à ce dernier //////////
    
    public IA(String name, int s) {
        super(name, s);
    }

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {
        return super.toString();
    }

    // Renvoie le nombre de ressources total du joueur :
    @Override
    protected int nbRessources(String ressource) {
        return super.nbRessources(ressource);
    }

    protected void gainInitialResources() {
        super.gainInitialResources();
    }


    ////////// Fonctions booléennes //////////

    @Override
    protected boolean isWinner() {return super.isWinner();}

    @Override
    protected boolean canConstructColony() {return super.canConstructColony();}

    @Override
    protected boolean canConstructCity() {return super.canConstructCity();}

    @Override
    protected boolean canConstructRoad() {return super.canConstructRoad();}

    @Override
    protected boolean canBuyACard() {return super.canBuyACard();}

    @Override
    protected boolean canExchange(int price, String ressource) {return super.canExchange(price, ressource);}

    @Override
    protected boolean canUseHarbor() {return super.canUseHarbor();}

    @Override
    protected boolean onlyContainsSimpleHarbour() {return super.onlyContainsSimpleHarbour();}



    ////////// FONCTIONS DU JEU //////////

    // Fonction principale (tour de l'IA) :
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
        CatanTerminal.PLAYBOARD.display();
        this.proposeToUseSpecialCard();
        this.proposeToConstructColony();
        this.proposeToConstructCity();
        this.proposeToConstructRoad();
        this.proposeToBuySpecialCard();
    }


    // Lancé des dés :
    private static int throwDices() { 
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        return (dice1+dice2);
    }


    ////////// Fonctions du voleur //////////

    // Fonction principale du voleur ;
    @Override
    protected void thief() {super.thief();} 

    // Donner des ressources au voleur :
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

    // Déplacer le voleur :
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
                CatanTerminal.PLAYBOARD.boxes[k][l].hasThief = true;
                CatanTerminal.PLAYBOARD.thief.hasThief = false;
                CatanTerminal.PLAYBOARD.thief = CatanTerminal.PLAYBOARD.boxes[k][l];
                res = CatanTerminal.PLAYBOARD.boxes[k][l];
            } catch (Exception e) {
                notOk = true;
            }
        } while (notOk);
        CatanTerminal.PLAYBOARD.updatePaths();
        CatanTerminal.PLAYBOARD.display();
        return res;
    }

    // Choix de la cible du joueur pour voler des ressources :
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
            System.out.println("Aucune colonie a proximite de cette case");
            return null;
        }
        ArrayList<Player> nearPlayers = new ArrayList<Player>();
        for (Colony c : colonies) {
            System.out.println(c);
            if (!nearPlayers.contains(c.player)) nearPlayers.add(c.player);
        }
        ArrayList<String> playersNames = new ArrayList<String>();
        for (Player PLAYBOARD : nearPlayers) playersNames.add(PLAYBOARD.name);
        boolean notOk = true;
        Player selectedPlayer = null;
        do {
            try {
                Random random = new Random();
                int r = random.nextInt(nearPlayers.size());
                String name = nearPlayers.get(r).name;
                System.out.println(name + " a ete vole");
                if (!playersNames.contains(name)) throw new WrongInputException();
                if (name.equals(this.name)) throw new WrongInputException();
                notOk = false;
                for (Player PLAYBOARD : nearPlayers) {
                    if (PLAYBOARD.name.equals(name)) {
                        selectedPlayer = PLAYBOARD;
                        break;
                    }
                }
            } catch (Exception e) {
                notOk = true;
            }
        } while (notOk);
        return selectedPlayer;
    }

    // Voler une ressource au hasard au joueur choisi :
    @Override
    protected void steal(Player victim) {super.steal(victim);}


    ////////// Fonctions de proposition //////////

    // Proposition d'échange de 4 ressources du joueur contre une de son choix,
    // (échange réalisable du moment que le joueur a au moins 4 ressources,
    //  même si ce dernier ne possède pas de port) :
    @Override
    protected void proposeToExchange41() {
        if (canExchange(4, null)) {
            Random random = new Random();
            int r = random.nextInt(2);
            do {
                try {
                    if (r == 0){
                        this.exchange(4, null);
                        System.out.println("Votre inventaire :"+this.inventory);
                    }break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

    // Proposition d'utilisation d'un port pour faire un échange :
    @Override
    protected void proposeToUseHarbor() {
        if (this.canUseHarbor()) {
            Random random = new Random();
            int r = random.nextInt(2);
            do {
                try {
                    if (r == 0){
                        this.useHarbor();
                        System.out.println("Votre inventaire :"+this.inventory);
                    }break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

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
                this.buildRoad(false, false);
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


    ////////// Fonctions de construction //////////

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
                if (CatanTerminal.PLAYBOARD.locations[k-1][l-1] instanceof Colony) throw new WrongInputException();
                CatanTerminal.PLAYBOARD.locations[k-1][l-1] = new Colony(CatanTerminal.PLAYBOARD.locations[k-1][l-1].boxes, 
                k-1, l-1, (Player) this);
                this.coloniesOnPlayBoard.add((Colony) CatanTerminal.PLAYBOARD.locations[k-1][l-1]);
                if (!isFree) {
                    this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                    this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
                    this.inventory.replace("Ble", this.inventory.get("Ble")-1); 
                    this.inventory.replace("Argile", this.inventory.get("Argile")-1);
                }
                this.victoryPoints++;
                CatanTerminal.PLAYBOARD.updatePaths();
                CatanTerminal.PLAYBOARD.display();
                return;
            }catch(Exception e){}
        }
    }

    // Construction d'une ville : (Pas sur si ça marche)
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
                        ((Colony) CatanTerminal.PLAYBOARD.locations[col.indexI][col.indexJ]).isCity = true;
                        this.inventory.replace("Roche", this.inventory.get("Roche")-3); 
                        this.inventory.replace("Ble", this.inventory.get("Ble")-2); 
                        this.victoryPoints++;
                        break;
                    }
                }
            } catch (Exception e) {}
        } while (notOk);
        CatanTerminal.PLAYBOARD.updatePaths();
        CatanTerminal.PLAYBOARD.display();
    }

    // Construction d'une route :
    @Override
    protected void buildRoad(boolean isFree, boolean beginning) {
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

                if ((c=='H' && CatanTerminal.PLAYBOARD.horizontalPaths[k][l] instanceof Road) || (c=='V' && CatanTerminal.PLAYBOARD.verticalPaths[k][l] instanceof Road)) {
                    notOk = true;
                } else {
                    try {
                        Road r = this.buildRoadNextToColony(c, (c=='H')? CatanTerminal.PLAYBOARD.horizontalPaths[k][l] : CatanTerminal.PLAYBOARD.verticalPaths[k][l], beginning);
                        if (c=='H') CatanTerminal.PLAYBOARD.horizontalPaths[k][l] = r;
                        else CatanTerminal.PLAYBOARD.verticalPaths[k][l] = r;
                        this.roads.add(r);
                        if (!isFree) {
                            this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                            this.inventory.replace("Argile", this.inventory.get("Argile")-1); 
                        }
                        notOk = false;
                    } catch (InexistantColonyException ice) {
                        notOk = true;
                        try {
                            if (beginning) throw new IllegalStateException();
                            Road r = this.buildRoadNextToRoad(c, (c=='H')? CatanTerminal.PLAYBOARD.horizontalPaths[k][l] : CatanTerminal.PLAYBOARD.verticalPaths[k][l]);
                            if (c=='H') CatanTerminal.PLAYBOARD.horizontalPaths[k][l] = r;
                            else CatanTerminal.PLAYBOARD.verticalPaths[k][l] = r;
                            this.roads.add(r);
                            if (!isFree) {
                                this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                                this.inventory.replace("Argile", this.inventory.get("Argile")-1); 
                            }
                            notOk = false;
                        } catch (IllegalStateException e) {
                            notOk = true;
                        } catch (InexistantRoadException ire) {
                            notOk = true;
                        }
                    }
                }
            } catch (Exception e) {
                notOk = true;
            }
        } while (notOk);
        CatanTerminal.PLAYBOARD.display();
    }

    // Construction d'une route à côté d'une colonie :
    @Override
    protected Road buildRoadNextToColony(char c, Path selectedPath, boolean beginning) throws InexistantColonyException {
        return super.buildRoadNextToColony(c, selectedPath, beginning);
    }

    // Construction d'une route à côté d'une autre route :
    @Override
    protected Road buildRoadNextToRoad(char c, Path selectedPath) throws InexistantRoadException {
        return super.buildRoadNextToRoad(c, selectedPath);
    }

    // Fonction pour récupérer les emplacements d'arrivée de toutes les routes
    // construites par le joueur courant :
    @Override
    protected ArrayList<Location> getEndPoints() {
        return super.getEndPoints();
    }


    ////////// Fonctions des ports et du commerce maritime //////////

    // Fonction qui procède à un échange de ressources via le commerce maritime :
    @Override
    protected void exchange(int n, String ressource) {
        if (ressource==null) {
            System.out.println("Choisissez la ressource dont vous voulez donner 4 unites :");
            Set<String> keys = this.inventory.keySet();
            ArrayList<String> selectables = new ArrayList<String>();
            for (String key : keys) {
                if (this.inventory.get(key)>=4) selectables.add(key);
            }
            String s = "";
            int r = 0;
            do {
                try {
                    Random random = new Random();
                    r = random.nextInt(5);
                    if(r == 0){
                        s = "Roche";
                    }else if(r == 1){
                        s = "Laine";
                    }else if(r == 2){
                        s = "Ble";
                    }else if(r == 3){
                        s = "Argile";
                    }else s = "Bois";
                    for (String sel : selectables) 
                        System.out.print(sel+"   ");
                    System.out.println();
                    String line = s;
                    if (!selectables.contains(line)) throw new WrongInputException();
                    this.exchange(4, line);
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        } else {
            Integer a = this.inventory.get(ressource);
            this.inventory.put(ressource, a-Integer.valueOf(n));
            do {
                try {
                    String s = "";
                    Random random = new Random();
                    int r = random.nextInt(5);
                    if(r == 0){
                        s = "Roche";
                    }else if(r == 1){
                        s = "Laine";
                    }else if(r == 2){
                        s = "Ble";
                    }else if(r == 3){
                        s = "Argile";
                    }else s = "Bois";
                    String line = s;
                    System.out.println();
                    if (!line.equals("Bois") && !line.equals("Argile") && !line.equals("Laine") && 
                        !line.equals("Ble") && !line.equals("Roche")) throw new WrongInputException();
                    Integer b = this.inventory.get(line);
                    this.inventory.put(line, b+Integer.valueOf(1));
                    System.out.println("Vous avez gagne 1 "+line);
                    break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true); 
        }   
    }

    // Fonction pour utiliser l'un des ports que possède le joueur :
    @Override
    protected void useHarbor() {
        Random r = new Random();
        int random = r.nextInt(2);
        if(random==0){
            int[] ids = new int[this.harbors.size()];
            int a = 0;
            for (Harbor h : this.harbors) {
                ids[a] = h.id;
                a++;
                System.out.print(h.toStringWithId()+"   ");
            }
            do {
                try {
                    Random r2 = new Random();
                    int random2 = r2.nextInt(ids.length);
                    int selectedId = ids[random2];
                    int b = 0;
                    for (int i=0; i<ids.length; i++) {
                        if (ids[i]!=selectedId) b++;
                    }
                    if (b==ids.length) throw new WrongInputException();
                    Harbor selectedHarbor = CatanTerminal.PLAYBOARD.getHarbor(selectedId);
                    if (selectedHarbor.type.equals("Simple")) 
                        if (!this.canExchange(3, null)) throw new NotEnoughRessourcesException();
                    else 
                        if (!this.canExchange(2, selectedHarbor.ressource)) throw new NotEnoughRessourcesException();
                    this.exchange(selectedHarbor.price, selectedHarbor.ressource);
                    break;
                } catch (Exception e) {}
            } while (true);
        }else{
            return;
        }
    }


    ////////// Fonctions des cartes developpement //////////

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
        this.buildRoad(true, false);
        this.buildRoad(true, false);
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
            for(Player PLAYBOARD : CatanTerminal.PLAYERS){
                if(PLAYBOARD!=this){
                    acc += PLAYBOARD.inventory.get(s);
                    PLAYBOARD.inventory.replace(s, 0);
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


    ////////// Route la plus longue du joueur courant //////////
    
    @Override
    protected int longestRoad() {
        return super.longestRoad();
    }

}