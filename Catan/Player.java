package Catan;

import java.util.*;
import Catan.Exceptions.*;

class Player {
    
    protected final String name;
    protected final char symbol;
    private int victoryPoints;
    private final Map<String, Integer> inventory;
    private final ArrayList<Road> roads;
    private final ArrayList<Card> specialCards;
    private final ArrayList<Colony> coloniesOnPlayBoard;
    private int knights;

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
        this.inventory.put("Blé", 0); // blé
        this.inventory.put("Roche", 0); // minerai
    }
    
    @Override
    public String toString() {
        return (this.name+" possède "+this.victoryPoints+" points de victoire.");
    }


    // Fonctions booléennes :
    protected boolean isWinner() {return (this.victoryPoints==10);}

    protected boolean isRich() {return this.inventory.size()>=8;} 
    
    protected boolean canConstructColony() {
        return ((this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1 &&
                this.inventory.get("Laine")>=1 && this.inventory.get("Blé")>=1) &&
                (this.coloniesOnPlayBoard.size()<5) && (!CatanTerminal.p.isFilledLocations()));
    }
    protected boolean canConstructCity() {
        int numberOfCities = 0;
        for (Colony col : this.coloniesOnPlayBoard) {
            if (col.isCity) numberOfCities++;
        }
        return ((numberOfCities<4) && 
                (this.inventory.get("Roche")>=3 && this.inventory.get("Blé")>=2));
    }
    protected boolean canConstructRoad() {
        return ((this.roads.size()<15) && (!CatanTerminal.p.isFilledPaths()) &&
                (this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1));
    }
    protected boolean hasEnoughToBuyACard() {
        return (this.inventory.get("Roche")>=1 && this.inventory.get("Laine")>=1 &&
                this.inventory.get("Blé")>=1);
    }


    // Fonction principale (tour du joueur):
    protected void play() {
        System.out.println();
        System.out.println("Au tour de "+this.name+" :");
        int dice = throwDices();
        System.out.println("Résultat du lancer des dés : " + dice);
        if (dice!=7) earnResources(dice);
        else {
            System.out.println("Voleur activé");
            this.thief();
        }
        CatanTerminal.p.display();
        System.out.println("Votre inventaire : "+this.inventory);
        if (!this.specialCards.isEmpty()) System.out.println("Vos cartes développement : "+this.specialCards);
        this.proposeToUseSpecialCard();
        this.proposeToConstructColony();
        this.proposeToConstructCity();
        this.proposeToConstructRoad();
        this.proposeToBuySpecialCard();
    }


    // Lancé des dés :
    protected static int throwDices() { 
        System.out.println("Tapez sur une touche, puis entrée pour lancer les dés :");
        Scanner sc1 = new Scanner(System.in);
        sc1.next();
        System.out.println();
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        sc1.close();
        return (dice1+dice2);
    }


    // Répartition des ressources selon le résultat du lancé des dés :
    protected final static void earnResources(int dice) {
        // On récupère la ou les cases désignée(s) par les dés :
        ArrayList<Box> boxes = CatanTerminal.p.getBoxes(dice);
        // On récupère les emplacement adjacents de ces cases :
        ArrayList<Location> locations = new ArrayList<Location>();
        for (Box b : boxes) {
            ArrayList<Location> loc = b.getLocations();
            for (int i=0; i<loc.size(); i++) locations.add(loc.get(i));
        }
        // On va vérifier si les joueurs possèdent une colonie sur l'un des ces emplacements :
        ArrayList<Colony> colonies = new ArrayList<Colony>();
        for (Location l : locations) {
            if (l instanceof Colony) {
                colonies.add((Colony) l);
            }
        }
        // Cas où aucune colonie est adjacentes aux cases désignées par les dés :
        if (colonies.isEmpty()) {
            System.out.println("Pas de chance, personne n'a rien gagné");
            return;
        }
        for (Colony c : colonies) System.out.println(c);
        // Les joueurs concernés gagnent une ressource de chaque case désignée par les dés :
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
            nbRessources[i] += CatanTerminal.players[i].inventory.get("Blé");
            nbRessources[i] += CatanTerminal.players[i].inventory.get("Roche");
            System.out.println(nbRessources[i]);
        }
        // Les joueurs qui possèdent plus de 8 ressources,
        //  doivent donner la moitié de leurs ressources au voleur :
        for (int i=0; i<nbRessources.length; i++) {
            if (nbRessources[i]>=8) CatanTerminal.players[i].giveRessources(nbRessources[i]);
        }
        // Ensuite, le joueur qui a lancé les dés déplace le voleur :
        Box b = this.moveThief();    
        // Ensuite, le joueur courant choisi le joueur a qui il veut voler une ressource :
        Player victim = this.selectPlayerToStealFrom(b);
        // Si le joueur courant a choisi une case avec des colonies adjacentes, 
        // alors le joueur désigné par le joueur courant subit le vol : 
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
                    System.out.println("Tapez Bois, Argile, Laine, Blé ou Roche :");
                    String line = sc2.nextLine();
                    if (!line.equals("Bois") && !line.equals("Argile") && !line.equals("Laine") && 
                        !line.equals("Blé") && !line.equals("Roche")) throw new WrongInputException();
                    notOk = false;
                    Integer a = this.inventory.get(line);
                    this.inventory.put(line, a-Integer.valueOf(1));
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
        sc2.close();
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
        sc3.close();
        return res;
    }

    protected Player selectPlayerToStealFrom(Box b) {
        // On récupère les emplacement adjacents de la case :
        ArrayList<Location> locations = b.getLocations();
        // On va vérifier si le joueur possède une colonie sur l'un des ces emplacements :
        ArrayList<Colony> colonies = new ArrayList<Colony>();
        for (Location l : locations) {
            if (l instanceof Colony) colonies.add((Colony) l);
        }
        if (colonies.isEmpty()) {
            System.out.println("Aucune colonie à proximité de cette case");
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
        sc4.close();
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
            case 4: Integer d = victim.inventory.get("Blé");
                    victim.inventory.put("Blé", d-Integer.valueOf(1));
                    d = this.inventory.get("Blé");
                    this.inventory.put("Blé", d+Integer.valueOf(1)); break;
            case 5: Integer e = victim.inventory.get("Roche");
                    victim.inventory.put("Roche", e-Integer.valueOf(1));
                    e = this.inventory.get("Roche");
                    this.inventory.put("Roche", e+Integer.valueOf(1)); break;
        }
    }


    // Fonctions de proposition :
    // on demande au joueur s'il veut construire une colonie/ville/route
    // ou utiliser/acheter une carte développement quand c'est possible :
    // Proposition de construction d'une colonie : 
    protected void proposeToConstructColony() {
        if (this.canConstructColony()) {
            Scanner sc5 = new Scanner(System.in);
            boolean notOk = true;
            System.out.println("Voulez-vous construire une colonie ?");
            System.out.println("Coût : 1 bois, 1 argile, 1 laine et 1 blé");
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
            sc5.close();
        }
    }

    // Proposition de construction d'une ville :
    protected void proposeToConstructCity() {
        if (this.canConstructCity()) {
            Scanner sc6 = new Scanner(System.in);
            boolean notOk = true;
            System.out.println("Voulez-vous construire une ville ?");
            System.out.println("Coût : 3 roches et 2 blés");
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
            sc6.close();
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
            sc7.close();
        }
    }

    // Proposition d'utilisation d'une carte développement :
    protected void proposeToUseSpecialCard() {
        if (!this.specialCards.isEmpty()) {
            Scanner sc8 = new Scanner(System.in);
            boolean notOk = true;
            System.out.println("Voulez vous utiliser une carte ?");
            System.out.println("Coût : 1 roche, 1 laine et 1 blé");
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
            sc8.close();
        }
    }

    // Proposition d'achat d'une carte développement :
    protected void proposeToBuySpecialCard() {
        if (this.hasEnoughToBuyACard()) {
            Scanner sc9 = new Scanner(System.in);
            boolean notOk = true;
            do {
                System.out.println("Voulez-vous acheter une carte développement ?");
                System.out.println("Coût : 1 roche, 1 laine et 1 blé");
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
            sc9.close();
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
                if (k-1<0 || k-1>4 || l-1<0 || l-1>4) {
                    System.out.println("Erreur : cet emplacement n'existe pas");
                    notOk = true;
                } else if (CatanTerminal.p.locations[k-1][l-1] instanceof Colony) {
                    System.out.println("Erreur : cet emplacement est occupé");
                    notOk = true;
                } else {
                    CatanTerminal.p.locations[k-1][l-1] = new Colony(CatanTerminal.p.locations[k-1][l-1].boxes, k-1, l-1, this);
                    this.coloniesOnPlayBoard.add((Colony) CatanTerminal.p.locations[k-1][l-1]);
                    if (!isFree) {
                        this.inventory.replace("Bois", this.inventory.get("Bois")-1); 
                        this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
                        this.inventory.replace("Blé", this.inventory.get("Blé")-1); 
                        this.inventory.replace("Argile", this.inventory.get("Argile")-1);
                    }
                    this.victoryPoints++;
                    notOk = false;
                }
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                notOk = true;
            }
        } while (notOk);
        sc10.close();
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
            try (Scanner sc11 = new Scanner(System.in);) {
                System.out.println("Pour choisir la colonie à transformer en ville,");
                System.out.println("veuillez tapez le numéro de la colonie en question :");
                int selectedId = sc11.nextInt();
                if (!ids.contains(selectedId)) throw new WrongInputException();
                notOk = false;
                for (Colony col : this.coloniesOnPlayBoard) {
                    if (col.id==selectedId) {
                        col.isCity = true;
                        ((Colony) CatanTerminal.p.locations[col.indexI][col.indexJ]).isCity = true;
                        this.inventory.replace("Roche", this.inventory.get("Roche")-3); 
                        this.inventory.replace("Blé", this.inventory.get("Blé")-2); 
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
                    System.out.println("Erreur : cet emplacement est occupé");
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
        sc12.close();
        CatanTerminal.p.display();
    }
    private Road buildRoadNextToColony(char c, Path selectedPath) throws InexistantColonyException {
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
    private Road buildRoadNextToRoad(char c, Path selectedPath) throws InexistantRoadException {
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
    // Fonction auxiliaire pour récupérer les emplacements d'arrivée de toutes les routes
    // construites par le joueur courant :
    private ArrayList<Location> getEndPoints() {
        ArrayList<Location> endPoints = new ArrayList<Location>();
        for (Road p : this.roads) {
            endPoints.add(p.endPoint);
        }
        return endPoints;
    }
    // Fonction auxiliaire pour récupérer les coordonnées d'un(e) case/emplacement/chemin :
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

    
    // Fonctions des cartes développement :
    // Achat d'une carte développement :
    protected void buySpecialCard() {
        this.inventory.replace("Roche", this.inventory.get("Roche")-1); 
        this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
        this.inventory.replace("Blé", this.inventory.get("Blé")-1); 
        this.specialCards.add(CatanTerminal.deck.getDeck().pop());
    }

    // Utilisation d'une carte développement :
    protected void useSpecialCard() {
        Scanner sc13 = new Scanner(System.in);       
        System.out.println("Vos cartes développement : "+this.specialCards);
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
                        //TODO Carte Monopole
                    }
                }
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                notOk = true;
            }
        } while (notOk);
        sc13.close();
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
                if(s.equals("Roche") || s.equals("Laine") || s.equals("Blé") || s.equals("Argile") || s.equals("Bois")){
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
        sc14.close();
    }
    // Carte progrès route :
    protected void carteRoute() {
        System.out.println("Veuillez construire votre première route");
        buildRoad(true);
        System.out.println("Veuillez construire votre seconde route");
        buildRoad(true);
    }

    
    // Route la plus longue du joueur courant :
    protected int longestRoad() {
        // TODO
        return -1;
    }

}