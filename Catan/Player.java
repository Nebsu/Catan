package Catan;

import java.util.*;
import Catan.Exceptions.*;

class Player implements PlayerAction {
    
    protected final String name;
    protected final char symbol;
    protected int victoryPoints;
    protected final Map<String, Integer> inventory;
    protected final ArrayList<Path> roads;
    protected final ArrayList<Card> specialCards;

    Player(String name, int s) {
        this.name = name;
        this.symbol = String.valueOf(s).charAt(0);
        this.victoryPoints = 0;
        this.inventory = new HashMap<String, Integer>();
        this.inventorySetup();
        this.roads = new ArrayList<Path>();
        this.specialCards = new ArrayList<Card>();
    }

    public boolean isWinner() {return (this.victoryPoints==10);}

    public void play(PlayBoard p) {
        int dice = this.throwDices();
        System.out.println("Résultat du lancer des dés : " + dice);
        if (dice!=7) this.earnResources(dice, p);
        else System.out.println("CHEH T'AS RIEN ! :)");
    }

    public int throwDices() { 
        Random rd1 = new Random(), rd2 = new Random();
        int dice1 = rd1.nextInt(6)+1;
        int dice2 = rd2.nextInt(6)+1;
        return (dice1+dice2);
    }

    public void earnResources(int dice, PlayBoard p) {
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

    public void inventorySetup() {
        this.inventory.put("Bois", 0); // bois
        this.inventory.put("Argile", 0); // argile
        this.inventory.put("Laine", 0); // laine
        this.inventory.put("Blé", 0); // blé
        this.inventory.put("Roche", 0); // minerai
    }

    public void useSpecialCard() {
        // TODO Auto-generated method stub
        
    }

    public void buildNativeColonies(PlayBoard p) {
        Scanner sc1 = new Scanner(System.in);
        boolean notOk;
        do {
            try {
                System.out.println("Joueur "+this.symbol+", placez votre colonie :");
                String s = sc1.nextLine();
                if (s.length()>2 || s.length()<=0) throw new RuntimeException();
                System.out.println();
                String i = String.valueOf(s.charAt(0));
                String j = String.valueOf(s.charAt(1));
                int k = Integer.valueOf(i);
                int l = Integer.valueOf(j);
                if (k-1<0 || k-1>4 || l-1<0 || l-1>4) {
                    System.out.println("Erreur : cet emplacement n'existe pas");
                    notOk = true;
                } else if (p.locations[k-1][l-1] instanceof Colony) {
                    System.out.println("Erreur : cet emplacement est occupé");
                    notOk = true;
                } else {
                    p.locations[k-1][l-1] = new Colony(p.locations[k-1][l-1].boxes, this);
                    this.victoryPoints++;
                    notOk = false;
                }
            } catch (Exception e) {
                System.out.println("\nErreur de format");
                notOk = true;
            }
        } while (notOk);
    }

    public void buildColony(PlayBoard p) {
        // TODO Auto-generated method stub
        
    }

    public void buildCity() {
        // TODO Auto-generated method stub
    }

    public void buildRoad(PlayBoard p) {
        Scanner sc2 = new Scanner(System.in);
        boolean notOk = true;
        char c;
        do {
            try {
                System.out.println("Joueur "+this.symbol+", voulez-vous placer une route " 
                +"horizontale ou verticale ?");
                System.out.println("Tapez h pour horizontale ou v pour verticale :");
                c = sc2.nextLine().charAt(0);
                System.out.println();
                    if (c!='h' && c!='v') throw new WrongInputException();
                    System.out.println("Placez votre route :");
                    String s = sc2.nextLine();
                    if (s.length()>2 || s.length()<=0) throw new RuntimeException();
                    System.out.println();
                    String i = String.valueOf(s.charAt(0));
                    String j = String.valueOf(s.charAt(1));
                    int k = Integer.valueOf(i);
                    int l = Integer.valueOf(j);
                    Path selectedPath;
                    if (c=='h') selectedPath = p.horizontalPaths[k-1][l-1];
                    else selectedPath = p.verticalPaths[k-1][l-1];
                    if (k-1<0 || k-1>5 || l-1<0 || l-1>4) {
                        System.out.println("Erreur : cet emplacement n'existe pas");
                        notOk = true;
                    } else if (selectedPath.player!=null) {
                        System.out.println("Erreur : cet emplacement est occupé");
                        notOk = true;
                    } else if (!(selectedPath.startPoint instanceof Colony && ((Colony) selectedPath.startPoint).player==this)) {
                        System.out.println("Erreur : Une route doit avoir comme point de départ une de vos colonies");
                        notOk = true;
                    } else {
                        int acc = 0;
                        ArrayList<Location> endPoints = this.getEndPoints();
                        for (Location endPoint : endPoints) {
                            if (selectedPath.startPoint==endPoint) {
                                selectedPath.player = this;
                                this.roads.add(selectedPath);
                                notOk = false;
                                break;
                            }
                            acc++;
                        }
                        if (acc==endPoints.size()) {
                            System.out.println("Erreur : Votre route doit être liée à une autre de vos routes");
                            notOk = true;
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

    public ArrayList<Location> getEndPoints() {
        ArrayList<Location> endPoints = new ArrayList<Location>();
        for (Path p : this.roads) {
            endPoints.add(p.endPoint);
        }
        return endPoints;
    }

    @Override
    public String toString() {
        return (this.name+" possède "+this.victoryPoints+" points de victoire.");
    }

}