package CatanUI;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class CatanUI extends JFrame{
    
    public final static void catan() {
        Game frame = new Game();
        frame.setVisible(true);
        // TODO
        System.out.println("Inteface Graphique a faire");
    } 

    public static void main(String[] args) {
        catan();
    }

}