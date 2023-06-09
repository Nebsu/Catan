package Catan.CatanTerminal;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import Catan.Exceptions.*;

final class IA extends Player {

    ////////// Constructeur et fonctions associées à ce dernier //////////
    
    public IA(String name, int s) {super(name, s);}

    ////////// Fonctions auxiliaires //////////

    // Print :
    @Override
    public String toString() {return super.toString();}

    // Renvoie le nombre de ressources total du joueur :
    @Override
    protected int nbRessources(String ressource) {return super.nbRessources(ressource);}

    // Gain de ressources pendant la phase initiale :
    // Comme dans notre version du Catan, une colonie peut être collée jusqu'à 4 terrains différents (tuiles carrées), 
    // on décide de donner 3 ressources au lieu de 4 pour les colonies collées à 4 tuiles de terrains, dans le but 
    // d'améliorer la jouabilité (4 ressources dès le début c'est un peu de la triche) :
    protected void gainInitialResources() {super.gainInitialResources();}


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
    protected boolean canExchange(int price, String ressource) {
        return super.canExchange(price, ressource);
    }

    @Override
    protected boolean canUseHarbor() {return super.canUseHarbor();}

    @Override
    protected boolean hasAVictoryPointCard() {return super.hasAVictoryPointCard();}


    ////////// FONCTIONS DU JEU //////////

    // Fonction principale (tour de l'IA) :
    @Override
    public void play() {
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println(this.color+"Au tour de "+this.name+" :"+CatanTerminal.RESET);
        // Proposition d'utilisation d'une carte développement (si le joueur en a) :
        this.proposeToUseSpecialCard();
        int dice = throwDices();
        System.out.println(this.color+"Resultat du lancer : " + dice+CatanTerminal.RESET+"\n");
        if (dice!=7) Player.earnResources(dice);
        else {
            System.out.println(this.color+"Voleur active"+CatanTerminal.RESET);
            this.thief();
        }
        CatanTerminal.PLAYBOARD.display();
        this.playerMenu();
        this.specialCards.addAll(this.notUsableCards);
        this.notUsableCards.clear();
        if (!this.specialCards.isEmpty())
            System.out.println("Vos cartes developpement :\n"+this.specialCards+CatanTerminal.RESET);
        if (this.hasAVictoryPointCard() && this.victoryPoints==9) this.victoryPoints = 10;
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
        n /= 2;
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
            if (this.inventory.get(line)>0) {
                Integer a = this.inventory.get(line);
                this.inventory.put(line, a-Integer.valueOf(1));
            } else n++;
        }
    }

    // Déplacer le voleur :
    @Override
    protected Box moveThief() {
        Box res = null;
        do {
            try {
                Random random1 = new Random();
                Random random2 = new Random();
                int k = random1.nextInt(4);
                int l = random2.nextInt(4);
                if (CatanTerminal.PLAYBOARD.boxes[k][l]==CatanTerminal.PLAYBOARD.thief) throw new IllegalStateException();
                CatanTerminal.PLAYBOARD.boxes[k][l].hasThief = true;
                CatanTerminal.PLAYBOARD.thief.hasThief = false;
                CatanTerminal.PLAYBOARD.thief = CatanTerminal.PLAYBOARD.boxes[k][l];
                res = CatanTerminal.PLAYBOARD.boxes[k][l];
                break;
            } catch (Exception e) {}
        } while (true);
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
            if (l instanceof Colony && ((Colony) l).player!=this) 
                colonies.add((Colony) l);
        }
        if (colonies.isEmpty()) 
            return null;
        ArrayList<Player> nearPlayers = new ArrayList<Player>();
        for (Colony c : colonies) {
            if (!nearPlayers.contains(c.player)) nearPlayers.add(c.player);
        }
        ArrayList<String> playersNames = new ArrayList<String>();
        for (Player p : nearPlayers) playersNames.add(p.name);
        Player selectedPlayer = null;
        do {
            try {
                Random random = new Random();
                int r = random.nextInt(nearPlayers.size());
                String name = nearPlayers.get(r).name;
                if (!playersNames.contains(name)) throw new WrongInputException();
                System.out.println(nearPlayers.get(r).color+name + " a ete vole"+CatanTerminal.RESET);
                for (Player p : nearPlayers) {
                    if (p.name.equals(name)) {
                        selectedPlayer = p;
                        break;
                    }
                }
                break;
            } catch (Exception e) {}
        } while (true);
        return selectedPlayer;
    }

    // Voler une ressource au hasard au joueur choisi :
    @Override
    protected void steal(Player victim) {super.steal(victim);}


    ////////// Fonctions de proposition //////////

    // Menu principal du joueur :
    @Override
    protected void playerMenu() {
        System.out.println(this.color+"Votre inventaire : "+this.inventory+CatanTerminal.RESET+"\n");
        ArrayList<String> actions = new ArrayList<String>();
        if (this.canExchange(4, null)) actions.add("ECHANGE BANQUE");
        if (this.canUseHarbor()) actions.add("ECHANGE PORT");
        if (this.canConstructColony()) actions.add("CONSTRUIRE COLONIE");
        if (this.canConstructCity()) actions.add("CONSTRUIRE VILLE");
        if (this.canConstructRoad()) actions.add("CONSTRUIRE ROUTE");
        if (!this.specialCards.isEmpty()) actions.add("UTILISER CARTE");
        if (this.canBuyACard()) actions.add("ACHETER CARTE");
        if (actions.isEmpty()) {
            System.out.println(this.color+"Vous ne pouvez rien faire pour le moment"+CatanTerminal.RESET);
            return;
        }
        Random rd1 = new Random();
        int sel = rd1.nextInt(actions.size());
        Random rd2 = new Random();
        int quit = rd2.nextInt(3);
        if (quit == 2) return;
        String line = actions.get(sel);
        System.out.println(this.color+line+CatanTerminal.RESET);
        switch (line) {
            case "ECHANGE BANQUE": this.exchange(4, null); break;
            case "ECHANGE PORT": this.useHarbor(); break;
            case "CONSTRUIRE COLONIE": this.buildColony(false); break;
            case "CONSTRUIRE VILLE": this.buildCity(); break;
            case "CONSTRUIRE ROUTE": this.buildRoad(false, false); break;
            case "UTILISER CARTE": this.useSpecialCard(); break;
            case "ACHETER CARTE": this.buySpecialCard(); break;
        }
        this.playerMenu();
        return;
    }

    // Proposition d'utilisation d'une carte developpement :
    @Override
    protected void proposeToUseSpecialCard() {
        if (!this.specialCards.isEmpty()) {
            Random rd = new Random();
            int n = rd.nextInt(2);
            String line = (n==0)? "OUI" : "NON";
            if (line.equals("OUI"))
                this.useSpecialCard();
        }
    }


    ////////// Fonctions de construction //////////

    // Construction d'une colonie :
    // Remarque : on a décidé de ne pas implémenter le fait que toute colonie doit être distante d’au moins 2 intersections
    // En effet, le plateau est trop petit pour pouvoir appliquer cette règle de distance
    @Override
    protected void buildColony(boolean beginning) {
        do {
            try {
                Random rd1 = new Random();
                Random rd2 = new Random();
                int k = rd1.nextInt(5);
                int l = rd2.nextInt(5);
                if (CatanTerminal.PLAYBOARD.locations[k][l] instanceof Colony) throw new WrongInputException();
                if (beginning) {
                    if (CatanTerminal.PLAYBOARD.locations[k][l].hasAnHarbor())
                        this.harbors.add(CatanTerminal.PLAYBOARD.locations[k][l].getHarbor());
                    CatanTerminal.PLAYBOARD.locations[k][l] = new Colony(CatanTerminal.PLAYBOARD.locations[k][l].boxes, k, l, this);
                    this.coloniesOnPlayBoard.add((Colony) CatanTerminal.PLAYBOARD.locations[k][l]);
                } else {
                    ArrayList<Location> endPoints = this.getEndPoints();
                    if (!endPoints.contains(CatanTerminal.PLAYBOARD.locations[k][l])) throw new InexistantRoadException();
                    if (CatanTerminal.PLAYBOARD.locations[k][l].hasAnHarbor())
                        this.harbors.add(CatanTerminal.PLAYBOARD.locations[k][l].getHarbor());
                    CatanTerminal.PLAYBOARD.locations[k][l] = new Colony(CatanTerminal.PLAYBOARD.locations[k][l].boxes, k, l, this);
                    this.coloniesOnPlayBoard.add((Colony) CatanTerminal.PLAYBOARD.locations[k][l]);
                    this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                    this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
                    this.inventory.replace("Ble", this.inventory.get("Ble")-1); 
                    this.inventory.replace("Argile", this.inventory.get("Argile")-1);
                }
                this.victoryPoints++;
                break;
            } catch (Exception e) {}
        } while (true);
        CatanTerminal.PLAYBOARD.updatePaths();
        CatanTerminal.PLAYBOARD.display();
    }

    // Construction d'une ville :
    @Override
    protected void buildCity() {
        do {   
            try {   
                Random random1 = new Random();
                Random random2 = new Random();
                int k = random1.nextInt(5);
                int l = random2.nextInt(5);
                if (!(CatanTerminal.PLAYBOARD.locations[k][l] instanceof Colony)) throw new InexistantColonyException();
                if (((Colony) CatanTerminal.PLAYBOARD.locations[k][l]).player!=this) throw new IllegalStateException();
                if (((Colony) CatanTerminal.PLAYBOARD.locations[k][l]).isCity) throw new WrongInputException();
                ((Colony) CatanTerminal.PLAYBOARD.locations[k][l]).isCity = true;
                this.inventory.replace("Roche", this.inventory.get("Roche")-3); 
                this.inventory.replace("Ble", this.inventory.get("Ble")-2); 
                this.victoryPoints++;
                break;
            } catch (Exception e) {}
        } while (true);
        CatanTerminal.PLAYBOARD.updatePaths();
        CatanTerminal.PLAYBOARD.display();
    }

    // Construction d'une route :
    @Override
    protected void buildRoad(boolean isFree, boolean beginning) {
        char c;
        do {
            try {
                Random random1 = new Random();
                Random random2 = new Random();
                Random random3 = new Random();
                int k = random1.nextInt(5);
                int l = random2.nextInt(5);
                int n3 = random3.nextInt(2);
                if(n3 == 0){
                    c = 'H';
                    if (k<0 || k>4 || l<0 || l>3) throw new IndexOutOfBoundsException();
                }else{
                    c = 'V';
                    if (k<0 || k>3 || l<0 || l>4) throw new IndexOutOfBoundsException();
                }
                if ((c=='H' && CatanTerminal.PLAYBOARD.horizontalPaths[k][l] instanceof Road) || 
                    (c=='V' && CatanTerminal.PLAYBOARD.verticalPaths[k][l] instanceof Road)) 
                    throw new IllegalStateException();
                try {
                    Road r = this.buildRoadNextToColony(c, (c=='H')? CatanTerminal.PLAYBOARD.horizontalPaths[k][l] : 
                    CatanTerminal.PLAYBOARD.verticalPaths[k][l], beginning);
                    if (c=='H') CatanTerminal.PLAYBOARD.horizontalPaths[k][l] = r;
                    else CatanTerminal.PLAYBOARD.verticalPaths[k][l] = r;
                    this.roads.add(r);
                    if (!isFree) {
                        this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                        this.inventory.replace("Argile", this.inventory.get("Argile")-1); 
                    }
                    break;
                } catch (InexistantColonyException ice) {
                    try {
                        if (beginning) throw new IllegalStateException();
                        Road r = this.buildRoadNextToRoad(c, (c=='H')? CatanTerminal.PLAYBOARD.horizontalPaths[k][l] : 
                        CatanTerminal.PLAYBOARD.verticalPaths[k][l]);
                        if (c=='H') CatanTerminal.PLAYBOARD.horizontalPaths[k][l] = r;
                        else CatanTerminal.PLAYBOARD.verticalPaths[k][l] = r;
                        this.roads.add(r);
                        if (!isFree) {
                            this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                            this.inventory.replace("Argile", this.inventory.get("Argile")-1); 
                        }
                        break;
                    } catch (Exception e) {}
                }
            } catch (Exception e) {}
        } while (true);
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
            Set<String> keys = this.inventory.keySet();
            ArrayList<String> selectables = new ArrayList<String>();
            for (String key : keys) {
                if (this.inventory.get(key)>=n) selectables.add(key);
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
                    String line = s;
                    if (!selectables.contains(line)) throw new WrongInputException();
                    this.exchange(n, line);
                    return;
                } catch (Exception e) {}
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
                    if (!line.equals("Bois") && !line.equals("Argile") && !line.equals("Laine") && 
                        !line.equals("Ble") && !line.equals("Roche")) throw new WrongInputException();
                    if (line.equals(ressource)) throw new WrongInputException();
                    Integer b = this.inventory.get(line);
                    this.inventory.put(line, b+Integer.valueOf(1));
                    return;
                } catch (Exception e) {}
            } while (true); 
        }   
    }

    // Fonction pour utiliser l'un des ports que possède le joueur :
    @Override
    protected void useHarbor() {
        int[] ids = new int[this.harbors.size()];
        int a = 0;
        for (Harbor h : this.harbors) {
            ids[a] = h.id;
            a++;
        }
        do {
            try {
                Random r2 = new Random();
                int random2 = r2.nextInt(ids.length);
                int selectedId = ids[random2];
                Harbor selectedHarbor = CatanTerminal.PLAYBOARD.getHarbor(selectedId);
                if (selectedHarbor.type.equals("Simple")) {
                    if (!this.canExchange(3, null)) throw new NotEnoughRessourcesException();
                    this.exchange(3, null);
                } else {
                    if (!this.canExchange(2, selectedHarbor.ressource)) throw new NotEnoughRessourcesException();
                    this.exchange(2, selectedHarbor.ressource);
                }
                return;
            } catch (Exception e) {}
        } while (true);
    }


    ////////// Fonctions des cartes developpement //////////

    // Utilisation d'une carte developpement :
    @Override
    protected void useSpecialCard() {
        do {
            try {
                Random rd = new Random();
                int n = rd.nextInt(5);
                for(Card c : this.specialCards){
                    // Carte point de victoire :
                    if (c.id==0 && n==0) {
                        this.specialCards.remove(c);
                        this.victoryPoints++; 
                        System.out.println(this.color+"Vous avez gagne un point de victoire"+CatanTerminal.RESET);
                        break;
                    }
                    // Carte chevalier :
                    if(c.id==1 && n==1){
                        this.specialCards.remove(c);
                        this.knight();
                        break;
                    }
                    // Carte progrès invention :
                    if(c.id==3 && n==3){
                        this.specialCards.remove(c);
                        this.invention();
                        break;
                    }
                    // Carte progrès route :
                    if(c.id==2 && n==2){
                        this.specialCards.remove(c);
                        this.carteRoute();
                        break;
                    }
                    // Carte progrès monopole :
                    if(c.id==4 && n==4){
                        this.specialCards.remove(c);
                        this.monopole();
                        break;
                    }
                    throw new WrongInputException();
                }
                break;
            } catch (Exception e) {}
        } while (true);
    }

    // Carte chevalier :
    @Override
    protected void knight() {super.knight();}

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
    
    // Carte progrès monopole :
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
    protected void buySpecialCard() {super.buySpecialCard();}


    ////////// Route la plus longue du joueur courant //////////
    
    @Override
    protected int longestRoad() {
        return super.longestRoad();
    }

}