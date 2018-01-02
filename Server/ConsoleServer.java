package Server;

import java.util.Scanner;

/**
 * Created by Kacper on 2018-01-02.
 */
public class ConsoleServer
{
	private static String hostname;
	private static int port;
	private static boolean defaultConnection;

	public static void main(String[] args)
	{
		defaultConnection = true;

		if(args.length == 2)
		{
			defaultConnection = false;
			hostname = args[0];

			try
			{
				port = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e)
			{
				defaultConnection = true;
			}
		}

		ThreadedServer server = ThreadedServer.getInstance();
		Thread serverThread = new Thread(server);
		serverThread.start();

		Scanner scan = new Scanner(System.in);
		try
		{
			while (scan.hasNextLine())
			{
				String line = scan.nextLine().toLowerCase();
				System.out.println(line);
				if (line.equals("exit") || line.equals("quit") || line.equals("q"))
				{
					System.exit(0);
					scan.close();
				}
			}
		} finally
		{
			if (scan != null)
				scan.close();
		}
	}
}
