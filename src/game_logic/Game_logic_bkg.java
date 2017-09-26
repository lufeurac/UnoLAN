package game_logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

	public static void save_state()
	{
		File outFile = new File("./save_state.txt");
		FileOutputStream outStream = null;
		PrintWriter dataOutStream = null;
		String temp;
		try
		{
			outStream = new FileOutputStream(outFile);
			dataOutStream = new PrintWriter(outStream);
			dataOutStream.println("UNOlan save-state");
			dataOutStream.println("Server running on: " + maint.toString());
			dataOutStream.println("---------------------------------------------");
			temp = "Players: \n";
			
			for(Thread t : kek.keySet())
			{
				dataOutStream.println(kek.get(t).getNombre() + " running on " + t.toString());
			}
			dataOutStream.println("---------------------------------------------");
			dataOutStream.println("Status:");
			dataOutStream.println("---------------------------------------------");			
			for(Jugador j : tablero.getJugadores())
			{
				dataOutStream.println(j.getNombre() + " cards: \n" + j.showMano());
			}
			dataOutStream.println("---------------------------------------------");
			dataOutStream.println(tablero.mostrarTurno());
			dataOutStream.println("---------------------------------------------");
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error en ruta de archivo:" + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("Error grabando el archivo:" + e.getMessage());
		}
		catch (Exception e)
		{
			System.out.println("excepcion inesperada:" + e.getMessage());
		}
		finally
		{
			try
			{
				dataOutStream.close();
				outStream.close();
			}
			catch (IOException e)
			{
				System.out.println("excepcion cerrando el archivo:" + e.getMessage());
			}
		}

	}
}
