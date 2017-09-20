package game_logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jugador implements Serializable
{

	private int id;
	private String nombre;
	private List<Carta> mano;
	private Visible vis;

	public Jugador(int id, String nombre)
	{
		super();
		this.id = id;
		this.nombre = nombre;
		this.mano = new ArrayList<Carta>();
		this.vis = null;
	}

	public Jugador()
	{
		super();
		this.id = 0;
		this.nombre = null;
		this.mano = new ArrayList<Carta>();
		this.vis = null;
	}

	void showVisible(List<Jugador> oponentes, Carta carta)
	{
		System.out.println("\n\n");
		System.out.println("Mano:");
		for (Carta carta1 : this.getMano())
		{
			System.out.println(carta1.getId() + ". " + carta1.getSigno() + "\t");
		}
		System.out.println("\nNumero cartas de oponentes: ");

		for (Jugador oponente : oponentes)
		{
			System.out.println(oponente.getNombre() + "-> " + oponente.getMano().size() + "\t");
		}

		System.out.println("\nCarta actual: ");
		System.out.println(carta.getId() + ". " + carta.getSigno());

		System.out.println("\nTurno de: ");
		System.out.println(this.getId());
	}

	Carta cartaAJugar(boolean robar)
	{
		if (!robar)
		{
			return new Carta("robar", -10, null, null, null);
		}

		System.out.println("Digite Id de carta que desea jugar: ");
		Scanner keyboard = new Scanner(System.in);
		String id = keyboard.nextLine();

		Carta elegida = null;
		for (Carta carta : mano)
		{
			if (id.equalsIgnoreCase(carta.getId()))
			{
				elegida = carta;
				// System.out.println("encontre el id");
			}
		}
		return elegida;
	}

	void hacerJugada(Carta jugada)
	{
		for (Carta carta : mano)
		{
			if (jugada.getId().equalsIgnoreCase(carta.getId()))
			{
				mano.remove(carta);
				break;
			}
		}
	}

	void robar(Carta pop)
	{
		this.mano.add(pop);
		System.out.println("NO HAY JUGADAS POSIBLE, ROBA");
		System.out.println("Nueva mano despues de robar:");
		for (Carta carta : mano)
		{
			System.out.println(carta.getId() + " " + carta.getSigno());
		}
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNombre()
	{
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public List<Carta> getMano()
	{
		return mano;
	}

	public void setMano(List<Carta> mano)
	{
		this.mano = mano;
	}

	public Visible getVis()
	{
		return vis;
	}

	public void setVis(Visible vis)
	{
		this.vis = vis;
	}

}
