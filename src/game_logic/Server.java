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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server
{

	public static void main(String[] args) throws IOException, InterruptedException
	{
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
		for (Jugador s : filler)
		{
			System.out.println(s.getNombre());
		}
		Game_logic_bkg.tablero = new Tablero(filler);
		Game_logic_bkg.tablero.start();

		for (Thread j : Game_logic_bkg.kek.keySet())
		{
			System.out.println(j);
		}
		for (Jugador j : Game_logic_bkg.tablero.getJugadores())
		{
			System.out.println(j.getNombre());
			for (Carta c : j.getMano())
			{
				System.out.println(c.getSigno());
			}
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
		boolean primeravez = true; // para que solo asigne los jugadores al
									// cliente una sola vez
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
							// out.writeUTF("Ingrese bien el comado y el nick");
							// out.flush();
							player.setNombre(read.substring(5));

						}
						else
						{
							player.setNombre(read.substring(5));
							System.out.println(player.getNombre());

							Game_logic_bkg.kek.put(Thread.currentThread(), player);
							synchronized (this)
							{
								notify();
							}

							out.writeObject(player);
							out.flush();
						}
					}

					read = in.readUTF();
					System.out.println("lei: " + read);
					switch (read)
					{

						case "mostrar turno":
						{

							out.writeUTF("mostar turno");
							out.flush();

							if (Game_logic_bkg.check_turn(Thread.currentThread()))
							{
								out.writeUTF(Game_logic_bkg.tablero.mostrarTurno());
								out.flush();
							}
							else
							{

								out.writeUTF("no es tu turno");
								out.flush();

							}
							break;
						}

						case "elegir carta":
						{
							if (Game_logic_bkg.check_turn(Thread.currentThread()))
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
												col = ("Azul"); // color por
																// defecto
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
							/*
							 * if
							 * (Game_logic_bkg.check_turn(Thread.currentThread()
							 * )) {
							 * 
							 * out.writeUTF("holiwis :3"); out.flush(); } else {
							 * out.writeUTF("holiwis :3"); out.flush();
							 * System.out.println("Responde " + this.s); break;
							 * }
							 */
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
				catch (ClassNotFoundException ex)
				{
					Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
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
