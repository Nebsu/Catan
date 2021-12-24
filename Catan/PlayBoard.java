package Catan;

import java.util.ArrayList;

final class PlayBoard {

    ////////// Attributs ////////// 

    protected final Box[][] boxes; // cases terrain
    protected Location[][] locations; // intersections
    protected final SeaBox[] seaBoxes; // cases maritimes
    protected Path[][] horizontalPaths; // chemins horizontaux
    protected Path[][] verticalPaths; // chemins verticaux

    ////////// Constructeur et fonctions associées à ce dernier //////////

    PlayBoard() {
        this.boxes = new Box[4][4];
        this.constructBoxes();
        this.constructLocations();
        this.seaBoxes = new SeaBox[16];
        this.constructSeaBoxes();
        this.constructPaths();
    }
    // Construction des cases :
    private final void constructBoxes() {
        this.boxes[0][0] = new Box("Forêt", "Bois", 6, 0, 0, false);
        this.boxes[0][1] = new Box("Pré", "Laine", 10, 0, 1, false);
        this.boxes[0][2] = new Box("Champs", "Blé", 11, 0, 2, false);
        this.boxes[0][3] = new Box("Pré", "Laine", 8, 0, 3, false);
        this.boxes[1][0] = new Box("Champs", "Blé", 4, 1,0, false);
        this.boxes[1][1] = new Box("Colline", "Argile", 9, 1, 1, false);
        this.boxes[1][2] = new Box("Forêt", "Bois", 5, 1, 2, false);
        this.boxes[1][3] = new Box("Montagne", "Roche", 12, 1, 3, false);
        this.boxes[2][0] = new Box("Montagne", "Roche", 3, 2, 0, false);
        this.boxes[2][1] = new Box("Désert", null, 7, 2, 1, true);
        this.boxes[2][2] = new Box("Champs", "Blé", 10, 2, 2, false);
        this.boxes[2][3] = new Box("Colline", "Argile", 6, 2, 3, false);
        this.boxes[3][0] = new Box("Colline", "Argile", 9, 3, 0, false);
        this.boxes[3][1] = new Box("Montagne", "Roche", 8, 3, 1, false);
        this.boxes[3][2] = new Box("Pré", "Laine", 5, 3, 2, false);
        this.boxes[3][3] = new Box("Forêt", "Bois", 2, 3, 3, false);
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
    // Construction des cases maritimes et des ports :
    private final void constructSeaBoxes() {
        this.seaBoxes[0] = new SeaBox(this.locations[0][0], this.locations[0][1]);
        this.seaBoxes[1] = new Harbor(this.locations[0][1], this.locations[0][2], "Special", "Laine");
        this.seaBoxes[2] = new SeaBox(this.locations[0][2], this.locations[0][3]);
        this.seaBoxes[3] = new Harbor(this.locations[0][3], this.locations[0][4], "Simple");
        this.seaBoxes[4] = new SeaBox(this.locations[0][4], this.locations[1][4]);
        this.seaBoxes[5] = new Harbor(this.locations[1][4], this.locations[2][4], "Special", "Bois");
        this.seaBoxes[6] = new SeaBox(this.locations[2][4], this.locations[3][4]);
        this.seaBoxes[7] = new Harbor(this.locations[3][4], this.locations[4][4], "Simple");
        this.seaBoxes[8] = new SeaBox(this.locations[4][4], this.locations[4][3]);
        this.seaBoxes[9] = new Harbor(this.locations[4][3], this.locations[4][2], "Special", "Argile");
        this.seaBoxes[10] = new SeaBox(this.locations[4][2], this.locations[4][1]);
        this.seaBoxes[11] = new Harbor(this.locations[4][1], this.locations[4][0], "Special", "Roche");
        this.seaBoxes[12] = new SeaBox(this.locations[4][0], this.locations[3][0]);
        this.seaBoxes[13] = new Harbor(this.locations[3][0], this.locations[2][0], "Special", "Blé");
        this.seaBoxes[14] = new SeaBox(this.locations[2][0], this.locations[1][0]);
        this.seaBoxes[15] = new Harbor(this.locations[1][0], this.locations[0][0], "Simple");
    }
    // Construction des chemins :
    private final void constructPaths() {
        this.horizontalPaths = new Path[5][4];
        for (int i=0; i<5; i++) {
            for (int j=0; j<4; j++) {
                this.horizontalPaths[i][j] = new Path('H', this.locations[i][j], this.locations[i][j+1]);
            }
        }
        this.verticalPaths = new Path[4][5];
        for (int i=0; i<4; i++) {
            for (int j=0; j<5; j++) {
                this.verticalPaths[i][j] = new Path('V', this.locations[i][j], this.locations[i+1][j]);
            }
        }
    }


    ////////// Fonctions auxiliaires //////////

    // Récupération des cases désignées par les dés :
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

    // Mise à jour des chemins après la constructions de colonies/villes :
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

    ////////// Fonctions booléennes //////////

    // Renvoie true si toutes les intersections sont occupées par une colonie/ville :
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
    
    // Renvoie true si tous les chemins ont été pris :
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


    ////////// Affichage du plateau //////////
    final void display() {
        int seaAcc1 = 4;
        int seaAcc2 = 15;
        System.out.println();
        System.out.print("                  ");
        for (int i=0; i<4; i++) 
            System.out.print("    "+this.seaBoxes[i]+"  ");
        System.out.println();
        System.out.print("                  ");
        for (int i=0; i<5; i++) {
            if (this.locations[0][i] instanceof Colony) {
                Colony col = (Colony) this.locations[0][i];
                if (col.isCity) System.out.print(col.player.color+"V"+col.player.symbol+CatanTerminal.RESET);
                else System.out.print(col.player.color+"C"+col.player.symbol+CatanTerminal.RESET);
            } else System.out.print("OO");
            if (i==4) {
                System.out.println();
                break;
            }
            if (this.horizontalPaths[0][i] instanceof Road) {
                for (int j=1; j<=7; j++) 
                    System.out.print(((Road) this.horizontalPaths[0][i]).player.color+
                    ((Road) this.horizontalPaths[0][i]).player.symbol+CatanTerminal.RESET);
            } else System.out.print("-------");
        }
        for (int i=0; i<4; i++) {
            System.out.print("             ");
            System.out.print(this.seaBoxes[seaAcc2]+"  ");seaAcc2--;
            if (this.verticalPaths[i][0] instanceof Road) {
                for (int j=1; j<=2; j++) 
                    System.out.print(((Road) this.verticalPaths[i][0]).player.color+
                    ((Road) this.verticalPaths[i][0]).player.symbol+CatanTerminal.RESET);
            } else System.out.print("||");
            for (int j=0; j<4; j++) {
                System.out.print(" "+this.boxes[i][j]);
                if (this.verticalPaths[i][j+1] instanceof Road) {
                    System.out.print(" ");
                    for (int k=1; k<=2; k++) 
                        System.out.print(((Road) this.verticalPaths[i][j+1]).player.color+
                        ((Road) this.verticalPaths[i][j+1]).player.symbol+CatanTerminal.RESET);
                } else System.out.print(" ||");
            }
            System.out.println("  "+this.seaBoxes[seaAcc1]); seaAcc1++;
            System.out.print("                  ");
            for (int j=0; j<5; j++) {
                if (this.locations[i+1][j] instanceof Colony) {
                    Colony col = (Colony) this.locations[i+1][j];
                    if (col.isCity) System.out.print(col.player.color+"V"+col.player.symbol+CatanTerminal.RESET);
                    else System.out.print(col.player.color+"C"+col.player.symbol+CatanTerminal.RESET);
                } else System.out.print("OO");
                if (j==4) {
                    System.out.println();
                    break;
                }
                if (this.horizontalPaths[i+1][j] instanceof Road) {
                    for (int k=1; k<=7; k++) 
                        System.out.print(((Road) this.horizontalPaths[i+1][j]).player.color+
                        ((Road) this.horizontalPaths[i+1][j]).player.symbol+CatanTerminal.RESET);
                } else System.out.print("-------");
            }
        }
        System.out.print("                  ");
        for (int i=11; i>=8; i--) 
            System.out.print("    "+this.seaBoxes[i]+"  ");
        System.out.println();
        System.out.println("\n");
    }

}