package game_logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Jugador implements Serializable
{

	private String nombre;
	private List<Carta> mano;
	
	public Jugador(String nombre)
	{
		super();
		this.nombre = nombre;
		this.mano = new ArrayList<Carta>();
		
	}

	public Jugador()
	{
		super();
		this.nombre = null;
		this.mano = new ArrayList<Carta>();
		
	}

	public String showVisible(HashMap <String,Integer> cartasOponentes, Carta carta)
	{
		String ret="";
                
                ret+=("\n\n");
		
            ret+=("\nNumero cartas de oponentes: ");

            // imprmir numero cartas oponente
            for (HashMap.Entry<String, Integer> entry : cartasOponentes.entrySet()) {
                ret+=("\nJugador: " + entry.getKey() + " tiene "+ entry.getValue()+ " cartas. ");
            }

            ret+=("\n Ultima carta jugada: ");
            ret+=("\n"+carta.getSigno());

		ret+=("\nTurno de: ");
		ret+=("\n"+this.getNombre());
                return ret;
	}



	public void hacerJugada(Carta jugada)
	{
		for (Carta carta : mano)
		{
			if (jugada==carta)
			{
                                System.out.println("Removiendo la carta " + jugada.getSigno());
				mano.remove(carta);
				break;
			}
		}
	}

	void robar(Carta pop)
	{
		this.mano.add(pop);
                System.out.println("\n\n-- Robo Automatico --");
		System.out.println("NO HAY JUGADAS POSIBLE, ROBA");
		System.out.println("Nueva mano despues de robar: \n\n");
                
                
	}

    public String showMano() {
        String ret ="";
        
        int i=1;
		for (Carta carta : mano)
		{
			ret+=("\n"+i+". "+ carta.getSigno());
                        ++i;
		}
                
       return ret;
        
     }
    
     public  void addCarta(Carta c) {
         
         this.mano.add(c);
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




   

	

}
