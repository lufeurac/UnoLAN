package game_logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class ClientSide
{

	public static void main(String[] args) throws IOException
	{
		Jugador player = new Jugador();
		boolean game_started = false;
		boolean nick_accepted;
		Socket s = null;

		try
		{
			String serverAddress = JOptionPane.showInputDialog("Ingrese la ip del servidor:");
			s = new Socket(serverAddress, 1234);
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.flush();

			String answer = s.getInetAddress().toString();
			System.out.println("connected : " + s);
			Scanner sc = new Scanner(System.in);

			answer = in.readUTF();
			System.out.println(answer);
			JOptionPane.showMessageDialog(null, answer);
			answer = in.readUTF();
			System.out.println(answer);
			JOptionPane.showMessageDialog(null, answer);

			while (!game_started)
			{
				answer = sc.nextLine();
				out.writeUTF(answer);
				out.flush();
				nick_accepted = in.readBoolean();
				if (!nick_accepted)
				{
					System.out.println("Error: wrong command");
					JOptionPane.showMessageDialog(null, "Comando mal escrito o nickname ocupado: /n Comando: <Nick (player_nickname)>");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Nick aceptado, esperando a los demas jugadores");
					player = (Jugador) in.readObject();
					answer = in.readUTF();
					System.out.println(answer);
					game_started = true;
				}
			}

			while (true)
			{
				System.out.println("Escriba instruccion");
				sc = new Scanner(System.in);
				answer = sc.nextLine();

				out.writeUTF(answer);
				out.flush();

				answer = in.readUTF();
				String imp = "";
				switch (answer)
				{
					case "Ayuda":
					{
						// out.writeUTF(Game_logic_bkg.show_help());
						out.flush();
						break;
					}
					case "mostar mesa":
					{
						imp = in.readUTF();
						System.out.println(imp);
						break;
					}

					case "elegir carta":
					{

						player.setMano((List<Carta>) in.readObject());
						System.out.println(player.showMano());

						boolean roboAutomatico = in.readBoolean();
						if (!roboAutomatico)
						{
							System.out.println("Digite ID");
							int id = sc.nextInt();

							while (id < 1 || id > player.getMano().size())
							{
								System.out.println("ID INVALIDO");
								id = sc.nextInt();
							}

							out.writeObject(player.getMano().get(id - 1));
							out.flush();

							boolean logica = in.readBoolean();

							while (!logica)
							{
								System.out.println("ID ILOGICA");
								id = sc.nextInt();
								out.writeObject(player.getMano().get(id - 1));
								out.flush();

								logica = in.readBoolean();

							}

							List<Carta> manonueva = player.getMano();

							Carta jugada = player.getMano().get(id - 1);
							if (!jugada.getEspecial().equals("no especial"))
							{
								System.out.println("ESPECIAL");

								if (jugada.getEspecial().equals("CAMBIO DE COLOR") || jugada.getEspecial().equals("TOMA CUATRO"))
								{
									System.out.println(in.readUTF());
									sc = new Scanner(System.in);

									out.writeUTF(sc.nextLine());
									out.flush();

									System.out.println(in.readUTF());

								}
								else
								{

									System.out.println(in.readUTF());

								}

							}

							manonueva.remove(player.getMano().get(id - 1));
							player.setMano(manonueva);
							out.writeObject(manonueva);
							out.flush();

							System.out.println("Carta jugada satisfactoriamente");
							System.out.println(in.readUTF());
							break;

						}

						break;
					}
					case "robar":
					{

						player.setMano((List<Carta>) in.readObject());
						System.out.println(player.showMano());

						Carta c = (Carta) in.readObject();
						System.out.println("NO TIENE COMO JUGAR, ROBA LA CARTA: " + c.getSigno());
						List<Carta> nuevamano = player.getMano();
						System.out.println("Antes re robar " + nuevamano.size());
						nuevamano.add(c);
						player.setMano(nuevamano);
						System.out.println("Desoues re robar " + nuevamano.size());

						out.writeObject(nuevamano);
						out.flush();

						System.out.println(in.readUTF());

						break;
					}
					case "no es tu turno":
					{
						System.out.println("No es tu turno");
						break;
					}
					case "Exit":
					{
						System.out.println("cerrando");
						s.close();
						break;
					}
					case "ver cartas":
					{
						imp = in.readUTF();
						System.out.println(imp);
						break;
					}
					default:
						System.out.println("Comandos: \n <Exit>: desconectarse del servidor \n <ver cartas>: muestra la mano del jugador \n" +
							"<elegir carta>: selecciona la accion para realizar en un turno \n <mostrar mesa>: muestra el estado del juego");						
						break;
				}

			}
		}
		catch (Exception e)
		{
			System.out.println("cerrando");
			s.close();
		}

	}
}
