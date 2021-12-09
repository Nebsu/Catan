package Catan;

import java.util.*;

public class PlayBoard {

    // Attributs :
    protected Box[][] boxes;
    protected Location[][] locations;
    protected Path[][] horizontalPaths;
    protected Path[][] verticalPaths;

    PlayBoard() {
        this.boxes = new Box[4][4];
        this.constructBoxes();
        this.constructPaths();
        this.constructLocations();
    }

    private final void constructBoxes() {
        // Construction des cases :
        this.boxes[0][0] = new Box("Forêt", "Bois", 6, 0, 0);
        this.boxes[0][1] = new Box("Pré", "Laine", 10, 0, 1);
        this.boxes[0][2] = new Box("Champs", "Blé", 11, 0, 2);
        this.boxes[0][3] = new Box("Pré", "Laine", 8, 0, 3);
        this.boxes[1][0] = new Box("Champs", "Blé", 4, 1,0);
        this.boxes[1][1] = new Box("Colline", "Argile", 9, 1, 1);
        this.boxes[1][2] = new Box("Forêt", "Bois", 5, 1, 2);
        this.boxes[1][3] = new Box("Montagne", "Roche", 12, 1, 3);
        this.boxes[2][0] = new Box("Montagne", "Roche", 3, 2, 0);
        this.boxes[2][1] = new Box("Désert", null, 7, 2, 1);
        this.boxes[2][2] = new Box("Champs", "Blé", 10, 2, 2);
        this.boxes[2][3] = new Box("Colline", "Argile", 6, 2, 3);
        this.boxes[3][0] = new Box("Colline", "Argile", 9, 3, 0);
        this.boxes[3][1] = new Box("Montagne", "Roche", 8, 3, 1);
        this.boxes[3][2] = new Box("Pré", "Laine", 5, 3, 2);
        this.boxes[3][3] = new Box("Forêt", "Bois", 2, 3, 3);
    }

    ArrayList<Box> getBoxes(int dice) {
        ArrayList<Box> res = new ArrayList<Box>();
        for (int i=0; i<boxes.length; i++) {
            for (int j=0; j<boxes[i].length; j++) {
                if (this.boxes[i][j].number==dice) {
                    res.add(this.boxes[i][j]);
                }
            }
        }
        return res;
    }

    private final void constructPaths() {
        this.horizontalPaths = new Path[5][4];
        for (int i=0; i<5; i++) {
            for (int j=0; j<4; j++) {
                this.horizontalPaths[i][j] = new Path('h', this.locations[i][j], this.locations[i][j+1]);
            }
        }
        this.verticalPaths = new Path[5][4];
        for (int i=0; i<5; i++) {
            for (int j=0; j<4; j++) {
                this.verticalPaths[i][j] = new Path('v', this.locations[i][j], this.locations[i][j+1]);
            }
        }
    }

    private final void constructLocations() 
    throws IllegalArgumentException {
        this.locations = new Location[5][5];
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                // Coins du plateau (1 case adjacente) :
                if (i==0 && j==0 || i==0 && j==4
                ||  i==4 && j==0 || i==4 && j==4) {
                    int k = (i==4)? 3 : i; 
                    int l = (j==4)? 3 : j;
                    Box[] b = {this.boxes[k][l]};
                    try {this.locations[i][j] = new Location(b);}
                    catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                // Arêtes horizontales du plateau (2 cases adjacentes) :
                } else if (i==0 && (j!=0 && j!=4) || i==4 && (j!=0 && j!=4)) {
                    int k = (i==4)? 3 : i;
                    Box[] b = {this.boxes[k][j-1], this.boxes[k][j]};
                    this.locations[i][j] = new Location(b);
                // Arêtes verticales du plateau (2 cases adjacentes) :
                } else if (j==0 && (i!=0 && i!=4) || j==4 && (i!=0 && i!=4)) {
                    int l = (j==4)? 3 : j;
                    Box[] b = {this.boxes[i-1][l], this.boxes[i][l]};
                    try {this.locations[i][j] = new Location(b);}
                    catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                // Centre du plateau (4 cases adjacentes) :
                } else {
                    Box[] b = {this.boxes[i-1][j-1], this.boxes[i-1][j], this.boxes[i][j-1], this.boxes[i][j]};
                    try {this.locations[i][j] = new Location(b);}
                    catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                }
            }
        }
    }

    void display() {
        System.out.println();
        System.out.println("          _________________________________________");
        System.out.print("         | ");
        for (int i=0; i<5; i++) {
            if (this.locations[0][i] instanceof Colony) {
                Colony col = (Colony) this.locations[0][i];
                if (col.isCity) System.out.print("V"+col.player.symbol);
                else System.out.print("C"+col.player.symbol);
            } else System.out.print("OO");
            if (i==4) {
                System.out.println(" |");
                break;
            }
            if (this.horizontalPaths[0][i].player!=null) {
                for (int j=1; j<=7; j++) System.out.print(this.horizontalPaths[0][i].player.symbol);
            } else System.out.print("-------");
        }
        for (int i=0; i<4; i++) {
            System.out.print("         | ");
            if (this.verticalPaths[i+1][0].player!=null) {
                for (int j=1; j<=2; j++) System.out.print(this.verticalPaths[i+1][0].player.symbol);
            } else System.out.print("||");
            for (int j=0; j<4; j++) {
                System.out.print(" "+this.boxes[i][j]);
                System.out.print(" ||");
                if (this.verticalPaths[i+1][j].player!=null) {
                    for (int k=1; k<=2; k++) System.out.print(this.verticalPaths[i+1][j].player.symbol);
                } else System.out.print("||");
            }
            System.out.println(" |");
            System.out.print("         | ");
            for (int j=0; j<5; j++) {
                if (this.locations[i+1][j] instanceof Colony) {
                    Colony col = (Colony) this.locations[i+1][j];
                    if (col.isCity) System.out.print("V"+col.player.symbol);
                    else System.out.print("C"+col.player.symbol);
                } else System.out.print("OO");
                if (j==4) {
                    System.out.println(" |");
                    break;
                }
                if (this.horizontalPaths[i+1][j].player!=null) {
                    for (int k=1; k<=7; k++) System.out.print(this.horizontalPaths[i+1][j].player.symbol);
                } else System.out.print("-------");
            }
        }
        System.out.println("         -----------------------------------------");
        System.out.println();
        System.out.println();
    }

}