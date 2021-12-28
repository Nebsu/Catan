package Catan;

import java.util.*;
import Catan.Exceptions.*;

class Player {
    
    ////////// Attributs //////////

    protected final String name; // nom du joueur
    protected final char symbol; // symbole du pion du joueur (son numéro)
    protected final String color; // couleur du pion du joueur
    protected int victoryPoints; // points de victoire du joueur
    protected final Map<String, Integer> inventory; // ressources du joueur
    protected final ArrayList<Road> roads; // routes construites par le joueur sur le plateau
    protected final ArrayList<Card> specialCards; // cartes développement piochées par le joueur
    protected final ArrayList<Colony> coloniesOnPlayBoard; // colonies construites par le joueur sur le plateau
    protected final ArrayList<Harbor> harbors; // ports possédés par le joueur
    protected int knights; // nombre de cartes chevalier jouées par le joueur

    ////////// Constructeur et fonctions associées à ce dernier ////////// 

    Player(String name, int s) {
        this.name = name;
        this.symbol = String.valueOf(s).charAt(0);
        this.color = this.setColor(s);
        this.victoryPoints = 0;
        this.inventory = new HashMap<String, Integer>();
        this.inventorySetup();
        this.roads = new ArrayList<Road>();
        this.specialCards = new ArrayList<Card>();
        this.coloniesOnPlayBoard = new ArrayList<Colony>();
        this.harbors = new ArrayList<Harbor>();
        this.knights = 0;
    }
    // Couleur du pion du joueur :
    private final String setColor(int s) {
        switch (s) {
            case 1: return CatanTerminal.WHITE_BRIGHT; // le joueur 1 joue les blancs
            case 2: return CatanTerminal.BLUE_BRIGHT; // le joueur 2 joue les bleus
            case 3: return CatanTerminal.PURPLE_BRIGHT; // le joueur 3 joue les violets
            case 4: return CatanTerminal.RED_BRIGHT; // le joueur 4 joue les rouges
        }
        return null;
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
        return (this.color+this.name+" possede "+this.victoryPoints+" points de victoire."+CatanTerminal.RESET);
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
        Colony lastConstructedColony = this.coloniesOnPlayBoard.get(this.coloniesOnPlayBoard.size()-1);
        Box[] boxes = lastConstructedColony.boxes;
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
        System.out.println("Votre inventaire : "+this.inventory);
    }


    ////////// Fonctions booléennes //////////

    protected boolean isWinner() {return (this.victoryPoints==10);}
    
    protected boolean canConstructColony() {
        int numberOfColonies = 0;
        for (Colony col : this.coloniesOnPlayBoard) {
            if (!col.isCity) numberOfColonies++;
        }
        return ((this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1 &&
                this.inventory.get("Laine")>=1 && this.inventory.get("Ble")>=1) &&
                (numberOfColonies<5) && (!CatanTerminal.PLAYBOARD.isFilledLocations()));
    }

    protected boolean canConstructCity() {
        int numberOfCities = 0;
        for (Colony col : this.coloniesOnPlayBoard) {
            if (col.isCity) numberOfCities++;
        }
        return ((numberOfCities<4) && 
                (this.inventory.get("Roche")>=3 && this.inventory.get("Ble")>=2));
    }

    protected boolean canConstructRoad() {
        return ((this.roads.size()<15) && (!CatanTerminal.PLAYBOARD.isFilledPaths()) &&
                (this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1));
    }

    protected boolean canBuyACard() {
        return (!CatanTerminal.DECK.getDeck().isEmpty() && this.inventory.get("Roche")>=1 
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
        for (Harbor h : this.harbors) {
            if (h.type.equals("Special")) {
                if (canExchange(2, h.ressource)) return true;
            }
            if (h.type.equals("Simple")) {
                if (canExchange(3, null)) return true;
            }
        }
        return false;
    }
    

    ////////// FONCTIONS DU JEU //////////

    // Fonction principale (tour du joueur) :
    protected void play() {
        System.out.println("Au tour de "+this.name+" :");
        // Proposition d'utilisation d'une carte développement quand cela est possible :
        if (!this.specialCards.isEmpty()) {
            System.out.println("Votre inventaire : "+this.inventory);
            System.out.println("Vos cartes developpement :\n"+this.specialCards);
        }
        this.proposeToUseSpecialCard();
        int dice = throwDices();
        System.out.println("Resultat du lancer : " + dice);
        if (dice!=7) earnResources(dice);
        else {
            System.out.println("Voleur active");
            this.thief();
        }
        CatanTerminal.PLAYBOARD.display();
        System.out.println("Votre inventaire : "+this.inventory);
        // Proposition d'utilisation d'une carte développement quand cela est possible :
        if (!this.specialCards.isEmpty()) 
            System.out.println("Vos cartes developpement :\n"+this.specialCards);
        this.proposeToUseSpecialCard();
        // Phase de commerce :
        this.proposeToExchange41();
        this.proposeToUseHarbor();
        // Phase de construction :
        this.proposeToConstructColony();
        this.proposeToConstructCity();
        this.proposeToConstructRoad();
        // Proposition d'achat d'une carte développement quand cela est possible :
        this.proposeToBuySpecialCard();
        if (!this.specialCards.isEmpty()) {
            if (this.specialCards.get(this.specialCards.size()-1).id==0
                && this.victoryPoints==9) {
                    this.specialCards.remove(this.specialCards.size()-1);
                    this.victoryPoints++;
            }
        }
    }


    // Lancé des dés :
    private static int throwDices() { 
        System.out.println("Tapez sur une touche, puis entree pour lancer les des :");
        Scanner sc = new Scanner(System.in);
        sc.next();
        System.out.println();
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        return (dice1+dice2);
    }

    // Répartition des ressources selon le résultat du lancé des dés :
    protected final static void earnResources(int dice) {
        // On récupère la ou les cases designée(s) par les dés :
        ArrayList<Box> boxes = CatanTerminal.PLAYBOARD.getBoxes(dice);
        // On récupère les emplacement adjacents de ces cases :
        ArrayList<Location> locations = new ArrayList<Location>();
        for (Box b : boxes) {
            ArrayList<Location> loc = b.getLocations();
            for (int i=0; i<loc.size(); i++) locations.add(loc.get(i));
        }
        // On va verifier si les joueurs possèdent une colonie sur l'un des ces emplacements :
        ArrayList<Colony> colonies = new ArrayList<Colony>();
        for (Location l : locations) {
            if (l instanceof Colony) {
                colonies.add((Colony) l);
            }
        }
        // Cas où aucune colonie est adjacentes aux cases designees par les des :
        if (colonies.isEmpty()) {
            System.out.println("Pas de chance, personne n'a rien gagne");
            return;
        }
        for (Colony c : colonies) System.out.println(c);
        // Les joueurs concernes gagnent une ressource de chaque case designee par les des :
        for (Colony c : colonies) {
            for (int i=0; i<c.boxes.length; i++) {
                if (c.boxes[i].number==dice && !c.boxes[i].hasThief) {
                    Integer a = c.player.inventory.get(c.boxes[i].ressource);
                    if (c.isCity)
                        c.player.inventory.put(c.boxes[i].ressource, a+Integer.valueOf(2));
                    else 
                        c.player.inventory.put(c.boxes[i].ressource, a+Integer.valueOf(1));
                }
            }
        }
    }


    ////////// Fonctions du voleur //////////

    // Fonction principale du voleur :
    protected void thief() {
        // On compte les ressources de chaque joueur :
        int[] nbRessources = new int[CatanTerminal.PLAYERS.length];
        for (int i=0; i<CatanTerminal.PLAYERS.length; i++) {
            nbRessources[i] += CatanTerminal.PLAYERS[i].nbRessources(null);
        }
        // Les joueurs qui possèdent plus de 8 ressources,
        //  doivent donner la moitie de leurs ressources au voleur :
        for (int i=0; i<nbRessources.length; i++) {
            if (nbRessources[i]>=8) CatanTerminal.PLAYERS[i].giveRessources(nbRessources[i]);
        }
        // Ensuite, le joueur qui a lance les des deplace le voleur :
        Box b = this.moveThief();
        // Ensuite, le joueur courant choisi le joueur a qui il veut voler une ressource :
        Player victim = this.selectPlayerToStealFrom(b);
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
                    System.out.println("Vous avez perdu 1 "+line);
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
    protected Box moveThief() {
        CatanTerminal.PLAYBOARD.display();
        Box res = null;
        Scanner sc = new Scanner(System.in);
        do {
            try {
                System.out.println(this.name+", placez le voleur sur la case de votre choix :");
                int[] indexs = scanLocationOrPath(sc);
                int k = indexs[0]-1; int l = indexs[1]-1;
                if (k<0 || k>4 || l<0 || l>4) throw new WrongInputException();
                if (CatanTerminal.PLAYBOARD.boxes[k][l]==CatanTerminal.PLAYBOARD.thief) throw new IllegalStateException();
                CatanTerminal.PLAYBOARD.boxes[k][l].hasThief = true;
                CatanTerminal.PLAYBOARD.thief.hasThief = false;
                CatanTerminal.PLAYBOARD.thief = CatanTerminal.PLAYBOARD.boxes[k][l];
                res = CatanTerminal.PLAYBOARD.boxes[k][l];
                break;
            } catch (IllegalArgumentException ill) {
                System.out.println("Erreur : Vous etes oblige de deplacer le voleur sur une nouvelle case");
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
            }
        } while (true);
        CatanTerminal.PLAYBOARD.updatePaths();
        CatanTerminal.PLAYBOARD.display();
        return res;
    }

    // Choix de la cible du joueur pour voler des ressources : 
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
        System.out.println("Choisissez le joueur que vous voulez racketter :");
        Player selectedPlayer = null;
        Scanner sc = new Scanner(System.in);
        do {
            try {
                System.out.println("Tapez l'un des noms ci-dessous :");
                for (Player p : nearPlayers) 
                    System.out.print(p.name+"  ");
                System.out.println();
                String name = sc.nextLine();
                if (!playersNames.contains(name)) throw new WrongInputException();
                if (name.equals(this.name)) throw new WrongInputException();
                for (Player p : nearPlayers) {
                    if (p.name.equals(name)) {
                        selectedPlayer = p;
                        System.out.println("Vous avez decide de voler une carte a "+selectedPlayer.name);
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
    protected void steal(Player victim) {
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
                    System.out.println("Vous avez pris 1 bois a "+victim.name); break;
            case 2: Integer b = victim.inventory.get("Argile");
                    victim.inventory.put("Argile", b-Integer.valueOf(1));
                    b = this.inventory.get("Argile");
                    this.inventory.put("Argile", b+Integer.valueOf(1));
                    System.out.println("Vous avez pris 1 argile a "+victim.name); break;
            case 3: Integer c = victim.inventory.get("Laine");
                    victim.inventory.put("Laine", c-Integer.valueOf(1));
                    c = this.inventory.get("Laine");
                    this.inventory.put("Laine", c+Integer.valueOf(1));
                    System.out.println("Vous avez pris 1 laine a "+victim.name); break;
            case 4: Integer d = victim.inventory.get("Ble");
                    victim.inventory.put("Ble", d-Integer.valueOf(1));
                    d = this.inventory.get("Ble");
                    this.inventory.put("Ble", d+Integer.valueOf(1));
                    System.out.println("Vous avez pris 1 blé a "+victim.name); break;
            case 5: Integer e = victim.inventory.get("Roche");
                    victim.inventory.put("Roche", e-Integer.valueOf(1));
                    e = this.inventory.get("Roche");
                    this.inventory.put("Roche", e+Integer.valueOf(1));
                    System.out.println("Vous avez pris 1 roche a "+victim.name); break;
        }
    }


    ////////// Fonctions de proposition //////////

    // Proposition d'échange de 4 ressources du joueur contre une de son choix,
    // (échange réalisable du moment que le joueur a au moins 4 ressources,
    //  même si ce dernier ne possède pas de port) :
    protected void proposeToExchange41() {
        if (canExchange(4, null)) {
            System.out.println("Voulez-vous echanger 4 ressources contre une de votre choix ?");
            Scanner sc = new Scanner(System.in);
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    if (line.equals("OUI")) {
                        this.exchange(4, null);
                        System.out.println("Votre inventaire :"+this.inventory);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

    // Proposition d'utilisation d'un port pour faire un échange :
    protected void proposeToUseHarbor() {
        if (this.canUseHarbor()) {
            System.out.println("Vous possedez un ou plusieurs port(s).");
            System.out.println("Voulez vous faire un echange base sur l'un de vos ports ?");
            Scanner sc = new Scanner(System.in);
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    if (line.equals("OUI")) {
                        this.useHarbor();
                        System.out.println("Votre inventaire :"+this.inventory);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

    // Proposition de construction d'une colonie : 
    // on demande au joueur s'il veut construire une colonie/ville/route
    // ou utiliser/acheter une carte developpement quand c'est possible :
    protected void proposeToConstructColony() {
        if (this.canConstructColony()) {
            System.out.println("Voulez-vous construire une colonie ?");
            System.out.println("Cout : 1 bois, 1 argile, 1 laine et 1 ble");
            Scanner sc = new Scanner(System.in);
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    if (line.equals("OUI")) {
                        this.buildColony(false);
                        System.out.println("Votre inventaire :"+this.inventory);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

    // Proposition de construction d'une ville :
    protected void proposeToConstructCity() {
        if (this.canConstructCity()) {
            System.out.println("Voulez-vous construire une ville ?");
            System.out.println("Cout : 3 roches et 2 bles");
            Scanner sc = new Scanner(System.in);
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    if (line.equals("OUI")) {
                        this.buildCity();
                        System.out.println("Votre inventaire :"+this.inventory);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

    // Proposition de construction d'une route :
    protected void proposeToConstructRoad() {
        if (this.canConstructRoad()) {
            System.out.println("Voulez-vous construire une route ?");
            System.out.println("Cout : 1 bois et 1 argile");
            Scanner sc = new Scanner(System.in);
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    if (line.equals("OUI")) {
                        this.buildRoad(false, false);
                        System.out.println("Votre inventaire :"+this.inventory);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

    // Proposition d'utilisation d'une carte developpement :
    protected void proposeToUseSpecialCard() {
        if (!this.specialCards.isEmpty()) {
            System.out.println("Voulez vous utiliser une carte ?");
            Scanner sc = new Scanner(System.in);
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    if (line.equals("OUI")) {
                        this.useSpecialCard();
                        System.out.println("Vos cartes developpement :\n"+this.specialCards);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }

    // Proposition d'achat d'une carte developpement :
    protected void proposeToBuySpecialCard() {
        if (this.canBuyACard()) {
            do {
                System.out.println("Voulez-vous acheter une carte developpement ?");
                System.out.println("Cout : 1 roche, 1 laine et 1 ble");
                Scanner sc = new Scanner(System.in);
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    if (line.equals("OUI")) {
                        this.buySpecialCard();
                        System.out.println("Votre inventaire :"+this.inventory);
                        System.out.println("Vos cartes developpement :\n"+this.specialCards);
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        }
    }


    ////////// Fonctions de construction //////////

    // Construction d'une colonie :
    // Remarque : on a décidé de ne pas implémenter le fait que toute colonie doit être distante d’au moins 2 intersections
    // En effet, le plateau est trop petit pour pouvoir appliquer cette règle de distance
    protected void buildColony(boolean isFree) {
        Scanner sc = new Scanner(System.in);
        do {
            try {
                System.out.println(this.name+", placez votre colonie :");
                int[] indexs = scanLocationOrPath(sc);
                int k = indexs[0]; int l = indexs[1];
                if (k-1<0 || k-1>4 || l-1<0 || l-1>4) throw new IndexOutOfBoundsException();
                if (CatanTerminal.PLAYBOARD.locations[k-1][l-1] instanceof Colony) throw new WrongInputException();
                if (isFree) {
                    if (CatanTerminal.PLAYBOARD.locations[k-1][l-1].hasAnHarbor())
                        this.harbors.add(CatanTerminal.PLAYBOARD.locations[k-1][l-1].getHarbor());
                    CatanTerminal.PLAYBOARD.locations[k-1][l-1] = new Colony(CatanTerminal.PLAYBOARD.locations[k-1][l-1].boxes, k-1, l-1, this);
                    this.coloniesOnPlayBoard.add((Colony) CatanTerminal.PLAYBOARD.locations[k-1][l-1]);
                } else {
                    ArrayList<Location> endPoints = this.getEndPoints();
                    if (!endPoints.contains(CatanTerminal.PLAYBOARD.locations[k-1][l-1])) throw new InexistantRoadException();
                    if (CatanTerminal.PLAYBOARD.locations[k-1][l-1].hasAnHarbor())
                        this.harbors.add(CatanTerminal.PLAYBOARD.locations[k-1][l-1].getHarbor());
                    CatanTerminal.PLAYBOARD.locations[k-1][l-1] = new Colony(CatanTerminal.PLAYBOARD.locations[k-1][l-1].boxes, k-1, l-1, this);
                    this.coloniesOnPlayBoard.add((Colony) CatanTerminal.PLAYBOARD.locations[k-1][l-1]);
                    this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                    this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
                    this.inventory.replace("Ble", this.inventory.get("Ble")-1); 
                    this.inventory.replace("Argile", this.inventory.get("Argile")-1);
                }
                this.victoryPoints++;
                break;
            } catch (IndexOutOfBoundsException ind) {
                System.out.println("Erreur : cet emplacement n'existe pas");
            } catch (WrongInputException w) {
                System.out.println("Erreur : cet emplacement est occupe");
            } catch (InexistantRoadException ire) {
                System.out.println("Erreur : cet emplacement n'est pas en contact avec l'une de vos routes");
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
            }
        } while (true);
        CatanTerminal.PLAYBOARD.updatePaths();
        CatanTerminal.PLAYBOARD.display();
    }

    // Construction d'une ville :
    protected void buildCity() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Colony col : this.coloniesOnPlayBoard) {
            System.out.println(col);
            ids.add(col.id);
        }
        do {   
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Pour choisir la colonie a transformer en ville,");
                System.out.println("veuillez tapez le numero de la colonie en question :");
                int selectedId = sc.nextInt();
                if (!ids.contains(selectedId)) throw new WrongInputException();
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
                break;
            } catch (WrongInputException w) {
                System.out.println("Erreur : Cette colonie n'existe pas ou ne vous appartient pas");
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
            }
        } while (true);
        CatanTerminal.PLAYBOARD.updatePaths();
        CatanTerminal.PLAYBOARD.display();
    }

    // Construction d'une route :
    protected void buildRoad(boolean isFree, boolean beginning) {
        CatanTerminal.PLAYBOARD.display();
        Scanner sc = new Scanner(System.in);
        char c;
        do {
            try {
                System.out.println(this.name+", voulez-vous placer une route " 
                +"horizontale ou verticale ?");
                System.out.println("Tapez H pour horizontale ou V pour verticale :");
                c = sc.nextLine().charAt(0);
                if (c!='H' && c!='V') throw new WrongInputException();
                System.out.println("Placez votre route :");
                int[] indexs = scanLocationOrPath(sc);
                int k = indexs[0]-1; int l = indexs[1]-1;
                if (c=='H') {
                    if (k<0 || k>4 || l<0 || l>3) throw new IndexOutOfBoundsException();
                } else {
                    if (k<0 || k>3 || l<0 || l>4) throw new IndexOutOfBoundsException();
                }
                if ((c=='H' && CatanTerminal.PLAYBOARD.horizontalPaths[k][l] instanceof Road) || 
                    (c=='V' && CatanTerminal.PLAYBOARD.verticalPaths[k][l] instanceof Road)) {
                    System.out.println("Erreur : cet emplacement est occupe");
                } else {
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
                        } catch (IllegalStateException e) {
                            System.out.println("Erreur : Vous devez construire votre route a cote de la colonie que vous venez de construire");
                        } catch (InexistantRoadException ire) {
                            System.out.println(ice);
                            System.out.println(ire);
                        }
                    }
                }
            } catch (InexistantRoadException ind) {
                System.out.println("Erreur : ce chemin n'existe pas");
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
            }
        } while (true);
        CatanTerminal.PLAYBOARD.display();
    }

    // Construction d'une route à côté d'une colonie :
    protected Road buildRoadNextToColony(char c, Path selectedPath, boolean beginning) throws InexistantColonyException {
        if (selectedPath.point1 instanceof Colony) {
            Colony col = (Colony) selectedPath.point1;
            if (col.player==this && !beginning)
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 1);
            if (col==this.coloniesOnPlayBoard.get(this.coloniesOnPlayBoard.size()-1) && beginning)
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 1);
        }
        if (selectedPath.point2 instanceof Colony) {
            Colony col = (Colony) selectedPath.point2;
            if (col.player==this && !beginning) 
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 2);
            if (col==this.coloniesOnPlayBoard.get(this.coloniesOnPlayBoard.size()-1) && beginning)
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 2);
        }
        throw new InexistantColonyException();
    }

    // Construction d'une route à côté d'une autre route :
    protected Road buildRoadNextToRoad(char c, Path selectedPath) throws InexistantRoadException {
        ArrayList<Location> endPoints = this.getEndPoints();
        for (Location endPoint : endPoints) {
            if (selectedPath.point1==endPoint) {
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 1);
            }
            if (selectedPath.point2==endPoint) {
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 2);
            }
        }
        throw new InexistantRoadException();
    }

    // Fonction pour récupérer les emplacements d'arrivée de toutes les routes
    // construites par le joueur courant :
    protected ArrayList<Location> getEndPoints() {
        ArrayList<Location> endPoints = new ArrayList<Location>();
        for (Road PLAYBOARD : this.roads) {
            endPoints.add(PLAYBOARD.endPoint);
        }
        return endPoints;
    }

    // Fonction auxiliaire pour récuperer les coordonnées d'un(e) case/emplacement/chemin :
    private final static int[] scanLocationOrPath(Scanner sc) throws Exception {
        try {
            String s = sc.nextLine();
            if (s.length()>2 || s.length()<=0) throw new RuntimeException();
            System.out.println();
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
    protected void exchange(int n, String ressource) {
        if (ressource==null) {
            System.out.println("Choisissez la ressource dont vous voulez donner "+n+" unites :");
            Set<String> keys = this.inventory.keySet();
            ArrayList<String> selectables = new ArrayList<String>();
            for (String key : keys) {
                if (this.inventory.get(key)>=n) selectables.add(key);
            }
            Scanner sc = new Scanner(System.in);
            do {
                try {
                    System.out.println("Tapez l'une des ressources ci-dessous :");
                    for (String sel : selectables) 
                        System.out.print(sel+"   ");
                    System.out.println();
                    String line = sc.nextLine();
                    if (!selectables.contains(line)) throw new WrongInputException();
                    this.exchange(n, line);
                    return;
                } catch (Exception e) {
                    System.out.println(WrongInputException.MESSAGE);
                }
            } while (true);
        } else {
            Integer a = this.inventory.get(ressource);
            this.inventory.put(ressource, a-Integer.valueOf(n));
            System.out.println("Vous avez donne "+n+" "+ressource);
            System.out.println("Veuillez maintenant prendre une ressource de votre choix :");
            System.out.println("Votre inventaire : "+this.inventory);
            System.out.println("Choisissez une ressource :");
            Scanner sc = new Scanner(System.in);
            do {
                try {
                    System.out.println("Tapez Bois, Argile, Laine, Ble ou Roche :");
                    String line = sc.nextLine();
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
    protected void useHarbor() {
        int[] ids = new int[this.harbors.size()];
        int a = 0;
        System.out.println("Vos ports :");
        for (Harbor h : this.harbors) {
            ids[a] = h.id;
            a++;
            System.out.print(h.toStringWithId()+"   ");
        }
        System.out.println();
        System.out.println("Choisissez l'un de vos ports :");
        do {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Tapez l'ID du port que vous voulez utiliser :");
                int selectedId = sc.nextInt();
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
            } catch (NotEnoughRessourcesException not) {
                System.out.println(not);
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
            }
        } while (true);
    }

    
    ////////// Fonctions des cartes developpement //////////

    // Utilisation d'une carte developpement :
    protected void useSpecialCard() {      
        System.out.println("Vos cartes developpement : "+this.specialCards);
        System.out.println("Choisissez la carte que vous voulez utiliser (ID)");
        System.out.println("0 = Point de victoire\n1 = Chevalier\n"+
        "2 = Construction de Route\n3 = Invention\n4 = Monopole");
        do {
            try {
                Scanner sc = new Scanner(System.in);
                int n = sc.nextInt();
                if (n!=0 && n!=1 && n!=2 && n!=3 && n!=4) throw new WrongInputException();
                for(Card c : this.specialCards){
                    if (c.id==0 && n==0) {
                        this.specialCards.remove(c);
                        this.victoryPoints++; 
                        System.out.println("Vous avez gagne un point de victoire");
                        break;
                    }
                    if(c.id==1 && n==1){
                        Box b = this.moveThief();
                        // Ensuite, le joueur courant choisi le joueur a qui il veut voler une ressource :
                        Player victim = this.selectPlayerToStealFrom(b);
                        // Si le joueur courant a choisi une case avec des colonies adjacentes, 
                        // alors le joueur designe par le joueur courant subit le vol : 
                        if (victim!=null) this.steal(victim);
                        this.specialCards.remove(c);
                        this.knights++;
                        if (this.knights==3 && !CatanTerminal.army) {
                            CatanTerminal.army = true;
                            this.victoryPoints += 2;
                            CatanTerminal.hasTheStrongestArmy = this;
                            System.out.println("Vous avez gagne 2 points de victoire");
                        } else if (CatanTerminal.army && CatanTerminal.hasTheStrongestArmy!=this
                                && this.knights>CatanTerminal.hasTheStrongestArmy.knights) {
                            CatanTerminal.hasTheStrongestArmy.victoryPoints -= 2;
                            System.out.println(CatanTerminal.hasTheStrongestArmy.name+" a perdu deux points de victoire");
                            this.victoryPoints += 2;
                            System.out.println("Vous avez gagne 2 points de victoire");
                            CatanTerminal.hasTheStrongestArmy = this;
                        }
                        break;
                    }
                    if(c.id==3 && n==3){
                        this.specialCards.remove(c);
                        invention();
                        break;
                    }
                    if(c.id==2 && n==2){
                        this.specialCards.remove(c);
                        carteRoute();
                        break;
                    }
                    if(c.id==4 && n==4){
                        this.specialCards.remove(c);
                        monopole();
                        break;
                    }
                }
                break;
            } catch (Exception e) {
                System.out.println(WrongInputException.MESSAGE);
            }
        } while (true);
    }

    // Carte progrès invention :
    protected void invention() {
        int acc = 1;
        Scanner sc = new Scanner(System.in);
        do{
            try{
                if(acc == 1){
                    System.out.println("Choisissez votre première ressource");
                }else if(acc == 2){
                    System.out.println("Choisissez votre deuxième ressource");
                }
                String s = sc.nextLine();
                if(s.equals("Roche") || s.equals("Laine") || s.equals("Ble") || s.equals("Argile") || s.equals("Bois")){
                    this.inventory.replace(s, this.inventory.get(s)+1);
                    acc = 2;
                    if(acc == 2) break;
                }else{
                    throw new WrongInputException();
                }
            }catch(WrongInputException e){
                System.out.println(e);
            }
        }while (true);
    }

    // Carte progrès route :
    protected void carteRoute() {
        System.out.println("Veuillez construire votre premiere route");
        this.buildRoad(true, false);
        System.out.println("Veuillez construire votre seconde route");
        this.buildRoad(true, false);
    }

    // Carte monopole :
    protected void monopole() {
        int n = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Votre inventaire : "+this.inventory);
        while(true){
            try{
                System.out.println("Veuillez choisir une ressource a monopoliser");
                System.out.println("Roche | Laine | Argile | Ble | Bois");
                String s = sc.nextLine();
                if(s.equals("Roche") || s.equals("Laine") || s.equals("Ble") || s.equals("Bois") || s.equals("Argile")){
                    for(Player p: CatanTerminal.PLAYERS){
                        if(p!=this){
                            n += p.inventory.get(s);
                            p.inventory.replace(s, 0);
                        }
                    }
                    this.inventory.replace(s, this.inventory.get(s) + n);
                    return;
                }else{
                    throw new WrongInputException();
                }
            }catch(WrongInputException e){
                System.out.println(e);
            }
        }
    }

    // Achat d'une carte developpement :
    protected void buySpecialCard() {
        this.inventory.replace("Roche", this.inventory.get("Roche")-1); 
        this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
        this.inventory.replace("Ble", this.inventory.get("Ble")-1); 
        this.specialCards.add(CatanTerminal.DECK.getDeck().pop());
    }
    

    ////////// Route la plus longue du joueur courant //////////

    protected int longestRoad() {
        ArrayList<Road> startRoads = new ArrayList<Road>();
        for (Road r : this.roads)  {
            if (r.isAStartRoad()) startRoads.add(r);
        }
        int[] distances = new int[startRoads.size()];
        for (int i=0; i<startRoads.size(); i++) {
            ArrayList<Road> crossedRoads = new ArrayList<Road>();
            crossedRoads.add(startRoads.get(i));
            distances[i] = this.calculateLongestRoad(startRoads.get(i), crossedRoads, 1);
        }
        return CatanTerminal.getMax(distances);
    }
    
    private final int calculateLongestRoad(Road road, ArrayList<Road> crossedRoads, int acc) {
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
            return CatanTerminal.getMax(tab);
        }
        return acc;
    }

}