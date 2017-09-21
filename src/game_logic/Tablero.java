package game_logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author NicolÃ¡s Restrepo
 */
public class Tablero implements Serializable
{

	private Stack<Carta> mazo;
	private List<Jugador> jugadores;
	private Jugador turno_Actual;
	private Jugador ganador;
	private Stack<Carta> jugadas;

	public Tablero(List<Jugador> jugadores)
	{
		this.jugadores = jugadores;
		this.ganador = null;
		this.turno_Actual = null;
		this.mazo = cargarMazo();
	}

	private Stack<Carta> cargarMazo()
	{

		String c1 = "Amarillo";
		String c2 = "Verde";
		String c3 = "Rojo";
		String c4 = "Azul";

		String e1 = "TOMA DOS";
		String e2 = "CAMBIO DE SENTIDO";
		String e3 = "PIERDE TURNO";
		String e4 = "CAMBIO DE COLOR";
		String e5 = "TOMA CUATRO";

		Stack<Carta> mazo = new Stack<>();
		Carta c;

		int k = 1;

		for (int i = 0; i < 10; i++)
		{
			mazo.add(new Carta("" + k++, i, c1, "no especial", (i + " " + c1)));
			mazo.add(new Carta("" + k++, i, c2, "no especial", (i + " " + c2)));
			mazo.add(new Carta("" + k++, i, c3, "no especial", (i + " " + c3)));
			mazo.add(new Carta("" + k++, i, c4, "no especial", (i + " " + c4)));
		}

		for (int i = 0; i < 2; i++)
		{
			mazo.add(new Carta("" + k++, -1, c1, e1, ("+2 " + c1)));
			mazo.add(new Carta("" + k++, -1, c2, e1, ("+2 " + c2)));
			mazo.add(new Carta("" + k++, -1, c3, e1, ("+2 " + c3)));
			mazo.add(new Carta("" + k++, -1, c4, e1, ("+2 " + c4)));

			mazo.add(new Carta("" + k++, -1, c1, e2, ("CS " + c1)));
			mazo.add(new Carta("" + k++, -1, c2, e2, ("CS " + c2)));
			mazo.add(new Carta("" + k++, -1, c3, e2, ("CS " + c3)));
			mazo.add(new Carta("" + k++, -1, c4, e2, ("CS " + c4)));

			mazo.add(new Carta("" + k++, -1, c1, e3, ("ST " + c1)));
			mazo.add(new Carta("" + k++, -1, c2, e3, ("ST " + c2)));
			mazo.add(new Carta("" + k++, -1, c3, e3, ("ST " + c3)));
			mazo.add(new Carta("" + k++, -1, c4, e3, ("ST " + c4)));

		}

		c = new Carta("65", -1, "", e4, "CC"); // cambio de color
		mazo.add(c);
		c = new Carta("66", -1, "", e4, "CC"); // cambio de color
		mazo.add(c);
		c = new Carta("67", -1, "", e4, "CC"); // cambio de color
		mazo.add(c);
		c = new Carta("68", -1, "", e4, "CC"); // cambio de color
		mazo.add(c);

		c = new Carta("69", -1, "", e5, "+4"); // toma cuatro
		mazo.add(c);
		c = new Carta("70", -1, "", e5, "+4"); // toma cuatro
		mazo.add(c);
		c = new Carta("71", -1, "", e5, "+4"); // toma cuatro
		mazo.add(c);
		c = new Carta("71", -1, "", e5, "+4"); // toma cuatro
		mazo.add(c);
		
		

		return mazo;

	}

	void start()
	{

		Collections.shuffle(this.jugadores);
		Collections.shuffle(this.mazo);
		List<Carta> mano = null;
		for (Jugador j : this.jugadores)
		{
			mano = new ArrayList<>();

			for (int i = 0; i < 7; i++)
			{
				refillMazo();
				Carta repartir = this.mazo.pop();
				mano.add(repartir);
			}
			j.setMano(mano);

		}
		this.turno_Actual = jugadores.get(0);
		this.jugadas = new Stack<>();

		// System.out.println(this.mazo.peek().getEspecial());
		// System.out.println(this.mazo.peek().getSigno());

		while (!this.mazo.peek().getEspecial().equalsIgnoreCase("no especial"))
		{

			// System.out.println("entre");
			Collections.shuffle(this.mazo); // asegurarse que la primera carta
											// no sea especial;

		}
		refillMazo();
		this.jugadas.push(this.mazo.pop());

		// System.out.println(this.jugadas.peek().getEspecial());
		// System.out.println(this.jugadas.peek().getSigno());

	}

	void gestionTurnos()
	{
		boolean robar = comprobarRobar();

		List<Jugador> oponentes = new ArrayList<>();

		for (Jugador jugador : jugadores)
		{
			if (jugador.getId() != turno_Actual.getId())
			{
				oponentes.add(jugador);
			}
		}

		this.turno_Actual.showVisible(oponentes, (this.jugadas.peek()));

		Carta jugada = this.turno_Actual.cartaAJugar(robar);
		boolean jugable = true;

		while (jugada == null)
		{
			System.out.println("Carta inexistente");
			jugada = this.turno_Actual.cartaAJugar(robar);
		}

		if (jugada.getId().equalsIgnoreCase("robar"))
		{

			System.out.println(this.mazo.size());

			refillMazo();
			this.turno_Actual.robar(this.mazo.pop());
		}
		else
		{

			while (!checkLogic(jugada))
			{
				System.out.println("Carta ilogica");
				jugada = this.turno_Actual.cartaAJugar(robar);
				while (jugada == null)
				{
					System.out.println("Carta inexistente");
					jugada = this.turno_Actual.cartaAJugar(robar);

				}
				if (jugada.getId().equalsIgnoreCase("robar"))
				{
					refillMazo();
					this.turno_Actual.robar(this.mazo.pop());
					break;
				}
			}
		}

		boolean jugadaEspecial = true;
		Carta especial = null;
		if (!jugada.getId().equalsIgnoreCase("robar"))
		{
			especial = jugada;
			this.turno_Actual.hacerJugada(jugada);
			this.jugadas.push(jugada);
			System.out.println("Carta colocada en la mesa: " + this.jugadas.peek().getSigno());
			if (jugada.getEspecial().equalsIgnoreCase("no especial")) jugadaEspecial = false;

			if (jugadaEspecial)
			{
				jugadaCartaEspecial(especial);
			}

		}

	}

	private boolean checkLogic(Carta jugada)
	{
		Carta actual = this.getJugadas().peek();
		int tipo = 0; // 1 si es numero, 2 si es especial con color, 3 si es
						// especial sin color
		if (jugada.getEspecial().equalsIgnoreCase("no especial")) tipo = 1;
		if (jugada.getEspecial().equalsIgnoreCase("TOMA DOS") || jugada.getEspecial().equalsIgnoreCase("CAMBIO DE SENTIDO") || jugada.getEspecial().equalsIgnoreCase("PIERDE TURNO")) tipo = 2;

		if (jugada.getEspecial().equalsIgnoreCase("CAMBIO DE COLOR") || jugada.getEspecial().equalsIgnoreCase("TOMA CUATRO")) tipo = 3;

		if (tipo == 1) // si la jugada es un numero
		{
			if (actual.getEspecial().equalsIgnoreCase("no especial")) // si la
																		// actual
																		// es un
																		// numero
			{
				if (actual.getColor().equalsIgnoreCase(jugada.getColor()))// si
																			// el
																			// color
																			// es
																			// el
																			// mismo
					return true;
				if (actual.getNumero() == jugada.getNumero())// si es el mismo
																// numero
					return true;

			}
			else
			{// si es especial

				if (actual.getColor().equalsIgnoreCase(jugada.getColor()))// si
																			// el
																			// color
																			// es
																			// el
																			// mismo
					return true;
			}

		}

		if (tipo == 2) // si la jugada es una especial con color
		{
			if (actual.getColor().equalsIgnoreCase(jugada.getColor())) // si el
																		// color
																		// es el
																		// mismo
				return true;
			if (actual.getEspecial().equalsIgnoreCase(jugada.getEspecial())) return true;

		}

		if (tipo == 3) // si la jugada es una especial sin color
			return true;

		return false;

	}

	boolean existeGanador()
	{
		for (Jugador jugadore : jugadores)
		{
			if (jugadore.getMano().size() == 0) return true;
		}
		return false;
	}

	void pasarTurno()
	{

		Jugador j = this.jugadores.get(0);
		jugadores.add(j);
		jugadores.remove(j);
		this.setTurno_Actual(this.jugadores.get(0));

		// System.out.println("Imprimire turnos");
		for (Jugador jugadore : this.jugadores)
		{
			// System.out.println(jugadore.getId());
		}
		// System.out.println("Turno de"+j.getId());
	}

	private void jugadaCartaEspecial(Carta especial)
	{

		System.out.println(especial.getSigno());
		if (especial.getEspecial().equalsIgnoreCase("TOMA DOS"))
		{

			System.out.println("Se le agregaran 2 a " + this.jugadores.get(1).getId());
			List<Carta> mano = this.jugadores.get(1).getMano();
			refillMazo();
			mano.add(this.mazo.pop());
			refillMazo();
			mano.add(this.mazo.pop());

			this.jugadores.get(1).setMano(mano);

		}

		if (especial.getEspecial().equalsIgnoreCase("CAMBIO DE SENTIDO"))
		{
			for (Jugador jugadore : this.jugadores)
			{
				System.out.println(jugadore.getId());
			}
			Collections.reverse(this.jugadores);
			for (int i = 0; i < this.jugadores.size() - 1; i++)
			{
				pasarTurno();
			}
			for (Jugador jugadore : this.jugadores)
			{
				System.out.println(jugadore.getId());
			}

		}

		if (especial.getEspecial().equalsIgnoreCase("PIERDE TURNO"))
		{
			System.out.println("Jugador que pierde turno: " + jugadores.get(1).getId());
			pasarTurno();
		}
		if (especial.getEspecial().equalsIgnoreCase("CAMBIO DE COLOR"))
		{
			boolean sel = false;

			while (!sel)
			{
				System.out.println("Eliga color");
				System.out.println("Digite A para amarillo, V para verde, R para rojo, AZ para azul: ");
				Scanner key = new Scanner(System.in);
				String col = key.nextLine();

				if (col.equalsIgnoreCase("a"))
				{
					this.jugadas.peek().setColor("Amarillo");
					sel = true;

				}
				if (col.equalsIgnoreCase("v"))
				{
					this.jugadas.peek().setColor("Verde");
					sel = true;
				}
				if (col.equalsIgnoreCase("r"))
				{
					this.jugadas.peek().setColor("Rojo");
					sel = true;
				}
				if (col.equalsIgnoreCase("az"))
				{
					this.jugadas.peek().setColor("Azul");
					sel = true;
				}

			}
			especial.setSigno(especial.getSigno() + " " + especial.getColor());
			System.out.println("Carta cambiada: " + especial.getSigno());
		}

		if (especial.getEspecial().equalsIgnoreCase("TOMA CUATRO"))

		{
			System.out.println("Se le agregaran 4 a " + this.jugadores.get(1).getId());
			List<Carta> mano = this.jugadores.get(1).getMano();
			refillMazo();
			mano.add(this.mazo.pop());
			refillMazo();
			mano.add(this.mazo.pop());
			refillMazo();
			mano.add(this.mazo.pop());
			refillMazo();
			mano.add(this.mazo.pop());

			this.jugadores.get(1).setMano(mano);

			boolean sel = false;

			while (!sel)
			{
				System.out.println("Eliga color");
				System.out.println("Digite A para amarillo, V para verde, R para rojo, AZ para azul: ");
				Scanner key = new Scanner(System.in);
				String col = key.nextLine();

				if (col.equalsIgnoreCase("a"))
				{
					this.jugadas.peek().setColor("Amarillo");
					sel = true;

				}
				if (col.equalsIgnoreCase("v"))
				{
					this.jugadas.peek().setColor("Verde");
					sel = true;
				}
				if (col.equalsIgnoreCase("r"))
				{
					this.jugadas.peek().setColor("Rojo");
					sel = true;
				}
				if (col.equalsIgnoreCase("az"))
				{
					this.jugadas.peek().setColor("Azul");
					sel = true;
				}

			}
			especial.setSigno(especial.getSigno() + " " + especial.getColor());
			System.out.println("Carta cambiada: " + especial.getSigno());

		}

	}

	public void refillMazo()
	{

		if (this.mazo.size() == 1)
		{ // si queda 1

			System.out.println("Se esta llenando el mazo con las cartas utilizadas");
			Carta actual = this.jugadas.pop(); // guardo la carta actual

			for (Carta jugada : this.jugadas)
			{

				if (jugada.getEspecial().equalsIgnoreCase("CAMBIO DE COLOR"))
				{
					jugada.setSigno("Â©");
					jugada.setColor("");
				}

				if (jugada.getEspecial().equalsIgnoreCase("TOMA CUATRO")) jugada.setSigno("+4");
				jugada.setColor("");

			}

			this.setMazo(this.getJugadas());
			this.setJugadas(new Stack<Carta>());

			Collections.shuffle(this.mazo);
			this.jugadas.push(actual);

			System.out.println("Carta actual sigue siendo: " + this.jugadas.peek().getSigno());
			System.out.println("tam jugdas: " + this.jugadas.size());
			System.out.println(" tam mazo: " + this.mazo.size());
		}

	}

	private boolean comprobarRobar()
	{ // si puede hacer una jugada no se le permite robar
		boolean ret;
		for (Carta c : this.turno_Actual.getMano())
		{
			ret = checkLogic(c);
			if (ret) return true;
		}
		return false;
	}

	public void existeGanadorImp()
	{
		for (Jugador jugadore : jugadores)
		{

			if (jugadore.getMano().size() == 0) System.out.println("FELICIDADES " + jugadore.getId() + " GANO!");
			break;
		}

	}

	public Stack<Carta> getMazo()
	{
		return mazo;
	}

	public void setMazo(Stack<Carta> mazo)
	{
		this.mazo = mazo;
	}

	public List<Jugador> getJugadores()
	{
		return jugadores;
	}

	public void setJugadores(List<Jugador> jugadores)
	{
		this.jugadores = jugadores;
	}

	public Jugador getTurno_Actual()
	{
		return turno_Actual;
	}

	public void setTurno_Actual(Jugador turno_Actual)
	{
		this.turno_Actual = turno_Actual;
	}
	
	public Jugador getPlayer(String name)
	{
		for (Jugador j : jugadores)
		{
			if (name.equals(j.getNombre()))
			{
				return j;
			}
		}
		return null;
	}

	public Jugador getGanador()
	{
		return ganador;
	}

	public void setGanador(Jugador ganador)
	{
		this.ganador = ganador;
	}

	public Stack<Carta> getJugadas()
	{
		return jugadas;
	}

	public void setJugadas(Stack<Carta> jugadas)
	{
		this.jugadas = jugadas;
	}

}
