package game_logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nicolás Restrepo
 */
public class Main
{
	private static Tablero tablero;

	public static void main(String[] args)
	{

		System.out.println("Digite numero de jugadores que jugaran (entre 4 y 6)");
		Scanner keyboard = new Scanner(System.in);
		int numJugadores = keyboard.nextInt();
		while (numJugadores < 4 || numJugadores > 6)
		{
			System.out.println("Digite numero de jugadores VALIDO (entre 4 y 6)");
			keyboard = new Scanner(System.in);
			numJugadores = keyboard.nextInt();
		}
		List<Jugador> jugadores = new ArrayList<Jugador>();
		for (int i = 0; i < numJugadores; ++i)
		{
			System.out.println("Digite nombre para jugador" + (i + 1));
			keyboard = new Scanner(System.in);
			String nombre = keyboard.nextLine();
			Jugador j = new Jugador(nombre);
			jugadores.add(j);
		}

		tablero = new Tablero(jugadores);

		tablero.start();
                String g="";
		while (!tablero.existeGanador())
		{
			//tablero.gestionTurnos();
                         g= tablero.getTurno_Actual().getNombre();
			tablero.pasarTurno();
		}
		          System.out.println("-----------------------\n\n");
                          System.out.println("GANADOR!!!!!!!!!:");
                          System.out.println(g);
                          System.out.println("FELICIDADES!!!!!!");
                          System.out.println("-----------------------\n\n");
                          

	}

}
