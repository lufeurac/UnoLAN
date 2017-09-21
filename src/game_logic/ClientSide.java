package game_logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class ClientSide implements Serializable
{
	public static void main(String[] args)
	{
		try
		{
			String serverAddress = JOptionPane.showInputDialog("Ingrese la ip del servidor:");
			Socket s = new Socket(serverAddress, 1234);
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
			
			answer = sc.nextLine();
			out.writeUTF(answer);
			out.flush();
			Jugador player = (Jugador) in.readObject();
			System.out.println(player.getNombre());
			

			while (true)
			{
				answer = sc.nextLine();

				out.writeUTF(answer);
				out.flush();

				if (answer.equals("Exit"))
				{
					System.out.println("cerrando");
					s.close();
					break;
				}
				if (answer.equals("ver"))
				{
					player = (Jugador) in.readObject();
					for (Carta c : player.getMano())
						System.out.println(c.toString());
					answer = sc.nextLine();
				}
				answer = in.readUTF();


				System.out.println(answer);
				JOptionPane.showMessageDialog(null, answer);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
