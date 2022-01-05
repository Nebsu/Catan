package Catan.CatanUI;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.awt.Color;
import Catan.CatanTerminal.Card;
import Catan.Exceptions.*;

final class IAIG extends PlayerIG {

    ////////// Constructeur et fonctions associées à ce dernier //////////
    
    public IAIG(String name, int s, Color c) {super(name, s, c);}

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
        //System.out.println(this.color+"Au tour de "+this.name+" :"+Game.RESET);
        // Proposition d'utilisation d'une carte développement (si le joueur en a) :
        this.proposeToUseSpecialCard();
        int dice = throwDices();
        //System.out.println(this.color+"Resultat du lancer : " + dice+Game.RESET+"\n");
        if (dice!=7) PlayerIG.earnResources(dice);
        else {
            //System.out.println(this.color+"Voleur active"+Game.RESET);
            this.thief();
        }
        this.specialCards.addAll(this.notUsableCards);
        this.notUsableCards.clear();
        if (!this.specialCards.isEmpty())
            //System.out.println("Vos cartes developpement :\n"+this.specialCards+Game.RESET);
        if (this.hasAVictoryPointCard() && this.victoryPoints==9) this.victoryPoints = 10;
    }

    // Lancé des dés :
    public int throwDices() { 
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
    protected BoxIG moveThief() {
        BoxIG res = null;
        do {
            try {
                Random random1 = new Random();
                Random random2 = new Random();
                int k = random1.nextInt(4);
                int l = random2.nextInt(4);
                if (Game.PLAYBOARD.boxes[k][l]==Game.PLAYBOARD.thief) throw new IllegalStateException();
                Game.PLAYBOARD.boxes[k][l].hasThief = true;
                Game.PLAYBOARD.thief.hasThief = false;
                Game.PLAYBOARD.thief = Game.PLAYBOARD.boxes[k][l];
                res = Game.PLAYBOARD.boxes[k][l];
                break;
            } catch (Exception e) {}
        } while (true);
        Game.PLAYBOARD.updatePaths();
        return res;
    }

    // Choix de la cible du joueur pour voler des ressources :
    @Override
    protected PlayerIG selectPlayerToStealFrom(BoxIG b) {
        // On recupère les emplacement adjacents de la case :
        ArrayList<LocationIG> locations = b.getLocations();
        // On va verifier si le joueur possède une colonie sur l'un des ces emplacements :
        ArrayList<ColonyIG> colonies = new ArrayList<ColonyIG>();
        for (LocationIG l : locations) {
            if (l instanceof ColonyIG && ((ColonyIG) l).player!=this) 
                colonies.add((ColonyIG) l);
        }
        if (colonies.isEmpty()) 
            return null;
        ArrayList<PlayerIG> nearPlayers = new ArrayList<PlayerIG>();
        for (ColonyIG c : colonies) {
            if (!nearPlayers.contains(c.player)) nearPlayers.add(c.player);
        }
        ArrayList<String> playersNames = new ArrayList<String>();
        for (PlayerIG p : nearPlayers) playersNames.add(p.name);
        PlayerIG selectedPlayer = null;
        do {
            try {
                Random random = new Random();
                int r = random.nextInt(nearPlayers.size());
                String name = nearPlayers.get(r).name;
                if (!playersNames.contains(name)) throw new WrongInputException();
                for (PlayerIG p : nearPlayers) {
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
    protected void steal(PlayerIG victim) {super.steal(victim);}


    ////////// Fonctions de proposition //////////

    // Menu principal du joueur :

    protected void playerMenu() {
        ArrayList<String> actions = new ArrayList<String>();
        if (this.canExchange(4, null)) actions.add("ECHANGE BANQUE");
        if (this.canUseHarbor()) actions.add("ECHANGE PORT");
        if (this.canConstructColony()) actions.add("CONSTRUIRE COLONIE");
        if (this.canConstructCity()) actions.add("CONSTRUIRE VILLE");
        if (this.canConstructRoad()) actions.add("CONSTRUIRE ROUTE");
        if (!this.specialCards.isEmpty()) actions.add("UTILISER CARTE");
        if (this.canBuyACard()) actions.add("ACHETER CARTE");
        if (actions.isEmpty()) {
            return;
        }
        Random rd1 = new Random();
        int sel = rd1.nextInt(actions.size());
        Random rd2 = new Random();
        boolean quit = rd2.nextBoolean();
        if (quit) {
            return;
        }
        String line = actions.get(sel);
        switch (line) {
            //case "ECHANGE BANQUE": System.out.println("Banque"); this.exchange(4, null); break;
            //case "ECHANGE PORT": System.out.println("Port");this.useHarbor(); break;
            //case "CONSTRUIRE COLONIE":System.out.println("Colonie"); this.buildColony(0,0,false); break;
            case "CONSTRUIRE VILLE":System.out.println("Ville"); this.buildCity(0,0); break;
            case "CONSTRUIRE ROUTE":System.out.println("Route"); this.buildRoad(' ',0,0,false, false); break;
            case "UTILISER CARTE":System.out.println("Carte"); this.useSpecialCard(); break;
            case "ACHETER CARTE":System.out.println("CarteAchat"); this.buySpecialCard(); break;
        }
        return;
    }


    // Proposition d'utilisation d'une carte developpement :

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
    protected boolean buildColony(int a, int b, boolean beginning) {
        do {
            try {
                Random rd1 = new Random();
                Random rd2 = new Random();
                int k = rd1.nextInt(5);
                int l = rd2.nextInt(5);
                if (Game.PLAYBOARD.locations[k][l] instanceof ColonyIG) throw new WrongInputException();
                if (beginning) {
                    if (Game.PLAYBOARD.locations[k][l].hasAnHarbor())
                        this.harbors.add(Game.PLAYBOARD.locations[k][l].getHarbor());
                    Game.PLAYBOARD.remove(Game.PLAYBOARD.locations[k][l]);
                    Game.PLAYBOARD.locations[k][l] = new ColonyIG(Game.PLAYBOARD.locations[k][l].boxes, k, l, this, Game.PLAYBOARD.locations[k][l].x, Game.PLAYBOARD.locations[k][l].y, Game.PLAYBOARD);
                    this.coloniesOnPlayBoard.add((ColonyIG) Game.PLAYBOARD.locations[k][l]);
                    ((LocationIG)Game.PLAYBOARD.locations[k][l]).setBackground(this.color);
                } else {
                    ArrayList<LocationIG> endPoints = this.getEndPoints();
                    if (!endPoints.contains(Game.PLAYBOARD.locations[k][l])) throw new InexistantRoadException();
                    if (Game.PLAYBOARD.locations[k][l].hasAnHarbor())
                        this.harbors.add(Game.PLAYBOARD.locations[k][l].getHarbor());
                    Game.PLAYBOARD.remove(Game.PLAYBOARD.locations[k][l]);
                    Game.PLAYBOARD.locations[k][l] = new ColonyIG(Game.PLAYBOARD.locations[k][l].boxes, k, l, this, Game.PLAYBOARD.locations[k][l].x, Game.PLAYBOARD.locations[k][l].y, Game.PLAYBOARD);
                    this.coloniesOnPlayBoard.add((ColonyIG) Game.PLAYBOARD.locations[k][l]);
                    ((LocationIG)Game.PLAYBOARD.locations[k][l]).setBackground(this.color);
                    this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                    this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
                    this.inventory.replace("Ble", this.inventory.get("Ble")-1); 
                    this.inventory.replace("Argile", this.inventory.get("Argile")-1);
                }
                this.victoryPoints++;
                break;
            } catch (Exception e) {}
        } while (true);
        Game.PLAYBOARD.updatePaths();
        return true;
    }

    // Construction d'une ville :
    @Override
    protected boolean buildCity(int a, int b) {
        do {   
            try {   
                Random random1 = new Random();
                Random random2 = new Random();
                int k = random1.nextInt(5);
                int l = random2.nextInt(5);
                if (!(Game.PLAYBOARD.locations[k][l] instanceof ColonyIG)) throw new InexistantColonyException();
                if (((ColonyIG) Game.PLAYBOARD.locations[k][l]).player!=this) throw new IllegalStateException();
                if (((ColonyIG) Game.PLAYBOARD.locations[k][l]).isCity) throw new WrongInputException();
                ((ColonyIG) Game.PLAYBOARD.locations[k][l]).isCity = true;
                ((LocationIG)Game.PLAYBOARD.locations[k][l]).setBackground(Color.BLACK);
                this.inventory.replace("Roche", this.inventory.get("Roche")-3); 
                this.inventory.replace("Ble", this.inventory.get("Ble")-2); 
                this.victoryPoints++;
                break;
            } catch (Exception e) {}
        } while (true);
        Game.PLAYBOARD.updatePaths();
        return true;
    }

    // Construction d'une route :
    @Override
    protected boolean buildRoad(char x, int y, int z, boolean isFree, boolean beginning) {
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
                if ((c=='H' && Game.PLAYBOARD.horizontalPaths[k][l] instanceof RoadIG) || 
                    (c=='V' && Game.PLAYBOARD.verticalPaths[k][l] instanceof RoadIG)) 
                    throw new IllegalStateException();
                try {
                    RoadIG r = this.buildRoadNextToColony(c, (c=='H')? Game.PLAYBOARD.horizontalPaths[k][l] : 
                    Game.PLAYBOARD.verticalPaths[k][l], beginning);
                    if (c=='H') {
                        Game.PLAYBOARD.remove(Game.PLAYBOARD.horizontalPaths[k][l]);
                        Game.PLAYBOARD.horizontalPaths[k][l] = r;
                        ((PathIG)Game.PLAYBOARD.horizontalPaths[k][l]).setBackground(this.color);
                    }
                    else {
                        Game.PLAYBOARD.remove(Game.PLAYBOARD.verticalPaths[k][l]);
                        Game.PLAYBOARD.verticalPaths[k][l] = r;
                        ((PathIG)Game.PLAYBOARD.verticalPaths[k][l]).setBackground(this.color);
                    }
                    this.roads.add(r);
                    if (!isFree) {
                        this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                        this.inventory.replace("Argile", this.inventory.get("Argile")-1); 
                    }
                    break;
                } catch (InexistantColonyException ice) {
                    try {
                        if (beginning) throw new IllegalStateException();
                        RoadIG r = this.buildRoadNextToRoad(c, (c=='H')? Game.PLAYBOARD.horizontalPaths[k][l] : 
                        Game.PLAYBOARD.verticalPaths[k][l]);
                        if (c=='H') {
                            Game.PLAYBOARD.remove(Game.PLAYBOARD.horizontalPaths[k][l]);
                            Game.PLAYBOARD.horizontalPaths[k][l] = r;
                            ((PathIG)Game.PLAYBOARD.horizontalPaths[k][l]).setBackground(this.color);
                        } 
                        else {
                            Game.PLAYBOARD.remove(Game.PLAYBOARD.verticalPaths[k][l]);
                            Game.PLAYBOARD.verticalPaths[k][l] = r;
                            ((PathIG)Game.PLAYBOARD.verticalPaths[k][l]).setBackground(this.color);
                        } 
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
        return true;
    }

    // Construction d'une route à côté d'une colonie :
    @Override
    protected RoadIG buildRoadNextToColony(char c, PathIG selectedPath, boolean beginning) throws InexistantColonyException {
        return super.buildRoadNextToColony(c, selectedPath, beginning);
    }

    // Construction d'une route à côté d'une autre route :
    @Override
    protected RoadIG buildRoadNextToRoad(char c, PathIG selectedPath) throws InexistantRoadException {
        return super.buildRoadNextToRoad(c, selectedPath);
    }

    // Fonction pour récupérer les emplacements d'arrivée de toutes les routes
    // construites par le joueur courant :
    @Override
    protected ArrayList<LocationIG> getEndPoints() {
        return super.getEndPoints();
    }


    ////////// Fonctions des ports et du commerce maritime //////////

    // Fonction qui procède à un échange de ressources via le commerce maritime :
    @Override
    protected boolean exchange(int n, String ressource) {
        if (ressource==null) {
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
                    String line = s;
                    if (!selectables.contains(line)) throw new WrongInputException();
                    this.exchange(n, line);
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
                    return true;
                } catch (Exception e) {}
            } while (true); 
        }   
    }

    // Fonction pour utiliser l'un des ports que possède le joueur :

    protected void useHarbor() {
        int[] ids = new int[this.harbors.size()];
        int a = 0;
        for (HarborIG h : this.harbors) {
            ids[a] = h.id;
            a++;
        }
        do {
            try {
                Random r2 = new Random();
                int random2 = r2.nextInt(ids.length);
                int selectedId = ids[random2];
                HarborIG selectedHarbor = Game.PLAYBOARD.getHarbor(selectedId);
                if (selectedHarbor.type.equals("Simple")) 
                    if (!this.canExchange(3, null)) throw new NotEnoughRessourcesException();
                else 
                    if (!this.canExchange(2, selectedHarbor.ressource)) throw new NotEnoughRessourcesException();
                this.exchange(selectedHarbor.price, selectedHarbor.ressource);
                break;
            } catch (Exception e) {}
        } while (true);
    }


    ////////// Fonctions des cartes developpement //////////

    // Utilisation d'une carte developpement :
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
    protected void carteRoute() {
        this.buildRoad(' ', 0, 0, true, false);
        this.buildRoad(' ', 0, 0, true, false);
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
            for(PlayerIG PLAYBOARD : Game.PLAYERS){
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