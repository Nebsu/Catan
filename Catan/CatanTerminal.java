package Catan;

import java.util.*;
import Catan.Exceptions.*;

public final class CatanTerminal {

    // Joueurs :
    final static Player[] players = setPlayers();
    // Plateau :
    final static PlayBoard p = new PlayBoard();
    // Cartes développement :
    final static Deck deck = new Deck();
    // Armée la plus puissante :
    static boolean army = false;
    static Player hasTheStrongestArmy = null;
    // Joueur qui a la route la plus longue :
    static Player hasTheLongestRoad = null;

    public static final void catan() {
        p.display(); // affichage du plateau vide
        // Placement des colonies et routes de base :
        for (int k=0; k<2; k++) {
            for (int i=0; i<players.length; i++) {
                players[i].buildColony(true);
                players[i].buildRoad(true);
            }
        }
        // Affichage des joueurs au commencement du jeu :
        for (int i=0; i<players.length; i++) System.out.println(players[i]);
        // Début de la partie :
        boolean end = false;
        Player winner = null; 
        int turns = 0;
        while (!end) {
            turns++;
            for (int i=0; i<players.length; i++) {
                players[i].play();
                hasTheLongestRoad = longestRoad();
                if (players[i].isWinner()) {
                    end = true;
                    winner = players[i];
                    break;
                } 
            }
            for (int i=0; i<players.length; i++) 
                System.out.println(players[i]);
            System.out.println();
        }
        System.out.println("Félicitations "+winner.name+" ! Vous avez gagné la partie !");
        System.out.println("Nombre de tours total de la partie : "+turns);
    }

    // Créations des différents joueurs :
    private static final Player[] setPlayers() {
        // Nombre de joueurs :
        byte nbPlayers = requestNumPlayers();
        Player[] players = new Player[nbPlayers];
        // Joueur 1 (joueur principal) (forcément un humain) :
        players[0] = createRealPlayer(1);
        // Créations des autres joueurs (humains ou IAs) :
        for (int i=1; i<players.length; i++) players[i] = createPlayer(i+1);
        return players;
    }

    // Demande du nombre de joueurs :
    private static final byte requestNumPlayers() {
        byte nb = 0; // type byte pour économiser de la mémoire
        System.out.println("Choisissez le nombre de joueurs (3 ou 4) :");
        do {
            System.out.println("Tapez 3 ou 4 :");
            try (Scanner sc1 = new Scanner(System.in);) {
                nb = sc1.nextByte();
                if (nb!=3 && nb!=4) throw new WrongInputException();
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                // on met 1 pour que la boucle se répète :
                nb = 1;
            }
        } while (nb!=3 && nb!=4);
        return nb;
    }

    // Créations des joueurs :
    private static final Player createPlayer(int i) {
        System.out.println("Joueur "+i+" : Humain ou IA ?");
        char c;
        do {
            System.out.println("Tapez H ou I");
            try (Scanner sc2 = new Scanner(System.in);) {
                c = sc2.nextLine().charAt(0);
                if (c!='H' && c!='I') throw new WrongInputException();
            } catch (Exception e) {
                System.out.println(WrongInputException.message);
                // on met e pour que la boucle se répète :
                c = 'e';
            }
        } while (c!='H' && c!='I');
        Player res = null;
        // Si le joueur choisi humain, on crée un joueur humain:
        if (c=='H') res = createRealPlayer(i);
        // Sinon on crée un nouvel IA :
        if (c=='I') res = new IA("IA "+String.valueOf(i), i);
        return res;
    }  

    // Création d'un "vrai" joueur (humain) :
    private static final Player createRealPlayer(int i) {
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
                System.out.println(WrongInputException.message);
                name = null;
            }
            // Se répète s'il y a une erreur de format ou si le joueur
            // n'a rien mis (le nom doit contenir au moins une lettre) :
        } while (name==null);
        sc3.close();
        return (new Player(name, i));
    }

    private static Player longestRoad() {
        int[] sizes = new int[players.length];
        for (int i=0; i<players.length; i++) {
            sizes[i] = players[i].longestRoad();
        }
        int max = 0;
        int index = 0;
        for (int i=0; i<sizes.length; i++) {
            if (sizes[i]>max) {
                max = sizes[i];
                index = i;
            }
            if (sizes[i]==max) return null;
        }
        return players[index];
    }

}