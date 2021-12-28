package Catan.CatanUI;

import javax.swing.JFrame;

public final class CatanUI extends JFrame{
    
    public final static void catan() {
        Game frame = new Game();
        frame.setVisible(true);
    } 

    public static void main(String[] args) {
        catan();
    }

}