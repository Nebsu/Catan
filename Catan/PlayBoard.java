package Catan;

import java.util.*;

final class PlayBoard {

    protected final Box[][] boxes;
    protected Location[][] locations;
    protected Path[][] horizontalPaths;
    protected Path[][] verticalPaths;
    protected Deck deck;

    PlayBoard() {
        this.boxes = new Box[4][4];
        this.constructBoxes();
        this.constructLocations();
        this.constructPaths();
        this.deck = new Deck();
    }
    // Construction des cases :
    private final void constructBoxes() {
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
    // Construction des intersections (nommées emplacements) :
    private final void constructLocations() throws IllegalArgumentException {
        this.locations = new Location[5][5];
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                // Coins du plateau (1 case adjacente) :
                if (i==0 && j==0 || i==0 && j==4
                ||  i==4 && j==0 || i==4 && j==4) {
                    int k = (i==4)? 3 : i; 
                    int l = (j==4)? 3 : j;
                    Box[] b = {this.boxes[k][l]};
                    try {this.locations[i][j] = new Location(b, i, j);}
                    catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                // Arêtes horizontales du plateau (2 cases adjacentes) :
                } else if (i==0 && (j!=0 && j!=4) || i==4 && (j!=0 && j!=4)) {
                    int k = (i==4)? 3 : i;
                    Box[] b = {this.boxes[k][j-1], this.boxes[k][j]};
                    this.locations[i][j] = new Location(b, i, j);
                // Arêtes verticales du plateau (2 cases adjacentes) :
                } else if (j==0 && (i!=0 && i!=4) || j==4 && (i!=0 && i!=4)) {
                    int l = (j==4)? 3 : j;
                    Box[] b = {this.boxes[i-1][l], this.boxes[i][l]};
                    try {this.locations[i][j] = new Location(b, i, j);}
                    catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                // Centre du plateau (4 cases adjacentes) :
                } else {
                    Box[] b = {this.boxes[i-1][j-1], this.boxes[i-1][j], this.boxes[i][j-1], this.boxes[i][j]};
                    try {this.locations[i][j] = new Location(b, i, j);}
                    catch (IllegalArgumentException e) {
                        System.out.println("Erreur d'argument");
                        return;
                    }
                }
            }
        }
    }
    // Construction des chemins :
    private final void constructPaths() {
        this.horizontalPaths = new Path[5][4];
        for (int i=0; i<5; i++) {
            for (int j=0; j<4; j++) {
                this.horizontalPaths[i][j] = new Path('h', this.locations[i][j], this.locations[i][j+1]);
            }
        }
        this.verticalPaths = new Path[4][5];
        for (int i=0; i<4; i++) {
            for (int j=0; j<5; j++) {
                this.verticalPaths[i][j] = new Path('v', this.locations[i][j], this.locations[i+1][j]);
            }
        }
    }

    final ArrayList<Box> getBoxes(int dice) {
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

    final void updatePaths() {
        for (int i=0; i<this.horizontalPaths.length; i++) {
            for (int j=0; j<this.horizontalPaths[i].length; j++) {
                this.horizontalPaths[i][j].point1 = this.locations[i][j];
                this.horizontalPaths[i][j].point2 = this.locations[i][j+1];
            }
        }
        for (int i=0; i<this.verticalPaths.length; i++) {
            for (int j=0; j<this.verticalPaths[i].length; j++) {
                this.verticalPaths[i][j].point1 = this.locations[i][j];
                this.verticalPaths[i][j].point2 = this.locations[i+1][j];
            }
        }
    }

    final boolean isFilledLocations() {
        int numberOfColonies = 0;
        for (int i=0; i<this.locations.length; i++) {
            for (int j=0; j<this.locations[i].length; j++) {
                if (this.locations[i][j] instanceof Colony) {
                    numberOfColonies++;
                }
            }
        }
        return (numberOfColonies==25);
    }
    final boolean isFilledPaths() {
        int numberOfRoads = 0;
        for (int i=0; i<this.horizontalPaths.length; i++) {
            for (int j=0; j<this.horizontalPaths[i].length; j++) {
                if (this.horizontalPaths[i][j] instanceof Road) numberOfRoads++;
            }
        }
        for (int i=0; i<this.verticalPaths.length; i++) {
            for (int j=0; j<this.verticalPaths[i].length; j++) {
                if (this.verticalPaths[i][j] instanceof Road) numberOfRoads++;
            }
        }
        return (numberOfRoads==40);
    }

    // Affichage du plateau :
    final void display() {
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
            if (this.horizontalPaths[0][i] instanceof Road) {
                for (int j=1; j<=7; j++) System.out.print(((Road)this.horizontalPaths[0][i]).player.symbol);
            } else System.out.print("-------");
        }
        for (int i=0; i<4; i++) {
            System.out.print("         | ");
            if (this.verticalPaths[i][0] instanceof Road) {
                for (int j=1; j<=2; j++) System.out.print(((Road)this.verticalPaths[i][0]).player.symbol);
            } else System.out.print("||");
            for (int j=0; j<4; j++) {
                System.out.print(" "+this.boxes[i][j]);
                if (this.verticalPaths[i][j+1] instanceof Road) {
                    System.out.print(" ");
                    for (int k=1; k<=2; k++) System.out.print(((Road)this.verticalPaths[i][j+1]).player.symbol);
                } else System.out.print(" ||");
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
                if (this.horizontalPaths[i+1][j] instanceof Road) {
                    for (int k=1; k<=7; k++) System.out.print(((Road)this.horizontalPaths[i+1][j]).player.symbol);
                } else System.out.print("-------");
            }
        }
        System.out.println("         -----------------------------------------");
        System.out.println();
        System.out.println();
    }

}