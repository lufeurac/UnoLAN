package game_logic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game_logic_bkg implements Serializable
{
	public static Tablero tablero;	
	public static Thread maint;
	public static Map<Thread, Jugador> kek = new HashMap<>();

	public static boolean check_turn(String client)
	{
		if (client.equals(tablero.getTurno_Actual().getNombre()))
		{
			return true;
		}
		return false;
	}
}