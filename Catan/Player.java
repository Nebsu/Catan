package Catan;

import java.util.*;
import Catan.Exceptions.*;

class Player implements PlayerAction {
    
    protected final String name;
    protected final char symbol;
    private int victoryPoints;
    private final Map<String, Integer> inventory;
    private final ArrayList<Road> roads;
    private final ArrayList<Card> specialCards;
    private final ArrayList<Colony> coloniesOnPlayBoard;

    int getVictoryPoints() {return this.victoryPoints;}

    Player(String name, int s) {
        this.name = name;
        this.symbol = String.valueOf(s).charAt(0);
        this.victoryPoints = 0;
        this.inventory = new HashMap<String, Integer>();
        this.inventorySetup();
        this.roads = new ArrayList<Road>();
        this.specialCards = new ArrayList<Card>();
        this.coloniesOnPlayBoard = new ArrayList<Colony>();
    }
    private void inventorySetup() {
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

    public boolean isWinner() {return (this.victoryPoints==10);}

    boolean canConstructColony(PlayBoard p) {
        return ((this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1 &&
                this.inventory.get("Laine")>=1 && this.inventory.get("Blé")>=1) &&
                (this.coloniesOnPlayBoard.size()<5) && (!p.isFilledLocations()));
    }
    boolean canConstructCity() {
        int numberOfCities = 0;
        for (Colony col : this.coloniesOnPlayBoard) {
            if (col.isCity) numberOfCities++;
        }
        return ((numberOfCities<4) && 
                (this.inventory.get("Roche")>=3 && this.inventory.get("Blé")>=2));
    }
    boolean canConstructRoad(PlayBoard p) {
        return ((this.roads.size()<15) && (!p.isFilledPaths()) &&
                (this.inventory.get("Bois")>=1 && this.inventory.get("Argile")>=1));
    }
    boolean hasEnoughToBuyACard() {
        return (this.inventory.get("Roche")>=1 && this.inventory.get("Laine")>=1 &&
                this.inventory.get("Blé")>=1);
    }

    public void play(PlayBoard p) {
        System.out.println("Au tour de "+this.name+" :");
        int dice = this.throwDices();
        System.out.println("Résultat du lancer des dés : " + dice);
        if (dice!=7) earnResources(dice, p);
        else System.out.println("CHEH T'AS RIEN ! :)");
        this.proposeToUseSpecialCard();
        this.proposeToConstructColony(p);
        this.proposeToConstructCity(p);
        this.proposeToConstructRoad(p);
        this.proposeToBuySpecialCard();
        p.display();
        System.out.println("Votre inventaire : "+this.inventory);
        if (!this.specialCards.isEmpty()) System.out.println("Vos cartes développement : "+this.specialCards);
    }

    private int throwDices() { 
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        return (dice1+dice2);
    }

    private static void earnResources(int dice, PlayBoard p) {
        // On récupère la ou les cases désignée(s) par les dés :
        ArrayList<Box> boxes = p.getBoxes(dice);
        for (Box b : boxes) System.out.println(b);
        System.out.println();
        // On récupère les emplacement adjacents de ces cases :
        ArrayList<Location> locations = new ArrayList<Location>();
        for (Box b : boxes) {
            Location[] loc = b.getLocations(p);
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

    // Fonctions de proposition :
    // on demande au joueur s'il veut construire une colonie/ville/route
    // ou utiliser/acheter une carte développement quand c'est possible :
    private void proposeToConstructColony(PlayBoard p) {
        if (this.canConstructColony(p)) {
            Scanner sc3 = new Scanner(System.in);
            boolean notOk = true;
            do {
                try {
                    System.out.println("Voulez-vous construire une colonie ?");
                    System.out.println("Coût : 1 bois, 1 argile, 1 laine et 1 blé");
                    System.out.println("Tapez oui ou non :");
                    String line = sc3.nextLine();
                    System.out.println();
                    if (!line.equals("oui") && !line.equals("non")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("oui")) this.buildColony(p);
                } catch (WrongInputException w) {
                    System.out.println(w);
                    notOk = true;
                } catch (Exception e) {
                    System.out.println("\nErreur de format");
                    notOk = true;
                }
            } while (notOk);
        }
    }
    private void proposeToConstructCity(PlayBoard p) {
        if (this.canConstructCity()) {
            Scanner sc4 = new Scanner(System.in);
            boolean notOk = true;
            do {
                try {
                    System.out.println("Voulez-vous construire une ville ?");
                    System.out.println("Coût : 3 roches et 2 blés");
                    System.out.println("Tapez oui ou non :");
                    String line = sc4.nextLine();
                    System.out.println();
                    if (!line.equals("oui") && !line.equals("non")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("oui")) this.buildCity(p);
                } catch (WrongInputException w) {
                    System.out.println(w);
                    notOk = true;
                } catch (Exception e) {
                    System.out.println("\nErreur de format");
                    notOk = true;
                }
            } while (notOk);
        }
    }
    private void proposeToConstructRoad(PlayBoard p) {
        if (this.canConstructRoad(p)) {
            Scanner sc2 = new Scanner(System.in);
            Boolean notOk = true;
            do {
                try {
                    System.out.println("Voulez-vous construire une route ?");
                    System.out.println("Coût : 1 bois et 1 argile");
                    System.out.println("Tapez oui ou non :");
                    String line = sc2.nextLine();
                    System.out.println();
                    if (!line.equals("oui") && !line.equals("non")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("oui")) this.buildRoad(p);
                } catch (WrongInputException w) {
                    System.out.println(w);
                    notOk = true;
                } catch (Exception e) {
                    System.out.println("\nErreur de format");
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
                // TODO
            } while (notOk);
        }
    }
    private void proposeToBuySpecialCard() {
        if (this.hasEnoughToBuyACard()) {
            Scanner sc5 = new Scanner(System.in);
            boolean notOk = true;
            do {
                try {
                    System.out.println("Voulez-vous acheter une carte développement ?");
                    System.out.println("Coût : 1 roche, 1 laine et 1 blé");
                    System.out.println("Tapez oui ou non :");
                    String line = sc5.nextLine();
                    System.out.println();
                    if (!line.equals("oui") && !line.equals("non")) throw new WrongInputException();
                    notOk = false;
                    if (line.equals("oui")) this.buySpecialCard();
                } catch (WrongInputException w) {
                    System.out.println(w);
                    notOk = true;
                } catch (Exception e) {
                    System.out.println("\nErreur de format");
                    notOk = true;
                }
            } while (notOk);
        }
    }

    private static int[] scanLocationOrPath(Scanner sc) throws Exception {
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
    void buildColony(PlayBoard p) {
        Scanner sc6 = new Scanner(System.in);
        boolean notOk;
        do {
            try {
                System.out.println("Joueur "+this.symbol+", placez votre colonie :");
                int[] indexs = scanLocationOrPath(sc6);
                int k = indexs[0]; int l = indexs[1];
                if (k-1<0 || k-1>4 || l-1<0 || l-1>4) {
                    System.out.println("Erreur : cet emplacement n'existe pas");
                    notOk = true;
                } else if (p.locations[k-1][l-1] instanceof Colony) {
                    System.out.println("Erreur : cet emplacement est occupé");
                    notOk = true;
                } else {
                    p.locations[k-1][l-1] = new Colony(p.locations[k-1][l-1].boxes, k-1, l-1, this);
                    this.victoryPoints++;
                    notOk = false;
                }
            } catch (Exception e) {
                System.out.println("\nErreur de format");
                notOk = true;
            }
        } while (notOk);
        p.updatePaths();
    }

    // Construction d'une ville :
    void buildCity(PlayBoard p) {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Colony col : this.coloniesOnPlayBoard) {
            System.out.println(col);
            ids.add(col.id);
        }
        Scanner sc7 = new Scanner(System.in);
        boolean notOk = true;
        do {   
            try {
                System.out.println("Pour choisir la colonie à transformer en ville,");
                System.out.println("veuillez tapez le numéro de la colonie en question :");
                int selectedId = sc7.nextInt();
                if (!ids.contains(selectedId)) throw new WrongInputException();
                notOk = false;
                for (Colony col : this.coloniesOnPlayBoard) {
                    if (col.id==selectedId) {
                        col.isCity = true;
                        ((Colony) p.locations[col.indexI][col.indexJ]).isCity = true;
                        this.victoryPoints++;
                        break;
                    }
                }
            } catch (WrongInputException w) {
                System.out.println(w);
                notOk = true;
            } catch (Exception e) {
                System.out.println("\nErreur de format");
                notOk = true;
            }
        } while (notOk);
    }

    // Construction d'une route :
    void buildRoad(PlayBoard p) {
        Scanner sc8 = new Scanner(System.in);
        boolean notOk = true;
        char c;
        do {
            try {
                System.out.println("Joueur "+this.symbol+", voulez-vous placer une route " 
                +"horizontale ou verticale ?");
                System.out.println("Tapez h pour horizontale ou v pour verticale :");
                c = sc8.nextLine().charAt(0);
                System.out.println();
                if (c!='h' && c!='v') throw new WrongInputException();
                System.out.println("Placez votre route :");
                int[] indexs = scanLocationOrPath(sc8);
                int k = indexs[0]-1; int l = indexs[1]-1;
                if (c=='h') {
                    if (k<0 || k>4 || l<0 || l>3) throw new WrongInputException();
                } else {
                    if (k<0 || k>3 || l<0 || l>4) throw new WrongInputException();
                }
                if ((c=='h' && p.horizontalPaths[k][l] instanceof Road) || (c=='v' && p.verticalPaths[k][l] instanceof Road)) {
                    System.out.println("Erreur : cet emplacement est occupé");
                    notOk = true;
                } else {
                    try {
                        Road r = this.buildRoadNextToColony(p, c, (c=='h')? p.horizontalPaths[k][l] : p.verticalPaths[k][l]);
                        if (c=='h') p.horizontalPaths[k][l] = r;
                        else p.verticalPaths[k][l] = r;
                        this.roads.add(r);
                        notOk = false;
                    } catch (InexistantColonyException ice) {
                        System.out.println(ice);
                        notOk = true;
                        try {
                            Road r = this.buildRoadNextToRoad(p, c, (c=='h')? p.horizontalPaths[k][l] : p.verticalPaths[k][l]);
                            if (c=='h') p.horizontalPaths[k][l] = r;
                            else p.verticalPaths[k][l] = r;
                            this.roads.add(r);
                            notOk = false;
                        } catch (InexistantRoadException ire) {
                            System.out.println(ire);
                            notOk = true;
                        }
                    }
                }
            } catch (WrongInputException w) {
                System.out.println(w);
                notOk = true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("\nErreur de format");
                notOk = true;
            }
        } while (notOk);
    }
    private Road buildRoadNextToColony(PlayBoard p, char c, Path selectedPath) throws InexistantColonyException {
        if (selectedPath.point1 instanceof Colony) {
            Colony col = (Colony) selectedPath.point1;
            if (col.player==this) {
                System.out.println("ok");
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 1);
            }
        }
        if (selectedPath.point2 instanceof Colony) {
            Colony col = (Colony) selectedPath.point2;
            if (col.player==this) {
                System.out.println("ok");
                return new Road(this, c, selectedPath.point1, selectedPath.point2, 2);
            }
        }
        throw new InexistantColonyException();
    }
    private Road buildRoadNextToRoad(PlayBoard p, char c, Path selectedPath) throws InexistantRoadException {
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

    private void useSpecialCard() {
        // TODO Auto-generated method stub 
    }

    private void buySpecialCard() {
        // TODO Auto-generated method stub
    }

}