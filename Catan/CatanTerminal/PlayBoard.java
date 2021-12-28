package Catan.CatanTerminal;

import java.util.ArrayList;

final class PlayBoard {

    ////////// Attributs ////////// 

    final Box[][] boxes; // cases terrain
    Location[][] locations; // intersections
    final SeaBox[] seaBoxes; // cases maritimes
    Path[][] horizontalPaths; // chemins horizontaux
    Path[][] verticalPaths; // chemins verticaux
    Box thief; // case contenant le voleur 

    ////////// Constructeur et fonctions associées à ce dernier //////////

    PlayBoard() {
        this.boxes = new Box[4][4];
        this.constructBoxes();
        this.constructLocations();
        this.seaBoxes = new SeaBox[16];
        this.constructSeaBoxes();
        this.constructPaths();
        this.thief = this.boxes[2][1];
    }
    // Construction des cases :
    private final void constructBoxes() {
        this.boxes[0][0] = new Box("Foret", "Bois", 6, 0, 0, false);
        this.boxes[0][1] = new Box("Pre", "Laine", 10, 0, 1, false);
        this.boxes[0][2] = new Box("Champs", "Ble", 11, 0, 2, false);
        this.boxes[0][3] = new Box("Pre", "Laine", 8, 0, 3, false);
        this.boxes[1][0] = new Box("Champs", "Ble", 4, 1,0, false);
        this.boxes[1][1] = new Box("Colline", "Argile", 9, 1, 1, false);
        this.boxes[1][2] = new Box("Foret", "Bois", 5, 1, 2, false);
        this.boxes[1][3] = new Box("Montagne", "Roche", 12, 1, 3, false);
        this.boxes[2][0] = new Box("Montagne", "Roche", 3, 2, 0, false);
        this.boxes[2][1] = new Box("Desert", null, 7, 2, 1, true);
        this.boxes[2][2] = new Box("Champs", "Ble", 10, 2, 2, false);
        this.boxes[2][3] = new Box("Colline", "Argile", 6, 2, 3, false);
        this.boxes[3][0] = new Box("Colline", "Argile", 9, 3, 0, false);
        this.boxes[3][1] = new Box("Montagne", "Roche", 8, 3, 1, false);
        this.boxes[3][2] = new Box("Pre", "Laine", 5, 3, 2, false);
        this.boxes[3][3] = new Box("Foret", "Bois", 2, 3, 3, false);
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
        this.seaBoxes[1] = new Harbor(this.locations[0][1], this.locations[0][2], 2, "Laine");
        this.seaBoxes[2] = new SeaBox(this.locations[0][2], this.locations[0][3]);
        this.seaBoxes[3] = new Harbor(this.locations[0][3], this.locations[0][4]);
        this.seaBoxes[4] = new SeaBox(this.locations[0][4], this.locations[1][4]);
        this.seaBoxes[5] = new Harbor(this.locations[1][4], this.locations[2][4], 2, "Bois");
        this.seaBoxes[6] = new SeaBox(this.locations[2][4], this.locations[3][4]);
        this.seaBoxes[7] = new Harbor(this.locations[3][4], this.locations[4][4]);
        this.seaBoxes[8] = new SeaBox(this.locations[4][4], this.locations[4][3]);
        this.seaBoxes[9] = new Harbor(this.locations[4][3], this.locations[4][2], 2, "Argile");
        this.seaBoxes[10] = new SeaBox(this.locations[4][2], this.locations[4][1]);
        this.seaBoxes[11] = new Harbor(this.locations[4][1], this.locations[4][0], 2, "Roche");
        this.seaBoxes[12] = new SeaBox(this.locations[4][0], this.locations[3][0]);
        this.seaBoxes[13] = new Harbor(this.locations[3][0], this.locations[2][0], 2, "Ble");
        this.seaBoxes[14] = new SeaBox(this.locations[2][0], this.locations[1][0]);
        this.seaBoxes[15] = new Harbor(this.locations[1][0], this.locations[0][0]);
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

    // Affichage du plateau :
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
            System.out.print(this.locations[0][i]);
            if (i==4) {
                System.out.println();
                break;
            }
            System.out.print(this.horizontalPaths[0][i]);
        }
        for (int i=0; i<4; i++) {
            System.out.print("             ");
            System.out.print(this.seaBoxes[seaAcc2]+"  "); seaAcc2--;
            System.out.print(this.verticalPaths[i][0]);
            for (int j=0; j<4; j++) {
                System.out.print(" "+this.boxes[i][j]);
                System.out.print(" "+this.verticalPaths[i][j+1]);
            }
            System.out.println("  "+this.seaBoxes[seaAcc1]); seaAcc1++;
            System.out.print("                  ");
            for (int j=0; j<5; j++) {
                System.out.print(this.locations[i+1][j]);
                if (j==4) {
                    System.out.println();
                    break;
                }
                System.out.print(this.horizontalPaths[i+1][j]);
            }
        }
        System.out.print("                  ");
        for (int i=11; i>=8; i--) 
            System.out.print("    "+this.seaBoxes[i]+"  ");
        System.out.println();
        System.out.println("\n");
    }

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

    // Récupération du port qui possède l'id passé en argument :
    final Harbor getHarbor(int id) {
        for (int i=0; i<this.seaBoxes.length; i++) {
            if (this.seaBoxes[i] instanceof Harbor) {
                if (((Harbor) this.seaBoxes[i]).id==id) return ((Harbor) this.seaBoxes[i]);
            }
        }
        return null;
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

}