package game_logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game_logic_bkg
{
	public static Tablero tablero;	
	public static Map<Thread, Jugador> kek = new HashMap<>();

	public static boolean check_turn(Thread client)
	{
		if (kek.get(client).equals(tablero.getTurno_Actual()))
		{
			return true;
		}
		return false;
	}
}
