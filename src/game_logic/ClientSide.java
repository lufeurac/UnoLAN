package game_logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class ClientSide
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			//2 lazy 2 comment, mas tarde :V
			String serverAddress = JOptionPane.showInputDialog("Enter IP Address of a machine that is\n" + "running the date service on port 1234:");
			Socket s = new Socket(serverAddress, 1234);
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.flush();
			
			String answer = s.getInetAddress().toString();// =
			System.out.println("connected : " + s);		
			Scanner sc = new Scanner(System.in);
			
			answer = in.readUTF();
			System.out.println(answer);
			JOptionPane.showMessageDialog(null, answer);
			
			while (true)
			{
				answer = sc.nextLine();				
						
				out.writeUTF(answer);
				out.flush();			

				//ORDEN IMPORTA!!!
				if (answer.equals("Exit"))
				{
					System.out.println("cerrando");
					s.close();
					break;					
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
