import catan.*;

import java.util.Scanner;

public class Launcher {
    
    public static void main(String[] args) {
        // Créations des différents joueurs :
        byte nbPlayers = requestNumPlayers();
        Player[] players = new Player[nbPlayers];
        players[0] = createRealPlayer(1);
        for (int i=1; i<players.length; i++) {
            players[i] = createPlayer(i+1);
        }
        for (int i=0; i<players.length; i++) {
            System.out.println(players[i]);
        }
        PlayBoard p = new PlayBoard();
        p.display();
        boolean end = false;
        Player winner = null;
        int turns = 0;
        for (int i=0; i<players.length; i++) players[i].buildColony(p);
        for (int i=0; i<players.length; i++) players[i].buildColony(p);
        for (int i=0; i<players.length; i++) {
            System.out.println(players[i]);
        }
        p.display();
        players[0].play(p);
        for (int i=0; i<players.length; i++) {
            System.out.println(players[i].inventory);
        }
        // while (!end) {
        //     turns++;
        //     for (int i=0; i<players.length; i++) {
        //         players[i].play(p);
        //         p.display();
        //         if (players[i].isWinner()) {
        //             end = true;
        //             winner = players[i];
        //             break;
        //         } 
        //     }
        // }
        // System.out.println("Félicitations "+winner.name+" ! Vous avez gagné la partie !");
        // System.out.println("Nombre de tours total de la partie : "+turns);
    }

    private static byte requestNumPlayers() {
        Scanner sc = new Scanner(System.in);
        byte nb = 0;
        do {
            System.out.println("Choisissez le nombre de joueurs (3 ou 4) :");
            nb = (byte) sc.nextInt();
            System.out.println();
        } while (nb!=3 && nb!=4);
        return nb;
    }

    private static Player createRealPlayer(int i) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Joueur "+i+", entrez votre nom :");
        String name = sc.nextLine();
        System.out.println();
        return (new Player(name, i));
    }

    private static Player createPlayer(int i) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Joueur "+i+" : Humain ou IA ?");
        char c;
        do {
            System.out.println("Tapez h ou i");
            c = sc.nextLine().charAt(0);
            System.out.println();
        } while (c!='h' && c!='i');
        Player res = null;
        if (c=='h') res = createRealPlayer(i);
        if (c=='i') res = new IA("IA "+String.valueOf(i), i);
        return res;
    }  

}