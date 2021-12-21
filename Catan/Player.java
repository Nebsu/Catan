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

    boolean isWinner() {return (this.victoryPoints==10);}
    
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

    public void play() {
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

    protected static int throwDices() { 
        System.out.println("Tapez sur une touche, puis entrée pour lancer les dés :");
        Scanner sc0 = new Scanner(System.in);
        sc0.next();
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        return (dice1+dice2);
    }

    protected final static void earnResources(int dice) {
        // On récupère la ou les cases désignée(s) par les dés :
        ArrayList<Box> boxes = CatanTerminal.p.getBoxes(dice);
        for (Box b : boxes) System.out.println(b);
        System.out.println();
        // On récupère les emplacement adjacents de ces cases :
        ArrayList<Location> locations = new ArrayList<Location>();
        for (Box b : boxes) {
            Location[] loc = b.getLocations();
            for (int i=0; i<loc.length; i++) locations.add(loc[i]);
        }
        // On va vérifier si le joueur possède une colonie sur l'un des ces emplacements :
        ArrayList<Colony> colonies = new ArrayList<Colony>();
        for (Location l : locations) {
            if (l instanceof Colony) {
                colonies.add((Colony) l);
            }
        }
        if (colonies.isEmpty()) {
            System.out.println("Pas de chance, personne n'a rien gagné");
            return;
        }
        for (Colony c : colonies) System.out.println(c);
        for (Colony c : colonies) {
            for (int i=0; i<c.boxes.length; i++) {
                if (c.boxes[i].number==dice) {
                    Integer a = c.player.inventory.get(c.boxes[i].ressource);
                    if (c.isCity)
                        c.player.inventory.put(c.boxes[i].ressource, a+Integer.valueOf(2));
                    else 
                        c.player.inventory.put(c.boxes[i].ressource, a+Integer.valueOf(1));
                }
            }
        }
    }

    protected void thief() {

    }

    // Fonctions de proposition :
    // on demande au joueur s'il veut construire une colonie/ville/route
    // ou utiliser/acheter une carte développement quand c'est possible :
    private final void proposeToConstructColony() {
        if (this.canConstructColony()) {
            Scanner sc3 = new Scanner(System.in);
            boolean notOk = true;
            System.out.println("Voulez-vous construire une colonie ?");
            System.out.println("Coût : 1 bois, 1 argile, 1 laine et 1 blé");
            do {
                try {
                    System.out.println("Tapez oui ou non :");
                    String line = sc3.nextLine();
                    System.out.println();
                    if (!line.equals("oui") && !line.equals("non")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("oui")) this.buildColony(false);
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }
    private final void proposeToConstructCity() {
        if (this.canConstructCity()) {
            Scanner sc4 = new Scanner(System.in);
            boolean notOk = true;
            System.out.println("Voulez-vous construire une ville ?");
            System.out.println("Coût : 3 roches et 2 blés");
            do {
                try {
                    System.out.println("Tapez oui ou non :");
                    String line = sc4.nextLine();
                    System.out.println();
                    if (!line.equals("oui") && !line.equals("non")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("oui")) this.buildCity();
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }
    private final void proposeToConstructRoad() {
        if (this.canConstructRoad()) {
            Scanner sc2 = new Scanner(System.in);
            Boolean notOk = true;
            System.out.println("Voulez-vous construire une route ?");
            System.out.println("Coût : 1 bois et 1 argile");
            do {
                try {
                    System.out.println("Tapez oui ou non :");
                    String line = sc2.nextLine();
                    System.out.println();
                    if (!line.equals("oui") && !line.equals("non")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("oui")) this.buildRoad(false);
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }

    private void proposeToUseSpecialCard() {
        if (!this.specialCards.isEmpty()) {
            Scanner sc1 = new Scanner(System.in);
            boolean notOk = true;
            do {
                try {
                    System.out.println("Voulez vous utiliser une carte ?");
                    String s = sc1.next();
                    if (s.equals("Non")) return;
                    else if (s.equals("Oui")) {
                        useSpecialCard();
                        return;
                    } else throw new WrongInputException();
                } catch (WrongInputException e) {
                    System.out.println(e);
                }
            } while (notOk);
        }
    }

    private final void proposeToBuySpecialCard() {
        if (this.hasEnoughToBuyACard()) {
            Scanner sc5 = new Scanner(System.in);
            boolean notOk = true;
            do {
                System.out.println("Voulez-vous acheter une carte développement ?");
                System.out.println("Coût : 1 roche, 1 laine et 1 blé");
                try {
                    System.out.println("Tapez oui ou non :");
                    String line = sc5.nextLine();
                    System.out.println();
                    if (!line.equals("oui") && !line.equals("non")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("oui")) this.buySpecialCard();
                } catch (Exception e) {
                    System.out.println(WrongInputException.message);
                    notOk = true;
                }
            } while (notOk);
        }
    }

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

    // Fonctions de construction :
    // Construction d'une colonie :
    protected void buildColony(boolean isFree) {
        Scanner sc6 = new Scanner(System.in);
        boolean notOk;
        do {
            try {
                System.out.println(this.name+", placez votre colonie :");
                int[] indexs = scanLocationOrPath(sc6);
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
        CatanTerminal.p.updatePaths();
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
                Scanner sc7 = new Scanner(System.in);
                System.out.println("Pour choisir la colonie à transformer en ville,");
                System.out.println("veuillez tapez le numéro de la colonie en question :");
                int selectedId = sc7.nextInt();
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
    }

    // Construction d'une route :
    protected void buildRoad(boolean isFree) {
        Scanner sc8 = new Scanner(System.in);
        boolean notOk = true;
        char c;
        do {
            try {
                System.out.println(this.name+", voulez-vous placer une route " 
                +"horizontale ou verticale ?");
                System.out.println("Tapez h pour horizontale ou v pour verticale :");
                c = sc8.nextLine().charAt(0);
                System.out.println();
                if (c!='h' && c!='v') throw new WrongInputException();
                System.out.println("Placez votre route :");
                int[] indexs = scanLocationOrPath(sc8);
                int k = indexs[0]-1; int l = indexs[1]-1;
                if (c=='h') {
                    if (k<0 || k>4 || l<0 || l>3) throw new IndexOutOfBoundsException();
                } else {
                    if (k<0 || k>3 || l<0 || l>4) throw new IndexOutOfBoundsException();
                }
                if ((c=='h' && CatanTerminal.p.horizontalPaths[k][l] instanceof Road) || (c=='v' && CatanTerminal.p.verticalPaths[k][l] instanceof Road)) {
                    System.out.println("Erreur : cet emplacement est occupé");
                    notOk = true;
                } else {
                    try {
                        Road r = this.buildRoadNextToColony(c, (c=='h')? CatanTerminal.p.horizontalPaths[k][l] : CatanTerminal.p.verticalPaths[k][l]);
                        if (c=='h') CatanTerminal.p.horizontalPaths[k][l] = r;
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
                            Road r = this.buildRoadNextToRoad(c, (c=='h')? CatanTerminal.p.horizontalPaths[k][l] : CatanTerminal.p.verticalPaths[k][l]);
                            if (c=='h') CatanTerminal.p.horizontalPaths[k][l] = r;
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
    private ArrayList<Location> getEndPoints() {
        ArrayList<Location> endPoints = new ArrayList<Location>();
        for (Road p : this.roads) {
            endPoints.add(p.endPoint);
        }
        return endPoints;
    }
    
    // Fonctions des cartes :
    // METTRE LES EXCEPTIONS
    protected void useSpecialCard() {
        Scanner sc = new Scanner(System.in);       
        while(true) {
            System.out.println("Choisissez la carte que vous voulez utiliser (ID)");
            System.out.println("1 = Chevalier\n2 = Construction de Route\n3 = Invention\n4 = Monopole");
            String s = sc.next();
            int n = Integer.parseInt(s);
            if(n==1 || n==3 || n==2 || n==4){
                for(Card c : this.specialCards){
                    if(c.getId()==1 && n==1){
                        //Bouge le voleur
                        this.specialCards.remove(c);
                        this.knights++;
                        if (this.knights==3 && !CatanTerminal.army) {
                            CatanTerminal.army = true;
                            this.victoryPoints++;
                        }
                        return;
                    }else if(c.getId()==3 && n==3){
                        this.specialCards.remove(c);
                        invention();
                        return;
                    }else if(c.getId()==2 && n==2){
                        this.specialCards.remove(c);
                        carteRoute();
                        return;
                    }else if(c.getId()==4 && n==4){
                        //TODO Carte Monopole
                        return;
                    }
                }
            } else System.out.println("Veuillez saisir un entrée valide");
        }
    }

    protected void buySpecialCard() {
        this.inventory.replace("Roche", this.inventory.get("Roche")-1); 
        this.inventory.replace("Laine", this.inventory.get("Laine")-1); 
        this.inventory.replace("Blé", this.inventory.get("Blé")-1); 
        this.specialCards.add(CatanTerminal.p.deck.getDeck().pop());
    }

    protected void invention() {
        Scanner sc = new Scanner(System.in);
        boolean notOk = true;
        int acc = 1;
        do{
            try{
                if(acc == 1){
                    System.out.println("Choisissez votre première ressource");
                }else if(acc == 2){
                    System.out.println("Choisissez votre deuxième ressource");
                }
                String s = sc.nextLine();
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
        sc.close();
    }

    private void carteRoute() {
        System.out.println("Veuillez construire votre première route");
        buildRoad(true);
        System.out.println("Veuillez construire votre seconde route");
        buildRoad(true);
    }

}