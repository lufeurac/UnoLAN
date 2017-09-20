package game_logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Server
{
	public static void main(String[] args) throws IOException, InterruptedException
	{
		List<Thread> clientes = new ArrayList<Thread>();
		
		ServerSocket ss = new ServerSocket(1234);

		boolean game_flag = false;

		while (clientes.size() != 4)
		{
			Socket s = null;
			try
			{
				System.out.println("Listening");
				s = ss.accept();

				System.out.println("A new client connected : " + s);
				System.out.println("Assigning new thread for this client");

				Handler t = new Handler(s);

				t.start();
				clientes.add(t);
				synchronized (t)
				{
					t.wait();	
				}
				System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
			}
			catch (Exception e)
			{
				s.close();
				e.printStackTrace();
			}
		}		
		List<Jugador> filler = new ArrayList<>();
		filler.addAll(Game_logic_bkg.kek.values());
		for(Jugador s : filler)
			System.out.println(s.getNombre());
		Game_logic_bkg.tablero = new Tablero(filler);
		Game_logic_bkg.tablero.start();
		
		for (Thread j : Game_logic_bkg.kek.keySet())
		{
			System.out.println(j);
		}
		for (Jugador j : Game_logic_bkg.tablero.getJugadores())
		{
			System.out.println(j.getNombre());
			for(Carta c : j.getMano())
				System.out.println(c.getSigno());
		}
		while (!game_flag)
		{

		}
	}
}

// ClientHandler class
class Handler extends Thread
{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket s;

	public Handler(Socket s)
	{
		this.s = s;
	}

	public void run()
	{
		String write;
		String read;
		ObjectOutputStream out;
		ObjectInputStream in;
		boolean flag = false;
		Jugador player = new Jugador();
		Scanner sc = new Scanner(System.in); 
		
		try
		{		
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			
			out.writeUTF("Bienvenido al servidor!, esperando jugadores");
			out.flush(); 
			out.writeUTF("Ingrese el nick de jugador: <Nick (player_nickname)>");
			out.flush(); 

			while (!flag)
			{
				try
				{							
					
					while (player.getNombre() == null)
					{
						read = in.readUTF();			
						if (!read.contains("Nick "))
						{
							System.out.println("Ingrese bien el comado y el nick");
							//out.writeUTF("Ingrese bien el comado y el nick");
							//out.flush();							
							player.setNombre(read.substring(5));							
						}
						else
						{
							player.setNombre(read.substring(5));
							Game_logic_bkg.kek.put(Thread.currentThread(), player);	
							synchronized (this)
							{
								notify();								
							}
						}
					}
					
					out.writeObject(player);
					out.flush();
					read = in.readUTF();			
					// 192.168.0.104
					System.out.println(read);
					switch (read)
					{
						
						case "Pedir carta":

							break;

						case "ver cartas": 
							/*if (Game_logic_bkg.check_turn(Thread.currentThread()))
							{

								out.writeUTF("holiwis :3");
								out.flush();
							}
							else
							{
								out.writeUTF("holiwis :3");
								out.flush();
								System.out.println("Responde " + this.s);
								break;
							}*/		
							out.writeObject(Game_logic_bkg.tablero.getPlayer(player.getNombre()));
							out.flush();
							out.writeUTF("holiwis :3");
							out.flush();
							break;

						case "Exit":									
						{
							System.out.println("Client " + this.s + " sends exit...");
							System.out.println("Closing this connection.");
							this.s.close();
							flag = true;
							break;
						}
						default:
							// throw new IllegalArgumentException("Comando no
							// reconocido: " + read);
					}
				}
				catch (IOException e)
				{
					e.getMessage();
				}
			}
			System.out.println("Connection closed");
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		Thread.currentThread().interrupt(); // kys Thread .I.
		return;
	}
}
