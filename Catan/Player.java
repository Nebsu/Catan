package Joueur;

import java.util.*;

class Player implements PlayerAction {
    
    protected final ArrayList<Card> specialCards;
    protected final Map<String, Integer> inventory;
    protected int victoryPoints;
    protected final String name;
    protected final char symbol;

    Player(String name, int s) {
        this.specialCards = new ArrayList<Card>();
        this.inventory = new HashMap<String, Integer>();
        this.inventorySetup();
        this.victoryPoints = 0;
        this.name = name;
        this.symbol = String.valueOf(s).charAt(0);
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
        if (colonies.isEmpty()) {
            System.out.println("Pas de chance, personne n'a rien gagné");
            return;
        }
        for (Colony c : colonies) System.out.println(c);
        for (int i=0; i<boxes.size(); i++) {
            for (Colony c : colonies) {
                c.player.inventory.put(boxes.get(i).ressource, Integer.valueOf(1));
            }
        }
    }

    public void inventorySetup() {
        this.inventory.put("Wood", 0); // bois
        this.inventory.put("Clay", 0); // argile
        this.inventory.put("Wool", 0); // laine
        this.inventory.put("Wheat", 0); // blé
        this.inventory.put("Rock", 0); // minerai
    }

    public void useSpecialCard() {
        // TODO Auto-generated method stub
        
    }

    public void buildRoad() {
        // TODO Auto-generated method stub
        
    }

    public void buildColony(PlayBoard p) {
        Scanner sc = new Scanner(System.in);
        boolean notOk;
        do {
            System.out.println("Joueur "+this.symbol+", placez votre colonie :");
            String s = sc.nextLine();
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
        } while (notOk);
    }

    public void buildCity() {
        // TODO Auto-generated method stub
    }

    @Override
    public String toString() {
        return (this.name+" has "+this.victoryPoints+" victory points.");
    }

}