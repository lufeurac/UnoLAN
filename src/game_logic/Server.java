package game_logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket ss = new ServerSocket(0);
		List<Socket> clientes = new ArrayList<Socket>();
		boolean game_flag = false;
		// running infinite loop for getting
		// client request
		while (clientes.size() != 4)
		{
			Socket s = null;

			try
			{
				// socket object to receive incoming client requests
				s = ss.accept();

				System.out.println("A new client connected : " + s);

				// obtaining input and out streams
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(s.getInputStream());

				System.out.println("Assigning new thread for this client");

				// create a new thread object
				Thread t = new Handler(s, in, out);

				// Invoking the start() method
				t.start();
				clientes.add(s);
			}
			catch (Exception e)
			{
				s.close();
				e.printStackTrace();
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

	public Handler(Socket s, ObjectInputStream in, ObjectOutputStream out)
	{
		this.s = s;
		this.in = in;
		this.out = out;
	}

	public void run()
	{
		String write;
		String read;
		try
		{
			out.writeUTF("Bienvenido al servidor!, esperando jugadores");
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		while (true)
		{
			try
			{
				read = in.readUTF();
				System.out.println(read);

				switch (read)
				{
					case "/Help":

						break;

					case "/Exit":
					{
						System.out.println("Client " + this.s + " sends exit...");
						System.out.println("Closing this connection.");
						this.s.close();
						System.out.println("Connection closed");
						break;
					}

					default:
						throw new IllegalArgumentException("Comando no reconocido: " + read);						
				}
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				this.in.close();
				this.out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
