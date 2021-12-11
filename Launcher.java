import java.util.Scanner;
import Catan.CatanTerminal;
import Catan.Tests;
import Catan.CatanUI;
import Catan.Exceptions.WrongInputException;

public class Launcher {
    
    public final static void main(String[] args) {
        boolean UI = chooseCatan();
        if (UI) CatanUI.catan();
        else {
            // CatanTerminal.catan();
            Tests.catanTerminal();
        }
    }

    private static boolean chooseCatan() {
        Scanner sc = new Scanner(System.in);
        System.out.println("A quelle version du catan voulez-vous jouer ?");
        System.out.println("Tapez 1 pour la version texte sur le terminal");
        System.out.println("Tapez 2 pour la version avec interface graphique");
        System.out.println("Votre choix :");
        do {
            try {
                int choice = sc.nextInt();
                if (choice!=1 && choice!=2) throw new WrongInputException();
                return (choice==2);
            } catch (WrongInputException w) {
                System.out.println(w);
            } catch (Exception e) {
                System.out.println("\nErreur de format");
            }
        } while (true);
    }

}