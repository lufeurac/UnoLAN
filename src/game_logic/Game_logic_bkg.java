package game_logic;

import java.io.Serializable;
import java.util.ArrayList;
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

	public static boolean check_nicknames(String nickname)
	{
		if (kek.isEmpty())
		{
			return false;
		}
		for (Jugador j : kek.values())
		{
			if (j.getNombre().equals(nickname))
			{
				return true;
			}
		}
		return false;
	}

	public static String show_help()
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	//https://stackoverflow.com/questions/17206932/determine-which-thread-owns-a-monitor/17208717#17208717
	public static long getMonitorOwner(Object obj)
	{
		if (Thread.holdsLock(obj)) return Thread.currentThread().getId();
		for (java.lang.management.ThreadInfo ti : java.lang.management.ManagementFactory.getThreadMXBean().dumpAllThreads(true, false))
		{
			for (java.lang.management.MonitorInfo mi : ti.getLockedMonitors())
			{
				if (mi.getIdentityHashCode() == System.identityHashCode(obj))
				{
					return ti.getThreadId();
				}
			}
		}
		return 0;
	}
}
