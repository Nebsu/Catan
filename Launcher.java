import java.util.Scanner;
import Catan.CatanTerminal;
import Catan.CatanUI;
import Catan.Exceptions.WrongInputException;

public final class Launcher {
    
    public static final void main(String[] args) {
        boolean UI = chooseCatan();
        if (UI) CatanUI.catan();
        else CatanTerminal.catan();
    }

    private static final boolean chooseCatan() {
        System.out.println("A quelle version du catan voulez-vous jouer ?");
        System.out.println("Tapez 1 pour la version texte sur le terminal");
        System.out.println("Tapez 2 pour la version avec interface graphique");
        do {
            System.out.println("Votre choix :");
            Scanner sc = new Scanner(System.in);
            try {
                int choice = sc.nextInt();
                if (choice!=1 && choice!=2) throw new WrongInputException();
                return (choice==2);
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
            }
        } while (true);
    }

}