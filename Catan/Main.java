package Catan;

import java.util.Scanner;
import Catan.Exceptions.*;

public class Main {

    public final static void catanTerminal() {
        // Créations des différents joueurs :
        Player[] players = setPlayers();
        // Création du plateau :
        PlayBoard p = new PlayBoard();
        p.display(); // affichage du plateau vide
        // Placement des colonies de base :
        for (int i=0; i<players.length; i++) {
            players[i].buildNativeColonies(p);
            players[i].buildRoad(p);
        }
        for (int i=0; i<players.length; i++) {
            players[i].buildNativeColonies(p);
            players[i].buildRoad(p);
        }
        for (int i=0; i<players.length; i++) {
            System.out.println(players[i]);
        }
        p.display();
        boolean end = false;
        Player winner = null; 
        int turns = 0;
        while (turns==3) {
            turns++;
            for (int i=0; i<players.length; i++) {
                players[i].play(p);
                p.display();
                for (int j=0; i<players.length; j++) {
                    System.out.println(players[i].name+" : "+players[i].inventory);
                }
                if (players[i].isWinner()) {
                    end = true;
                    winner = players[i];
                    break;
                } 
            }
        }
        System.out.println("Félicitations "+winner.name+" ! Vous avez gagné la partie !");
        System.out.println("Nombre de tours total de la partie : "+turns);
    }

    // Créations des différents joueurs :
    private static Player[] setPlayers() {
        // Nombre de joueurs :
        byte nbPlayers = requestNumPlayers();
        Player[] players = new Player[nbPlayers];
        // Joueur 1 (joueur principal) (forcément un humain) :
        players[0] = createRealPlayer(1);
        // Créations des autres joueurs (humains ou IAs) :
        for (int i=1; i<players.length; i++) {
            players[i] = createPlayer(i+1);
        }
        // Affichage des joueurs définis pour la partie courante :
        for (int i=0; i<players.length; i++) {
            System.out.println(players[i]);
        }
        return players;
    }

    // Demande du nombre de joueurs :
    private static byte requestNumPlayers() {
        byte nb = 0; // type byte pour économiser de la mémoire
        do {
            Scanner sc1 = new Scanner(System.in);
            System.out.println("Choisissez le nombre de joueurs (3 ou 4) :");
            try {
                nb = (byte) sc1.nextInt();
                if (nb!=3 && nb!=4) throw new WrongInputException();
            } catch (WrongInputException w) {
                System.out.println(w);
            } catch (Exception e) {
                System.out.println("\nErreur de format");
                // on met 1 pour que la boucle se répète :
                nb = 1;
            }
            System.out.println();
        } while (nb!=3 && nb!=4);
        return nb;
    }

    // Créations des joueurs :
    private static Player createPlayer(int i) {
        System.out.println("Joueur "+i+" : Humain ou IA ?");
        char c;
        do {
            Scanner sc2 = new Scanner(System.in);
            System.out.println("Tapez h ou i");
            try {
                c = sc2.nextLine().charAt(0);
                if (c!='h' && c!='i') throw new WrongInputException();
            } catch (WrongInputException w) {
                System.out.println(w);
                // on met e pour que la boucle se répète :
                c = 'e';
            } catch (Exception e) {
                System.out.println("\nErreur de format");
                // on met e pour que la boucle se répète :
                c = 'e';
            }
            System.out.println();
        } while (c!='h' && c!='i');
        Player res = null;
        // Si le joueur choisi humain, on crée un joueur humain:
        if (c=='h') res = createRealPlayer(i);
        // Sinon on crée un nouvel IA :
        if (c=='i') res = new IA("IA "+String.valueOf(i), i);
        return res;
    }  

    // Création d'un "vrai" joueur (humain) :
    private static Player createRealPlayer(int i) {
        // On définit cette fonction, car chaque joueur humain doit entrer
        // son nom. Pour les IA, on les appelle automatiquement par exemple : 
        // IA2 , si l'IA est le joueur 2
        Scanner sc3 = new Scanner(System.in);
        String name = null;
        do {
            System.out.println("Joueur "+i+", entrez votre nom :");
            try {
                name = sc3.nextLine();
                if (name.length()==0) throw new InvalidNameException();
            } catch (InvalidNameException exp) {
                System.out.println(exp);
                name = null;
            } catch (Exception e) {
                System.out.println("\nErreur de format");
                name = null;
            }
            // Se répète s'il y a une erreur de format ou si le joueur
            // n'a rien mis (le nom doit contenir au moins une lettre) :
        } while (name==null);
        System.out.println();
        return (new Player(name, i));
    }

}