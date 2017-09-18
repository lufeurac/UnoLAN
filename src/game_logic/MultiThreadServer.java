package game_logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class MultiThreadServer implements Runnable
{
	
	/*
	 * 
	 * CLASE DE PRUEBAS
	 * 
	 */
	List<Socket> mano;
	Socket csocket;

	MultiThreadServer(Socket csocket)
	{
		this.csocket = csocket;
	}

	public static void main(String args[]) throws Exception
	{
		ServerSocket ssock = new ServerSocket(1234); //Bloqueea directo a este puerto, buscar si se puede multi puerto
		System.out.println("Listening");

		while (true)
		{
			Socket sock = ssock.accept(); // Block logico hasta que encuentre algo en el socket
			sock.toString();
			System.out.println("Connected");
			new Thread(new MultiThreadServer(sock)).start(); //Crear Thread, almacenar los threads o los sockets en array?
		}
	}

	public void run()
	{
		try
		{
			PrintStream pstream = new PrintStream(csocket.getOutputStream()); // Solo salida, investigar ObjectStream para lo de serliazable
			for (int i = 100; i >= 0; i--)
			{
				pstream.println(i + " bottles of beer on the wall");
			}
			pstream.close(); // si se cierra el socket, todos sus streams se cierran, evitar redundancia
			// EDIT: CERRAR AL MISMO TIEMPO EN SERVER - CLIENT
			csocket.close();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
}
