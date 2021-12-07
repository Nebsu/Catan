import java.util.*;

public class PlayBoard {

    // Attributs :
    protected Box[][] boxes;
    protected Location[][] locations;
    protected Road[][] horizontalRoads;
    protected Road[][] verticalRoads;

    PlayBoard() {
        this.boxes = new Box[4][4];
        this.constructBoxes();
        this.constructRoads();
        this.constructLocations();
    }

    private final void constructBoxes() {
        // Construction des cases :
        this.boxes[0][0] = new Box("Forest", "Wood", 6, 0, 0);
        this.boxes[0][1] = new Box("Grassland", "Wool", 10, 0, 1);
        this.boxes[0][2] = new Box("Field", "Wheat", 11, 0, 2);
        this.boxes[0][3] = new Box("Grassland", "Wool", 8, 0, 3);
        this.boxes[1][0] = new Box("Field", "Wheat", 4, 1,0);
        this.boxes[1][1] = new Box("Hill", "Clay", 9, 1, 1);
        this.boxes[1][2] = new Box("Forest", "Wood", 5, 1, 2);
        this.boxes[1][3] = new Box("Mountain", "Rock", 12, 1, 3);
        this.boxes[2][0] = new Box("Mountain", "Rock", 3, 2, 0);
        this.boxes[2][1] = new Box("Desert", null, 7, 2, 1);
        this.boxes[2][2] = new Box("Field", "Wheat", 10, 2, 2);
        this.boxes[2][3] = new Box("Hill", "Clay", 6, 2, 3);
        this.boxes[3][0] = new Box("Hill", "Clay", 9, 3, 0);
        this.boxes[3][1] = new Box("Mountain", "Rock", 8, 3, 1);
        this.boxes[3][2] = new Box("Grassland", "Wool", 5, 3, 2);
        this.boxes[3][3] = new Box("Forest", "Wood", 2, 3, 3);
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

    private final void constructRoads() {
        this.horizontalRoads = new Road[5][4];
        for (int i=0; i<5; i++) {
            for (int j=0; j<4; j++) {
                this.horizontalRoads[i][j] = new Road('h');
            }
        }
        this.verticalRoads = new Road[5][4];
        for (int i=0; i<5; i++) {
            for (int j=0; j<4; j++) {
                this.verticalRoads[i][j] = new Road('v');
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
                        System.out.println("Argument error");
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
                        System.out.println("Argument error");
                        return;
                    }
                // Centre du plateau (4 cases adjacentes) :
                } else {
                    Box[] b = {this.boxes[i-1][j-1], this.boxes[i-1][j], this.boxes[i][j-1], this.boxes[i][j]};
                    try {this.locations[i][j] = new Location(b);}
                    catch (IllegalArgumentException e) {
                        System.out.println("Argument error");
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
            System.out.print("-------");
        }
        for (int i=0; i<4; i++) {
            System.out.print("         | ||");
            for (int j=0; j<4; j++) {
                System.out.print(" "+this.boxes[i][j]);
                System.out.print(" ||");
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
                System.out.print("-------");
            }
        }
        System.out.println("         -----------------------------------------");
        System.out.println();
        System.out.println();
    }

}