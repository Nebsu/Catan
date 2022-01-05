package Catan.CatanUI;

import java.util.*;
import Catan.Exceptions.*;
import Catan.CatanTerminal.Card;
import java.awt.Color;
import java.awt.event.*;

class PlayerIG {
    
    ////////// Attributs //////////

    protected final Color color;
    protected final String name; // nom du joueur
    protected final char symbol; // symbole du pion du joueur (son numéro)
    protected int victoryPoints; // points de victoire du joueur
    protected final Map<String, Integer> inventory; // ressources du joueur
    protected final ArrayList<RoadIG> roads; // routes construites par le joueur sur le plateau
    protected final ArrayList<Card> specialCards; // cartes développement que le joueur peut utiliser
    protected final ArrayList<Card> notUsableCards; // cartes développement piochées par le joueur, qui ne peut pas encore utiliser
    protected final ArrayList<ColonyIG> coloniesOnPlayBoard; // colonies construites par le joueur sur le plateau
    protected final ArrayList<HarborIG> harbors; // ports possédés par le joueur
    protected int knights; // nombre de cartes chevalier jouées par le joueur

    boolean monopoleCard = false;
    boolean inventionCard = false;
    int inventionacc = 0;
    boolean harborExchange = false;
    String monopoleResource = "";

    ////////// Constructeur et fonctions associées à ce dernier ////////// 

    PlayerIG(String name, int s, Color color) {
        this.color = color;
        this.name = name;
        this.symbol = String.valueOf(s).charAt(0);
        this.victoryPoints = 0;
        this.inventory = new HashMap<String, Integer>();
        this.inventorySetup();
        this.roads = new ArrayList<RoadIG>();
        this.specialCards = new ArrayList<Card>();
        this.notUsableCards = new ArrayList<Card>();
        this.coloniesOnPlayBoard = new ArrayList<ColonyIG>();
        this.harbors = new ArrayList<HarborIG>();
        this.knights = 0;
    }

    // Initialisation des ressources du joueur :
    private final void inventorySetup() { 
        this.inventory.put("Bois", 0); // bois
        this.inventory.put("Argile", 0); // argile
        this.inventory.put("Laine", 0); // laine
        this.inventory.put("Ble", 0); // blé
        this.inventory.put("Roche", 0); // minerai
    }

    ////////// Fonctions auxiliaires //////////
    
    // Print :
    @Override
    public String toString() {
        return (this.name+" possede "+this.victoryPoints+" points de victoire.");
    }

    // Renvoie le nombre de ressources total du joueur :
    protected int nbRessources(String ressource) {
        int nbRessources = 0;
        if (ressource==null) {
            nbRessources += this.inventory.get("Bois");
            nbRessources += this.inventory.get("Argile");
            nbRessources += this.inventory.get("Laine");
            nbRessources += this.inventory.get("Ble");
            nbRessources += this.inventory.get("Roche");
        } else nbRessources += this.inventory.get(ressource);
        return nbRessources;
    }

    // Gain de ressources pendant la phase initiale :
    // Comme dans notre version du Catan, une colonie peut être collée jusqu'à 4 terrains différents (tuiles carrées), 
    // on décide de donner 3 ressources au lieu de 4 pour les colonies collées à 4 tuiles de terrains, dans le but 
    // d'améliorer la jouabilité (4 ressources dès le début c'est un peu de la triche) :
    protected void gainInitialResources() {
        ColonyIG lastConstructedColony = this.coloniesOnPlayBoard.get(this.coloniesOnPlayBoard.size()-1);
        BoxIG[] boxes = lastConstructedColony.boxes;
        if (boxes.length==4) {
            Random rd = new Random();
            int exclude = rd.nextInt(4);
            for (int i=0; i<boxes.length; i++) {
                if (boxes[i].ressource!=null && i!=exclude)
                    this.inventory.replace(boxes[i].ressource, this.inventory.get(boxes[i].ressource)+1);
            }
        } else {
            for (int i=0; i<boxes.length; i++) {
                if (boxes[i].ressource!=null)
                    this.inventory.replace(boxes[i].ressource, this.inventory.get(boxes[i].ressource)+1);
            }
        }
    }


    ////////// Fonctions booléennes //////////

    protected boolean isWinner() {return (this.victoryPoints>=10);}
    
    protected boolean canConstructColony() {
        int numberOfColonies = 0;
        for (ColonyIG col : this.coloniesOnPlayBoard) {
            if (!col.isCity) numberOfColonies++;
        }
        return ((this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1 &&
                this.inventory.get("Laine")>=1 && this.inventory.get("Ble")>=1) &&
                (numberOfColonies<5) && (!Game.PLAYBOARD.isFilledLocations()));
    }

    protected boolean canConstructCity() {
        int numberOfCities = 0;
        for (ColonyIG col : this.coloniesOnPlayBoard) {
            if (col.isCity) numberOfCities++;
        }
        return ((numberOfCities<4) && 
                (this.inventory.get("Roche")>=3 && this.inventory.get("Ble")>=2));
    }

    protected boolean canConstructRoad() {
        return ((this.roads.size()<15) && (!Game.PLAYBOARD.isFilledPaths()) &&
                (this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1));
    }

    protected boolean canBuyACard() {
        return (!Game.DECK.getDeck().isEmpty() && this.inventory.get("Roche")>=1 
                && this.inventory.get("Laine")>=1 && this.inventory.get("Ble")>=1);
    }

    protected boolean canExchange(int price, String ressource) {
        int[] ressources = new int[5];
        ressources[0] = this.nbRessources("Bois");
        ressources[1] = this.nbRessources("Argile");
        ressources[2] = this.nbRessources("Laine");
        ressources[3] = this.nbRessources("Ble");
        ressources[4] = this.nbRessources("Roche");
        if (ressource==null) {
            for (int i=0; i<ressources.length; i++) {
                if (ressources[i]>=price) return true;
            }
            return false;
        } else return (this.inventory.get(ressource)>=price);
    }

    protected boolean canUseHarbor() {
        if (this.harbors.isEmpty()) return false;
        for (HarborIG h : this.harbors) {
            if (h.type.equals("Special")) {
                if (canExchange(2, h.ressource)) return true;
            }
            if (h.type.equals("Simple")) {
                if (canExchange(3, null)) return true;
            }
        }
        return false;
    }

    protected boolean hasAVictoryPointCard() {
        if (this.specialCards.isEmpty()) return false;
        for (Card c : this.specialCards) {
            if (c.id==0) return true;
        }
        return false;
    }
    

    ////////// FONCTIONS DU JEU //////////

    // Fonction principale (tour du joueur) :
    protected void play() {
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("Au tour de "+this.name+" :");
        // Proposition d'utilisation d'une carte développement (si le joueur en a) :
        System.out.println("Tapez sur entree pour lancer les des :");
        int dice = throwDices();
        System.out.println("Resultat du lancer : " + dice+"\n");
        if (dice!=7) earnResources(dice);
        else {
            System.out.println("Voleur active"+"\n");
            this.thief();
        }
        this.specialCards.addAll(this.notUsableCards);
        this.notUsableCards.clear();
        if (!this.specialCards.isEmpty())
            System.out.println("Vos cartes developpement :\n"+this.specialCards);
        if (this.hasAVictoryPointCard() && this.victoryPoints==9) this.victoryPoints = 10;
    }

    // Lancé des dés :
    public int throwDices() { 
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        return (dice1+dice2);
    }

    // Répartition des ressources selon le résultat du lancé des dés :
    protected final static void earnResources(int dice) {
        // On récupère la ou les cases designée(s) par les dés :
        ArrayList<BoxIG> boxes = Game.PLAYBOARD.getBoxes(dice);
        // On récupère les emplacement adjacents de ces cases :
        ArrayList<LocationIG> locations = new ArrayList<LocationIG>();
        for (BoxIG b : boxes) {
            ArrayList<LocationIG> loc = b.getLocations();
            for (int i=0; i<loc.size(); i++) locations.add(loc.get(i));
        }
        // On va verifier si les joueurs possèdent une colonie sur l'un des ces emplacements :
        ArrayList<ColonyIG> colonies = new ArrayList<ColonyIG>();
        for (LocationIG l : locations) {
            if (l instanceof ColonyIG) {
                colonies.add((ColonyIG) l);
            }
        }
        // Cas où aucune colonie est adjacentes aux cases designees par les des :
        if (colonies.isEmpty()) {
            System.out.println("Pas de chance, personne n'a rien gagne");
            return;
        }
        // Les joueurs concernes gagnent une ressource de chaque case designee par les des :
        for (ColonyIG c : colonies) {
            for (int i=0; i<c.boxes.length; i++) {
                try{
                    if (c.boxes[i].number==dice && !c.boxes[i].hasThief) {
                        Integer a = c.player.inventory.get(c.boxes[i].ressource);
                        if (c.isCity)
                            c.player.inventory.put(c.boxes[i].ressource, a+Integer.valueOf(2));
                        else 
                            c.player.inventory.put(c.boxes[i].ressource, a+Integer.valueOf(1));
                    }
                }catch(Exception e){
                    System.out.println(c.player);
                }
            }
        }
    }


    ////////// Fonctions du voleur //////////

    // Fonction principale du voleur :
    protected void thief() {
        // On compte les ressources de chaque joueur :
        int[] nbRessources = new int[Game.PLAYERS.length];
        for (int i=0; i<Game.PLAYERS.length; i++) {
            nbRessources[i] += Game.PLAYERS[i].nbRessources(null);
        }
        // Les joueurs qui possèdent plus de 8 ressources,
        //  doivent donner la moitie de leurs ressources au voleur :
        for (int i=0; i<nbRessources.length; i++) {
            if (nbRessources[i]>=8) Game.PLAYERS[i].giveRessources(nbRessources[i]);
        }
        // Ensuite, le joueur qui a lance les des deplace le voleur :
        BoxIG b = this.moveThief();
        // Ensuite, le joueur courant choisi le joueur a qui il veut voler une ressource :
        PlayerIG victim = this.selectPlayerToStealFrom(b);
        // Si le joueur courant a choisi une case avec des colonies adjacentes, 
        // alors le joueur designe par le joueur courant subit le vol : 
        if (victim!=null) this.steal(victim);
    }

    // Donner des ressources au voleur :
    protected void giveRessources(int n) {
        System.out.println(this.name+" possède "+n+" ressources");
        n /= 2;
        System.out.println(this.name+" : Veuillez donner "+n+" ressources au voleur");
        Scanner sc = new Scanner(System.in);
        for (int i=0; i<n; i++) {
            System.out.println("Votre inventaire : "+this.inventory);
            System.out.println("Choisissez une ressource à donner au voleur :");
            do {
                try {
                    System.out.println("Tapez Bois, Argile, Laine, Ble ou Roche :");
                    String line = sc.nextLine();
                    if (!line.equals("Bois") && !line.equals("Argile") && !line.equals("Laine") && 
                        !line.equals("Ble") && !line.equals("Roche")) throw new WrongInputException();
                    Integer a = this.inventory.get(line);
                    if (a==0) throw new IllegalStateException(line);
                    this.inventory.put(line, a-Integer.valueOf(1));
                    break;
                } catch (IllegalStateException ill) {
                    System.out.println("Erreur : Vous n'avez plus aucun "+ill.getMessage());
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

    // Déplacer le voleur :
    protected BoxIG moveThief() {
        BoxIG res = null;
        Scanner sc = new Scanner(System.in);
        do {
            try {
                System.out.println(this.name+", placez le voleur sur la case de votre choix :");
                int[] indexs = scanLocationOrPath(sc);
                int k = indexs[0]-1; int l = indexs[1]-1;
                if (k<0 || k>4 || l<0 || l>4) throw new IndexOutOfBoundsException();
                if (Game.PLAYBOARD.boxes[k][l]==Game.PLAYBOARD.thief) throw new IllegalStateException();
                Game.PLAYBOARD.boxes[k][l].hasThief = true;
                Game.PLAYBOARD.thief.hasThief = false;
                Game.PLAYBOARD.thief = Game.PLAYBOARD.boxes[k][l];
                res = Game.PLAYBOARD.boxes[k][l];
                break;
            } catch (IndexOutOfBoundsException ind) {
                System.out.println("Erreur : cette case n'existe pas");
            } catch (IllegalArgumentException ill) {
                System.out.println("Erreur : Vous etes oblige de deplacer le voleur sur une nouvelle case");
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
            }
        } while (true);
        Game.PLAYBOARD.updatePaths();
        return res;
    }

    // Choix de la cible du joueur pour voler des ressources : 
    protected PlayerIG selectPlayerToStealFrom(BoxIG b) {
        // On recupère les emplacement adjacents de la case :
        ArrayList<LocationIG> locations = b.getLocations();
        // On va verifier si le joueur possède une colonie sur l'un des ces emplacements :
        ArrayList<ColonyIG> colonies = new ArrayList<ColonyIG>();
        for (LocationIG l : locations) {
            if (l instanceof ColonyIG && ((ColonyIG) l).player!=this) 
                colonies.add((ColonyIG) l);
        }
        if (colonies.isEmpty()) {
            System.out.println("Aucune colonie à proximite de cette case");
            return null;
        }
        ArrayList<PlayerIG> nearPlayers = new ArrayList<PlayerIG>();
        for (ColonyIG c : colonies) {
            if (!nearPlayers.contains(c.player)) nearPlayers.add(c.player);
        }
        ArrayList<String> playersNames = new ArrayList<String>();
        for (PlayerIG p : nearPlayers) playersNames.add(p.name);
        System.out.println("Choisissez le joueur que vous voulez racketter :");
        PlayerIG selectedPlayer = null;
        Scanner sc = new Scanner(System.in);
        do {
            try {
                System.out.println("Tapez l'un des noms ci-dessous :");
                for (PlayerIG p : nearPlayers) 
                    System.out.print(p.name+"  ");
                System.out.println();
                String name = sc.nextLine();
                if (!playersNames.contains(name)) throw new WrongInputException();
                for (PlayerIG p : nearPlayers) {
                    if (p.name.equals(name)) {
                        selectedPlayer = p;
                        break;
                    }
                }
                break;
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
            }
        } while (true);
        return selectedPlayer;
    }

    // Voler une ressource au hasard au joueur choisi :
    protected void steal(PlayerIG victim) {
        ArrayList<Integer> codes = new ArrayList<Integer>();
        Random rd = new Random();
        if (victim.inventory.get("Bois")>0) codes.add(1);
        if (victim.inventory.get("Argile")>0) codes.add(2);
        if (victim.inventory.get("Laine")>0) codes.add(3);
        if (victim.inventory.get("Ble")>0) codes.add(4);
        if (victim.inventory.get("Roche")>0) codes.add(5);
        if (codes.isEmpty()) {
            System.out.println(victim.name+" n'a aucune ressource, aucun vol subi");
            return;
        }
        int code = codes.get(rd.nextInt(codes.size()));
        switch (code) {
            case 1: Integer a = victim.inventory.get("Bois");
                    victim.inventory.put("Bois", a-Integer.valueOf(1));
                    a = this.inventory.get("Bois");
                    this.inventory.put("Bois", a+Integer.valueOf(1)); 
                    System.out.println("Vous avez pris 1 bois a "+victim.name);
                    break;
            case 2: Integer b = victim.inventory.get("Argile");
                    victim.inventory.put("Argile", b-Integer.valueOf(1));
                    b = this.inventory.get("Argile");
                    this.inventory.put("Argile", b+Integer.valueOf(1));
                    System.out.println("Vous avez pris 1 argile a "+victim.name);
                    break;
            case 3: Integer c = victim.inventory.get("Laine");
                    victim.inventory.put("Laine", c-Integer.valueOf(1));
                    c = this.inventory.get("Laine");
                    this.inventory.put("Laine", c+Integer.valueOf(1));
                    System.out.println("Vous avez pris 1 laine a "+victim.name);
                    break;
            case 4: Integer d = victim.inventory.get("Ble");
                    victim.inventory.put("Ble", d-Integer.valueOf(1));
                    d = this.inventory.get("Ble");
                    this.inventory.put("Ble", d+Integer.valueOf(1));
                    System.out.println("Vous avez pris 1 blé a "+victim.name);
                    break;
            case 5: Integer e = victim.inventory.get("Roche");
                    victim.inventory.put("Roche", e-Integer.valueOf(1));
                    e = this.inventory.get("Roche");
                    this.inventory.put("Roche", e+Integer.valueOf(1));
                    System.out.println("Vous avez pris 1 roche a "+victim.name);
                    break;
        }
    }

    ////////// Fonctions de construction //////////

    // Construction d'une colonie :
    // Remarque : on a décidé de ne pas implémenter le fait que toute colonie doit être distante d’au moins 2 intersections
    // En effet, le plateau est trop petit pour pouvoir appliquer cette règle de distance
    protected boolean buildColony(int k, int l, boolean beginning) {
        if(this.canConstructColony() || beginning){
            try {
                if (Game.PLAYBOARD.locations[k][l] instanceof ColonyIG) throw new WrongInputException();
                if (beginning) {
                    if (Game.PLAYBOARD.locations[k][l].hasAnHarbor())
                        this.harbors.add(Game.PLAYBOARD.locations[k][l].getHarbor());
                    Game.PLAYBOARD.remove(Game.PLAYBOARD.locations[k][l]);
                    Game.PLAYBOARD.locations[k][l] = new ColonyIG(Game.PLAYBOARD.locations[k][l].boxes, k, l, this, Game.PLAYBOARD.locations[k][l].x, Game.PLAYBOARD.locations[k][l].y, Game.PLAYBOARD);
                    ((LocationIG)Game.PLAYBOARD.locations[k][l]).setBackground(this.color);
                    this.coloniesOnPlayBoard.add((ColonyIG) Game.PLAYBOARD.locations[k][l]);
                } else {
                    ArrayList<LocationIG> endPoints = this.getEndPoints();
                    if (!endPoints.contains(Game.PLAYBOARD.locations[k][l])) throw new InexistantRoadException();
                    if (Game.PLAYBOARD.locations[k][l].hasAnHarbor())
                        this.harbors.add(Game.PLAYBOARD.locations[k][l].getHarbor());
                    Game.PLAYBOARD.remove(Game.PLAYBOARD.locations[k][l]);
                    Game.PLAYBOARD.locations[k][l] = new ColonyIG(Game.PLAYBOARD.locations[k][l].boxes, k, l, this, Game.PLAYBOARD.locations[k][l].x, Game.PLAYBOARD.locations[k][l].y, Game.PLAYBOARD);
                    ((LocationIG)Game.PLAYBOARD.locations[k][l]).setBackground(this.color);
                    this.coloniesOnPlayBoard.add((ColonyIG) Game.PLAYBOARD.locations[k][l]);
                    this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                    this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
                    this.inventory.replace("Ble", this.inventory.get("Ble")-1); 
                    this.inventory.replace("Argile", this.inventory.get("Argile")-1);
                }
                this.victoryPoints++;
            } catch (WrongInputException w) {
                System.out.println("Erreur : cet emplacement est occupe");
                System.out.println(((ColonyIG)Game.PLAYBOARD.locations[k][l]).player);
                return false;
            } catch (InexistantRoadException ire) {
                System.out.println("Erreur : cet emplacement n'est pas en contact avec l'une de vos routes");
                return false;
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
                return false;
            }
        Game.PLAYBOARD.updatePaths();
        return true;
        }else{
            System.out.println("Vous n'avez pas assez de ressources");
            return false;
        }
    }

    // Construction d'une ville :
    protected boolean buildCity(int k, int l) {
        if(canConstructCity()){
            try {
                if (k<0 || k>4 || l<0 || l>4) throw new IndexOutOfBoundsException();
                if (!(Game.PLAYBOARD.locations[k][l] instanceof ColonyIG)) throw new InexistantColonyException();
                if (((ColonyIG) Game.PLAYBOARD.locations[k][l]).player!=this) throw new IllegalStateException();
                if (((ColonyIG) Game.PLAYBOARD.locations[k][l]).isCity) throw new WrongInputException();
                ((ColonyIG) Game.PLAYBOARD.locations[k][l]).isCity = true;
                this.inventory.replace("Roche", this.inventory.get("Roche")-3); 
                this.inventory.replace("Ble", this.inventory.get("Ble")-2); 
                ((LocationIG)Game.PLAYBOARD.locations[k][l]).setBackground(Color.BLACK);
                this.victoryPoints++;
            } catch (IllegalStateException ise) {
                System.out.println("Erreur : cette colonie ne vous appartient pas");
                return false;
            } catch (WrongInputException w) {
                System.out.println("Erreur : vous avez selectionne une ville");
                return false;
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
                return false;
            }
            Game.PLAYBOARD.updatePaths();
            return true;
        }else{
            System.out.println("Vous n'avez pas assez de ressources");
            return false;
        }
    }

    // Construction d'une route :
    protected boolean buildRoad(char c, int k, int l, boolean isFree, boolean beginning) {
        try {
                if ((c=='H' && Game.PLAYBOARD.horizontalPaths[k][l] instanceof RoadIG) || 
                    (c=='V' && Game.PLAYBOARD.verticalPaths[k][l] instanceof RoadIG)) 
                throw new IllegalStateException();
            try {
                if(this.canConstructRoad() || isFree){
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
                }else{
                    System.out.println("Vous n'avez pas assez de ressources");
                }
            } catch (InexistantColonyException ice) {
                try {
                    if(this.canConstructRoad() || isFree){
                        if (beginning) throw new IllegalStateException();
                        RoadIG r = this.buildRoadNextToRoad(c, (c=='H')? Game.PLAYBOARD.horizontalPaths[k][l] :
                        Game.PLAYBOARD.verticalPaths[k][l]);
                        if (c=='H'){
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
                    }else{
                        System.out.println("Vous n'avez pas assez de ressources");
                    }
                } catch (IllegalStateException e) {
                    return false;
                } catch (InexistantRoadException ire) {
                    return false;
                }
            }
        } catch (IllegalStateException ill) {
            System.out.println("Erreur : cet emplacement est occupe");
            return false;
        }
        return true;
    }

    // Construction d'une route à côté d'une colonie :
    protected RoadIG buildRoadNextToColony(char c, PathIG selectedPath, boolean beginning) throws InexistantColonyException {
        if (selectedPath.point1 instanceof ColonyIG) {
            ColonyIG col = (ColonyIG) selectedPath.point1;
            if (col.player==this && !beginning)
                return new RoadIG(this, c, selectedPath.point1, selectedPath.point2, 1, selectedPath.x, selectedPath.y, selectedPath.height, selectedPath.width, Game.PLAYBOARD);
            if (col==this.coloniesOnPlayBoard.get(this.coloniesOnPlayBoard.size()-1) && beginning)
                return new RoadIG(this, c, selectedPath.point1, selectedPath.point2, 1, selectedPath.x, selectedPath.y, selectedPath.height, selectedPath.width, Game.PLAYBOARD);
        }
        if (selectedPath.point2 instanceof ColonyIG) {
            ColonyIG col = (ColonyIG) selectedPath.point2;
            if (col.player==this && !beginning) 
                return new RoadIG(this, c, selectedPath.point1, selectedPath.point2, 2, selectedPath.x, selectedPath.y, selectedPath.height, selectedPath.width, Game.PLAYBOARD);
            if (col==this.coloniesOnPlayBoard.get(this.coloniesOnPlayBoard.size()-1) && beginning)
                return new RoadIG(this, c, selectedPath.point1, selectedPath.point2, 2, selectedPath.x, selectedPath.y, selectedPath.height, selectedPath.width, Game.PLAYBOARD);
        }
        throw new InexistantColonyException();
    }

    // Construction d'une route à côté d'une autre route :
    protected RoadIG buildRoadNextToRoad(char c, PathIG selectedPath) throws InexistantRoadException {
        ArrayList<LocationIG> endPoints = this.getEndPoints();
        for (LocationIG endPoint : endPoints) {
            if (selectedPath.point1==endPoint) {
                return new RoadIG(this, c, selectedPath.point1, selectedPath.point2, 1, selectedPath.x, selectedPath.y, selectedPath.height, selectedPath.width, Game.PLAYBOARD);
            }
            if (selectedPath.point2==endPoint) {
                return new RoadIG(this, c, selectedPath.point1, selectedPath.point2, 2, selectedPath.x, selectedPath.y, selectedPath.height, selectedPath.width, Game.PLAYBOARD);
            }
        }
        throw new InexistantRoadException();
    }

    // Fonction pour récupérer les emplacements d'arrivée de toutes les routes
    // construites par le joueur courant :
    protected ArrayList<LocationIG> getEndPoints() {
        ArrayList<LocationIG> endPoints = new ArrayList<LocationIG>();
        for (RoadIG PLAYBOARD : this.roads) {
            endPoints.add(PLAYBOARD.endPoint);
        }
        return endPoints;
    }

    // Fonction auxiliaire pour récuperer les coordonnées d'un(e) case/emplacement/chemin :
    private final static int[] scanLocationOrPath(Scanner sc) throws Exception {
        try {
            String s = sc.nextLine();
            if (s.length()>2 || s.length()<=0) throw new RuntimeException();
            String i = String.valueOf(s.charAt(0));
            String j = String.valueOf(s.charAt(1));
            int k = Integer.valueOf(i);
            int l = Integer.valueOf(j);
            int[] res = {k,l};
            return res;
        } catch (Exception e) {
            throw e;
        }
    }


    ////////// Fonctions des ports et du commerce maritime //////////

    // Fonction qui procède à un échange de ressources via le commerce maritime :
    protected boolean exchange(int n, String ressource) {
        harborExchange = true;
        if(harborExchange){
            if (ressource==null) {
                Set<String> keys = this.inventory.keySet();
                ArrayList<String> selectables = new ArrayList<String>();
                for (String key : keys) {
                    if (this.inventory.get(key)>=n) selectables.add(key);
                }
                for (String sel : selectables) {
                    Game.popupechange.giveChoice.add(sel);
                }
                Game.popupechange.quantity = n;
                Game.popupechange.setVisible(true);
                return true;
            } else {
                Game.popupechange.quantity = n;
                Game.popupechange.giveChoice.add(ressource);
                Game.popupechange.setVisible(true);
                return true;
            }
        }
        return false;       
    }

    // Fonction pour utiliser l'un des ports que possède le joueur :
    protected boolean useHarbor(int selectedId) {
        int[] ids = new int[this.harbors.size()];
        int a = 0;
        for (HarborIG h : this.harbors) {
            ids[a] = h.id;
            a++;
        }
        try {
            int b = 0;
            for (int i=0; i<ids.length; i++) {
                if (ids[i]!=selectedId) b++;
            }
            if (b==ids.length) throw new WrongInputException();
            HarborIG selectedHarbor = Game.PLAYBOARD.getHarbor(selectedId);
            if (selectedHarbor.type.equals("Simple")) 
                if (!this.canExchange(3, null)) throw new NotEnoughRessourcesException();
            else 
                if (!this.canExchange(2, selectedHarbor.ressource)) throw new NotEnoughRessourcesException();
            this.exchange(selectedHarbor.price, selectedHarbor.ressource);
        } catch (NotEnoughRessourcesException not) {
            System.out.println(not);
            return false;
        } catch (Exception e) {
            System.out.println(WrongInputException.MESSAGE);
            return false;
        }
        return true;
    }

    
    ////////// Fonctions des cartes developpement //////////

    // Utilisation d'une carte developpement :
    protected void useSpecialCard(int n) {      
        for(Card c : this.specialCards){
            // Carte point de victoire :
            if (c.id==0 && n==0) {
                this.specialCards.remove(c);
                this.victoryPoints++;
                Game.refreshVictoryPoints();
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
                Game.roadCard = true;
                break;
            }
            // Carte progrès monopole :
            if(c.id==4 && n==4){
                this.specialCards.remove(c);
                this.monopole();
                break;
            }
        }
    }

    // Carte chevalier :
    protected void knight() {
        BoxIG b = this.moveThief();
        // Ensuite, le joueur courant choisi le joueur a qui il veut voler une ressource :
        PlayerIG victim = this.selectPlayerToStealFrom(b);
        // Si le joueur courant a choisi une case avec des colonies adjacentes, 
        // alors le joueur designe par le joueur courant subit le vol : 
        if (victim!=null) this.steal(victim);
        this.knights++;
        if (this.knights==3 && !Game.army) {
            Game.army = true;
            this.victoryPoints += 2;
            Game.hasTheStrongestArmy = this;
            System.out.println("Vous avez gagne 2 points de victoire");
        } else if (Game.army && Game.hasTheStrongestArmy!=this
                && this.knights>Game.hasTheStrongestArmy.knights) {
            Game.hasTheStrongestArmy.victoryPoints -= 2;
            this.victoryPoints += 2;
            System.out.println("Vous avez gagne 2 points de victoire");
            Game.hasTheStrongestArmy = this;
        }
    }

    // Carte progrès invention :
    protected void invention() {
        inventionCard = true;
        new Invention(this).setVisible(true);
    }

    protected void inventionFunc(String s) {
        this.inventory.replace(s, this.inventory.get(s)+1);
    }

    // Carte progrès monopole :
    protected void monopole() {
        new Monopole(this).setVisible(true);
    }


    // Achat d'une carte developpement :
    protected void buySpecialCard() {
        if(this.canBuyACard()){
            this.inventory.replace("Roche", this.inventory.get("Roche")-1); 
            this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
            this.inventory.replace("Ble", this.inventory.get("Ble")-1); 
            this.notUsableCards.add(Game.DECK.getDeck().pop());
        }
    }
    

    ////////// Route la plus longue du joueur courant //////////

    protected int longestRoad() {
        ArrayList<RoadIG> startRoads = new ArrayList<RoadIG>();
        for (RoadIG r : this.roads)  {
            if (r.isAStartRoad()) startRoads.add(r);
        }
        int[] distances = new int[startRoads.size()];
        for (int i=0; i<startRoads.size(); i++) {
            ArrayList<RoadIG> crossedRoads = new ArrayList<RoadIG>();
            crossedRoads.add(startRoads.get(i));
            distances[i] = this.calculateLongestRoad(startRoads.get(i), crossedRoads, 1);
        }
        return Game.getMax(distances);
    }
    
    private final int calculateLongestRoad(RoadIG road, ArrayList<RoadIG> crossedRoads, int acc) {
        if (!road.hasLinkedRoads()) return acc;
        if (road.getLinkedRoads().size()==1) {
            if (crossedRoads.contains(road.getLinkedRoads().get(0))) return acc;
            crossedRoads.add(road.getLinkedRoads().get(0));
            return this.calculateLongestRoad(road.getLinkedRoads().get(0), crossedRoads, acc+1);
        }
        if (road.getLinkedRoads().size()==2) {
            if (crossedRoads.contains(road.getLinkedRoads().get(0))
            || crossedRoads.contains(road.getLinkedRoads().get(1))) return acc;
            crossedRoads.add(road.getLinkedRoads().get(0));
            int a = this.calculateLongestRoad(road.getLinkedRoads().get(0), crossedRoads, acc+1);
            crossedRoads.add(road.getLinkedRoads().get(1));
            int b = this.calculateLongestRoad(road.getLinkedRoads().get(1), crossedRoads, acc+1);
            return Math.max(a,b);
        }
        if (road.getLinkedRoads().size()==3) {
            if (crossedRoads.contains(road.getLinkedRoads().get(0))
            || crossedRoads.contains(road.getLinkedRoads().get(1))
            || crossedRoads.contains(road.getLinkedRoads().get(2))) return acc;
            crossedRoads.add(road.getLinkedRoads().get(0));
            int a = this.calculateLongestRoad(road.getLinkedRoads().get(0), crossedRoads, acc+1);
            crossedRoads.add(road.getLinkedRoads().get(1));
            int b = this.calculateLongestRoad(road.getLinkedRoads().get(1), crossedRoads, acc+1);
            crossedRoads.add(road.getLinkedRoads().get(2));
            int c = this.calculateLongestRoad(road.getLinkedRoads().get(2), crossedRoads, acc+1);
            int[] tab = {a,b,c};
            return Game.getMax(tab);
        }
        return acc;
    }

}