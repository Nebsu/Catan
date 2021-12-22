package Catan;

import java.util.*;
import Catan.Exceptions.*;

class Player {
    
    protected final String name;
    protected final char symbol;
    protected int victoryPoints;
    protected final Map<String, Integer> inventory;
    protected final ArrayList<Road> roads;
    protected final ArrayList<Card> specialCards;
    protected final ArrayList<Colony> coloniesOnPlayBoard;
    protected int knights;

    final int getVictoryPoints() {return this.victoryPoints;}

    Player(String name, int s) {
        this.name = name;
        this.symbol = String.valueOf(s).charAt(0);
        this.victoryPoints = 0;
        this.inventory = new HashMap<String, Integer>();
        this.inventorySetup();
        this.roads = new ArrayList<Road>();
        this.specialCards = new ArrayList<Card>();
        this.coloniesOnPlayBoard = new ArrayList<Colony>();
        this.knights = 0;
    }
    private final void inventorySetup() {
        this.inventory.put("Bois", 0); // bois
        this.inventory.put("Argile", 0); // argile
        this.inventory.put("Laine", 0); // laine
        this.inventory.put("Ble", 0); // ble
        this.inventory.put("Roche", 0); // minerai
    }
    
    @Override
    public String toString() {
        return (this.name+" possède "+this.victoryPoints+" points de victoire.");
    }


    // Fonctions booleennes :
    protected boolean isWinner() {return (this.victoryPoints==10);}

    protected boolean isRich() {return this.inventory.size()>=8;} 
    
    protected boolean canConstructColony() {
        return ((this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1 &&
                this.inventory.get("Laine")>=1 && this.inventory.get("Ble")>=1) &&
                (this.coloniesOnPlayBoard.size()<5) && (!CatanTerminal.p.isFilledLocations()));
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
        return ((this.roads.size()<15) && (!CatanTerminal.p.isFilledPaths()) &&
                (this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1));
    }
    protected boolean hasEnoughToBuyACard() {
        return (this.inventory.get("Roche")>=1 && this.inventory.get("Laine")>=1 &&
                this.inventory.get("Ble")>=1);
    }


    // Fonction principale (tour du joueur):
    protected void play() {
        System.out.println("Au tour de "+this.name+" :");
        int dice = throwDices();
        System.out.println("Resultat du lancer des des : " + dice);
        if (dice!=7) earnResources(dice);
        else {
            System.out.println("Voleur active");
            this.thief();
        }
        CatanTerminal.p.display();
        System.out.println("Votre inventaire : "+this.inventory);
        if (!this.specialCards.isEmpty()) System.out.println("Vos cartes developpement : "+this.specialCards);
        this.proposeToUseSpecialCard();
        this.proposeToConstructColony();
        this.proposeToConstructCity();
        this.proposeToConstructRoad();
        this.proposeToBuySpecialCard();
    }


    // Lance des des :
    protected static int throwDices() { 
        System.out.println("Tapez sur une touche, puis entree pour lancer les des :");
        Scanner sc1 = new Scanner(System.in);
        sc1.next();
        
        System.out.println();
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        return (dice1+dice2);
    }


    // Repartition des ressources selon le resultat du lance des des :
    protected final static void earnResources(int dice) {
        // On recupère la ou les cases designee(s) par les des :
        ArrayList<Box> boxes = CatanTerminal.p.getBoxes(dice);
        // On recupère les emplacement adjacents de ces cases :
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


    // Fonctions du voleur :
    protected void thief() {
        // On compte les ressources de chaque joueur :
        int[] nbRessources = new int[CatanTerminal.players.length];
        for (int i=0; i<CatanTerminal.players.length; i++) {
            nbRessources[i] += CatanTerminal.players[i].inventory.get("Bois");
            nbRessources[i] += CatanTerminal.players[i].inventory.get("Argile");
            nbRessources[i] += CatanTerminal.players[i].inventory.get("Laine");
            nbRessources[i] += CatanTerminal.players[i].inventory.get("Ble");
            nbRessources[i] += CatanTerminal.players[i].inventory.get("Roche");
            System.out.println(nbRessources[i]);
        }
        // Les joueurs qui possèdent plus de 8 ressources,
        //  doivent donner la moitie de leurs ressources au voleur :
        for (int i=0; i<nbRessources.length; i++) {
            if (nbRessources[i]>=8) CatanTerminal.players[i].giveRessources(nbRessources[i]);
        }
        // Ensuite, le joueur qui a lance les des deplace le voleur :
        Box b = this.moveThief();    
        // Ensuite, le joueur courant choisi le joueur a qui il veut voler une ressource :
        Player victim = this.selectPlayerToStealFrom(b);
        // Si le joueur courant a choisi une case avec des colonies adjacentes, 
        // alors le joueur designe par le joueur courant subit le vol : 
        if (victim !=null) this.steal(victim);
    }

    protected void giveRessources(int n) {
        System.out.println("Vous avez "+n+" ressources");
        n /= 2;
        System.out.println("Veuillez donner "+n+" ressources au voleur");
        boolean notOk = true;
        Scanner sc2 = new Scanner(System.in);
        for (int i=0; i<n; i++) {
            System.out.println("Votre inventaire : "+this.inventory);
            System.out.println("Choisissez une ressource à donner au voleur :");
            do {
                try {
                    System.out.println("Tapez Bois, Argile, Laine, Ble ou Roche :");
                    String line = sc2.nextLine();
                    if (!line.equals("Bois") && !line.equals("Argile") && !line.equals("Laine") && 
                        !line.equals("Ble") && !line.equals("Roche")) throw new WrongInputException();
                    notOk = false;
                    Integer a = this.inventory.get(line);
                    this.inventory.put(line, a-Integer.valueOf(1));
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }

    protected Box moveThief() {
        Scanner sc3 = new Scanner(System.in);
        boolean notOk;
        Box res = null;
        do {
            try {
                System.out.println(this.name+", placez le voleur sur la case de votre choix :");
                int[] indexs = scanLocationOrPath(sc3);
                int k = indexs[0]-1; int l = indexs[1]-1;
                if (k<0 || k>4 || l<0 || l>4) throw new WrongInputException();
                notOk = false;
                CatanTerminal.p.boxes[k][l].hasThief = true;
                res = CatanTerminal.p.boxes[k][l];
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                notOk = true;
            }
        } while (notOk);
        CatanTerminal.p.updatePaths();
        CatanTerminal.p.display();
        return res;
    }

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
        Scanner sc4 = new Scanner(System.in);
        boolean notOk = true;
        Player selectedPlayer = null;
        do {
            try {
                System.out.println("Tapez l'un des noms ci-dessous :");
                for (Player p : nearPlayers) 
                    System.out.print(p.name+"  ");
                System.out.println();
                String name = sc4.nextLine();
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
                System.out.println(WrongInputException.message);
                notOk = true;
            }
        } while (notOk);
        return selectedPlayer;
    }

    protected void steal(Player victim) {
        Random rd = new Random();
        int code = rd.nextInt(5)+1;
        switch (code) {
            case 1: Integer a = victim.inventory.get("Bois");
                    victim.inventory.put("Bois", a-Integer.valueOf(1));
                    a = this.inventory.get("Bois");
                    this.inventory.put("Bois", a+Integer.valueOf(1)); break;
            case 2: Integer b = victim.inventory.get("Argile");
                    victim.inventory.put("Argile", b-Integer.valueOf(1));
                    b = this.inventory.get("Argile");
                    this.inventory.put("Argile", b+Integer.valueOf(1)); break;
            case 3: Integer c = victim.inventory.get("Laine");
                    victim.inventory.put("Laine", c-Integer.valueOf(1));
                    c = this.inventory.get("Laine");
                    this.inventory.put("Laine", c+Integer.valueOf(1)); break;
            case 4: Integer d = victim.inventory.get("Ble");
                    victim.inventory.put("Ble", d-Integer.valueOf(1));
                    d = this.inventory.get("Ble");
                    this.inventory.put("Ble", d+Integer.valueOf(1)); break;
            case 5: Integer e = victim.inventory.get("Roche");
                    victim.inventory.put("Roche", e-Integer.valueOf(1));
                    e = this.inventory.get("Roche");
                    this.inventory.put("Roche", e+Integer.valueOf(1)); break;
        }
    }


    // Fonctions de proposition :
    // on demande au joueur s'il veut construire une colonie/ville/route
    // ou utiliser/acheter une carte developpement quand c'est possible :
    // Proposition de construction d'une colonie : 
    protected void proposeToConstructColony() {
        if (this.canConstructColony()) {
            Scanner sc5 = new Scanner(System.in);
            boolean notOk = true;
            System.out.println("Voulez-vous construire une colonie ?");
            System.out.println("Coût : 1 bois, 1 argile, 1 laine et 1 ble");
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc5.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("OUI")) this.buildColony(false);
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }

    // Proposition de construction d'une ville :
    protected void proposeToConstructCity() {
        if (this.canConstructCity()) {
            Scanner sc6 = new Scanner(System.in);
            boolean notOk = true;
            System.out.println("Voulez-vous construire une ville ?");
            System.out.println("Coût : 3 roches et 2 bles");
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc6.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("OUI")) this.buildCity();
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }

    // Proposition de construction d'une route :
    protected void proposeToConstructRoad() {
        if (this.canConstructRoad()) {
            Scanner sc7 = new Scanner(System.in);
            Boolean notOk = true;
            System.out.println("Voulez-vous construire une route ?");
            System.out.println("Coût : 1 bois et 1 argile");
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc7.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("OUI")) this.buildRoad(false);
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }

    // Proposition d'utilisation d'une carte developpement :
    protected void proposeToUseSpecialCard() {
        if (!this.specialCards.isEmpty()) {
            Scanner sc8 = new Scanner(System.in);
            boolean notOk = true;
            System.out.println("Voulez vous utiliser une carte ?");
            System.out.println("Coût : 1 roche, 1 laine et 1 ble");
            do {
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc8.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("OUI")) this.useSpecialCard();
                } catch (WrongInputException e) {
                    System.out.println(e);
                    notOk = true;
                }
            } while (notOk);
        }
    }

    // Proposition d'achat d'une carte developpement :
    protected void proposeToBuySpecialCard() {
        if (this.hasEnoughToBuyACard()) {
            Scanner sc9 = new Scanner(System.in);
            boolean notOk = true;
            do {
                System.out.println("Voulez-vous acheter une carte developpement ?");
                System.out.println("Coût : 1 roche, 1 laine et 1 ble");
                try {
                    System.out.println("Tapez OUI ou NON :");
                    String line = sc9.nextLine();
                    System.out.println();
                    if (!line.equals("OUI") && !line.equals("NON")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("OUI")) this.buySpecialCard();
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }


    // Fonctions de construction :
    // Construction d'une colonie :
    protected void buildColony(boolean isFree) {
        Scanner sc10 = new Scanner(System.in);
        boolean notOk;
        do {
            try {
                System.out.println(this.name+", placez votre colonie :");
                int[] indexs = scanLocationOrPath(sc10);
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
                notOk = false;
            } catch (IndexOutOfBoundsException ind) {
                System.out.println("Erreur : cet emplacement n'existe pas");
                notOk = true;
            } catch (WrongInputException w) {
                System.out.println("Erreur : cet emplacement est occupe");
                notOk = true;
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                notOk = true;
            }
        } while (notOk);
        CatanTerminal.p.updatePaths();
        CatanTerminal.p.display();
    }

    // Construction d'une ville :
    protected void buildCity() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Colony col : this.coloniesOnPlayBoard) {
            System.out.println(col);
            ids.add(col.id);
        }
        boolean notOk = true;
        do {   
            try {
                Scanner sc11 = new Scanner(System.in);
                System.out.println("Pour choisir la colonie à transformer en ville,");
                System.out.println("veuillez tapez le numero de la colonie en question :");
                int selectedId = sc11.nextInt();
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
            } catch (WrongInputException w) {
                System.out.println("Erreur : Cette colonie n'existe pas ou ne vous appartient pas");
                notOk = true;
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                notOk = true;
            }
        } while (notOk);
        CatanTerminal.p.display();
    }

    // Construction d'une route :
    protected void buildRoad(boolean isFree) {
        Scanner sc12 = new Scanner(System.in);
        boolean notOk = true;
        char c;
        do {
            try {
                System.out.println(this.name+", voulez-vous placer une route " 
                +"horizontale ou verticale ?");
                System.out.println("Tapez H pour horizontale ou V pour verticale :");
                c = sc12.nextLine().charAt(0);
                System.out.println();
                if (c!='H' && c!='V') throw new WrongInputException();
                System.out.println("Placez votre route :");
                int[] indexs = scanLocationOrPath(sc12);
                int k = indexs[0]-1; int l = indexs[1]-1;
                if (c=='H') {
                    if (k<0 || k>4 || l<0 || l>3) throw new IndexOutOfBoundsException();
                } else {
                    if (k<0 || k>3 || l<0 || l>4) throw new IndexOutOfBoundsException();
                }
                if ((c=='H' && CatanTerminal.p.horizontalPaths[k][l] instanceof Road) || (c=='V' && CatanTerminal.p.verticalPaths[k][l] instanceof Road)) {
                    System.out.println("Erreur : cet emplacement est occupe");
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
                        System.out.println(ice);
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
                            System.out.println(ire);
                            notOk = true;
                        }
                    }
                }
            } catch (InexistantRoadException ind) {
                System.out.println("Erreur : ce chemin n'existe pas");
                notOk = true;
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                notOk = true;
            }
        } while (notOk);
        CatanTerminal.p.display();
    }

    protected Road buildRoadNextToColony(char c, Path selectedPath) throws InexistantColonyException {
        if (selectedPath.point1 instanceof Colony) {
            Colony col = (Colony) selectedPath.point1;
            if (col.player==this) {
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 1);
            }
        }
        if (selectedPath.point2 instanceof Colony) {
            Colony col = (Colony) selectedPath.point2;
            if (col.player==this) {
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 2);
            }
        }
        throw new InexistantColonyException();
    }
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
    // Fonction auxiliaire pour recuperer les emplacements d'arrivee de toutes les routes
    // construites par le joueur courant :
    protected ArrayList<Location> getEndPoints() {
        ArrayList<Location> endPoints = new ArrayList<Location>();
        for (Road p : this.roads) {
            endPoints.add(p.endPoint);
        }
        return endPoints;
    }
    // Fonction auxiliaire pour recuperer les coordonnees d'un(e) case/emplacement/chemin :
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

    
    // Fonctions des cartes developpement :
    // Utilisation d'une carte developpement :
    protected void useSpecialCard() {
        Scanner sc13 = new Scanner(System.in);       
        System.out.println("Vos cartes developpement : "+this.specialCards);
        System.out.println("Choisissez la carte que vous voulez utiliser (ID)");
        System.out.println("1 = Chevalier\n2 = Construction de Route\n3 = Invention\n4 = Monopole");
        boolean notOk = true;
        do {
            try {
                int n = sc13.nextInt();
                if (n!=1 && n!=2 && n!=3 && n!=4) throw new WrongInputException();
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
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                notOk = true;
            }
        } while (notOk);
    }
    // Carte progrès invention :
    protected void invention() {
        Scanner sc14 = new Scanner(System.in);
        boolean notOk = true;
        int acc = 1;
        do{
            try{
                if(acc == 1){
                    System.out.println("Choisissez votre première ressource");
                }else if(acc == 2){
                    System.out.println("Choisissez votre deuxième ressource");
                }
                String s = sc14.nextLine();
                if(s.equals("Roche") || s.equals("Laine") || s.equals("Ble") || s.equals("Argile") || s.equals("Bois")){
                    this.inventory.replace(s, this.inventory.get(s)+1);
                    acc = 2;
                    if(acc == 2)notOk = false;
                }else{
                    throw new WrongInputException();
                }
            }catch(WrongInputException e){
                System.out.println(e);
            }
        }while (notOk && acc != 2);
    }
    // Carte progrès route :
    protected void carteRoute() {
        System.out.println("Veuillez construire votre première route");
        this.buildRoad(true);
        System.out.println("Veuillez construire votre seconde route");
        this.buildRoad(true);
    }
    // Carte monopole :
    protected void monopole() {
        int n = 0;
        Scanner sc = new Scanner(System.in);
        while(true){
            try{
                String s = sc.nextLine();
                System.out.println("Veuillez choisir une ressource à monopoliser");
                System.out.println("Roche | Laine | Argile | Ble | Bois");
                if(s.equals("Roche") || s.equals("Laine") || s.equals("Ble") || s.equals("Bois") || s.equals("Argile")){
                    for(Player p : CatanTerminal.players){
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
        this.specialCards.add(CatanTerminal.deck.getDeck().pop());
    }
    

    // Route la plus longue du joueur courant :
    protected int longestRoad() {
        // TODO
        return -1;
    }

}