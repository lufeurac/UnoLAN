package game_logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server
{

	public static void main(String[] args) throws IOException, InterruptedException
	{
		Game_logic_bkg.maint = Thread.currentThread();

		List<Thread> clientes = new ArrayList<Thread>();

		ServerSocket ss = new ServerSocket(1234);

		boolean game_flag = false;

		while (clientes.size() != 2)
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

		Game_logic_bkg.tablero = new Tablero(filler);
		Game_logic_bkg.tablero.start();

		synchronized (Game_logic_bkg.maint)
		{
			Game_logic_bkg.maint.notifyAll();
		}

		System.out.println("TOY VIVO :3");
		while (!game_flag)
		{
			for (Thread t : Game_logic_bkg.kek.keySet())
			{
				if (!t.isAlive())
				{
					ss.close();
					for (Thread t2 : Game_logic_bkg.kek.keySet())
						t2.interrupt();
					
					System.out.println("DESCONEXION INESPERADA - PARANDO JUEGO");
					game_flag = true;
				}
			}
			Game_logic_bkg.save_state();
		}
		Thread.currentThread().interrupt();
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
		String name = null;
		String read;
		ObjectOutputStream out;
		ObjectInputStream in;
		boolean flag = false;
		boolean new_player = false;
		boolean game_started = false;
		Jugador player = new Jugador();
		Scanner sc = new Scanner(System.in);

		try
		{
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());

			out.writeUTF("Bienvenido al servidor!");
			out.flush();
			out.writeUTF("Ingrese el nick de jugador: <Nick (player_nickname)>");
			out.flush();

			while (!flag)
			{
				// Asks the Player(client) for a nickname as an id
				while (!new_player)
				{
					read = in.readUTF();
					if (!read.contains("Nick "))
					{
						System.out.println("Ingrese bien el comando y el nick");
						out.writeBoolean(false);
						out.flush();
					}
					else
					{
						// Checks if the nickname is being used
						if (Game_logic_bkg.check_nicknames(read.substring(5)))
						{
							out.writeBoolean(false);
							out.flush();
						}
						else
						{
							out.writeBoolean(true);
							out.flush();

							player.setNombre(read.substring(5));
							name = player.getNombre();

							Game_logic_bkg.kek.put(Thread.currentThread(), player);
							synchronized (this)
							{
								notify();
							}
							new_player = true;
						}
					}
				}
				// Thread waits until the server gets 4 players and then the
				// game starts
				if (!game_started)
				{
					synchronized (Game_logic_bkg.maint)
					{
						Game_logic_bkg.maint.wait();
					}

					player = Game_logic_bkg.tablero.getPlayer(player.getNombre());

					// Info about the game is sent to the Player(client)
					out.writeObject(player);
					out.flush();
					game_started = true;
					out.writeUTF(Game_logic_bkg.tablero.mostrarTurno());
					out.flush();
				}
				// Reads the commands that the Player(client) writes
				read = in.readUTF();
				System.out.println("lei: " + read);

				switch (read)
				{
					case "mostrar mesa":
					{
						out.writeUTF("mostar mesa");
						out.flush();
						out.writeUTF(Game_logic_bkg.tablero.mostrarTurno());
						out.flush();

						break;
					}
					case "elegir carta":
					{
						if (Game_logic_bkg.check_turn(player.getNombre()))
						{
							if (Game_logic_bkg.tablero.comprobarRobar())
							{ // si tiene que robar
								out.writeUTF("robar");
								out.flush();

								out.writeObject(Game_logic_bkg.tablero.getManoFromPlayer(player.getNombre()));
								out.flush();

								Carta c = Game_logic_bkg.tablero.robar();
								out.writeObject(c);
								out.flush();

								List<Carta> mano2 = (List<Carta>) in.readObject();
								Game_logic_bkg.tablero.getTurno_Actual().setMano(mano2);
								System.out.println("Mano de: " + Game_logic_bkg.tablero.getTurno_Actual().getMano().size());

								out.writeUTF("FIN DE SU TURNO");
								out.flush();

								Game_logic_bkg.tablero.pasarTurno();

								break;

							}
							else
							{
								out.writeUTF("elegir carta");
								out.flush();

								out.writeObject(Game_logic_bkg.tablero.getManoFromPlayer(player.getNombre()));
								out.flush();

								out.writeBoolean(Game_logic_bkg.tablero.comprobarRobar());
								out.flush();

								Carta jugada = (Carta) in.readObject();
								System.out.println(jugada.getSigno());

								boolean logica = Game_logic_bkg.tablero.checkLogic(jugada);

								out.writeBoolean(logica);
								out.flush();

								while (!logica)
								{
									jugada = (Carta) in.readObject();

									logica = Game_logic_bkg.tablero.checkLogic(jugada);
									out.writeBoolean(logica);
									out.flush();

								}

								if (!jugada.getEspecial().equals("no especial"))
								{
									if (jugada.getEspecial().equals("CAMBIO DE COLOR") || jugada.getEspecial().equals("TOMA CUATRO"))
									{
										System.out.println("ENTRA");
										out.writeUTF("Digite A para amarillo, V para verde, R para rojo, AZ para azul: ");
										out.flush();

										String col = (in.readUTF());

										if (col.equalsIgnoreCase("a"))
										{
											col = "Amarillo";
										}
										else if (col.equalsIgnoreCase("v"))
										{
											col = "Verde";
										}
										else if (col.equalsIgnoreCase("r"))
										{
											col = ("Rojo");
										}
										else if (col.equalsIgnoreCase("az"))
										{
											col = ("Azul");
										}
										else
										{
											col = ("Azul");
										}
										System.out.println("col:" + col);
										out.writeUTF(Game_logic_bkg.tablero.jugadaCartaEspecialColor(jugada, col));
										out.flush();
									}
									else
									{
										out.writeUTF(Game_logic_bkg.tablero.jugadaEspecialSinPregunta(jugada));
										out.flush();
									}
								}

								Game_logic_bkg.tablero.JugarCarta(jugada);
								List<Carta> mano2 = (List<Carta>) in.readObject();
								Game_logic_bkg.tablero.getTurno_Actual().setMano(mano2);
								System.out.println("Mano de: " + Game_logic_bkg.tablero.getTurno_Actual().getMano().size());

								out.writeUTF("FIN DE SU TURNO");
								out.flush();
								if (jugada.getEspecial().equals("PIERDE TURNO"))
								{
									Game_logic_bkg.tablero.pasarTurno();
								}
								Game_logic_bkg.tablero.pasarTurno();
							}
						}
						else
						{
							out.writeUTF("no es tu turno");
							out.flush();
						}
						break;
					}

					case "ver cartas":
					{
						out.writeUTF("ver cartas");
						out.flush();
						out.writeUTF(player.showMano());
						out.flush();
						break;
					}

					case "Ayuda":
					{
						out.writeUTF("cmd");
						out.flush();
					}

					case "Exit":
					{
						System.out.println("Client " + this.s + " sends exit...");
						System.out.println("Closing this connection.");
						this.s.close();
						flag = true;
						break;
					}
					default:
						out.writeUTF("cmd");
						out.flush();
						break;
				}
				if (!(Game_logic_bkg.tablero.getGanador() == null)) flag = true;
			}

			System.out.println("Connection closed");
		}
		catch (IOException | ClassNotFoundException | InterruptedException | NullPointerException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Thread.currentThread().interrupt(); // kys Thread .I.
		}
		return;
	}

	private void cartaEspecial(ObjectOutputStream out, ObjectInputStream in, Carta jugada) throws IOException
	{

		if (jugada.getEspecial().equalsIgnoreCase("CAMBIO DE COLOR") || jugada.getEspecial().equalsIgnoreCase("TOMA CUATRO"))
		{

			out.writeUTF("Digite A para amarillo, V para verde, R para rojo, AZ para azul:");
			out.flush();

			System.out.println(in.readUTF());

		}

	}
}
