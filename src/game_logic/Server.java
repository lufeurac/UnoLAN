package game_logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket ss = new ServerSocket(1234, 6); // puerto quemado, max 6 conexiones
		List<Thread> clientes = new ArrayList<Thread>(); // lista de threads, quizas cambiarlo por ids?
		boolean game_flag = false; // boolean de emergencia

		while (clientes.size() != 4)
		{
			Socket s = null;
			try
			{
				// espera a cliente
				System.out.println("Listening");
				s = ss.accept(); 

				System.out.println("A new client connected : " + s);
				System.out.println("Assigning new thread for this client");

				// Crea clase de Thread, ver abajo, HAY COMENTARIOS
				Handler t = new Handler(s);
				
				// Inicie al pinche y agreguelo al contenedor (que toca modificar?)
				clientes.add(t);
				t.start();
				
				//cout mostrando cuantos handlers (clientes) se estan ejecutando
				System.out.println("Number of active threads from the given thread: " + Thread.activeCount());
			}
			catch (Exception e)
			{
				s.close();
				e.printStackTrace();
			}
		}
		while (!game_flag)
		{
			//TODO: inicio del juego y manejo de la rotacion de turnos
			/*
			 * 
			 * MIRAR CLASE TABLERO, quizas crearla como clase publica?
			 * serialize clientes, asi se chambonean los threads? -> puede ser, entrar a evaluar			 
			 * 
			 */			
		}
	}
}

// ClientHandler class
class Handler extends Thread
{
	//Puerto y streams de entrada y salida (VER LA PT DOCUMENTACION >:V)
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
		boolean flag = false; //booleano de emergencia
		try
		{
			// Creacion de los Streams y prueba de envio al cliente
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			out.writeUTF("Bienvenido al servidor!, esperando jugadores");
			out.flush(); // EDIT: HACER FLUSH DESPUES DE CADA OUT
			Scanner sc = new Scanner(System.in); // pa que era esto?

			while (!flag)
			{
				try
				{
					/*
					 * Lectura de los comandos que llegan desde el cliente, como se recibe un OBJETO toca castear
					 * pero ya trae metodo para las strings
					*/
					read = in.readUTF();
					System.out.println(read);

					switch (read)
					{
						//TODO: Insertar funcionalidad de comandos de juego aca
						case "Pedir carta":
						
							break;
						
						case "Help": // prueba e conexion constante
							out.writeUTF("holiwis :3");
							out.flush();
							System.out.println("Responde " + this.s);
							break;

						case "Exit": // cierre de puertos EDIT: Sale y mata al thread, pero como que corre 1 vez mas
						{
							System.out.println("Client " + this.s + " sends exit...");
							System.out.println("Closing this connection.");
							this.s.close();
							flag = true;
							break;
						}
						default:
							throw new IllegalArgumentException("Comando no reconocido: " + read);
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
		Thread.currentThread().interrupt(); //kys Thread .I.
		return;
	}
}
