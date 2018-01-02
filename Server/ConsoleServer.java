package Server;

import java.util.Scanner;

/**
 * Simply console server.
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

				if(port > 0 && port <= 65535)
				{
					defaultConnection = false;
				}
				else
					defaultConnection = true;
			}
			catch(NumberFormatException e)
			{
				defaultConnection = true;
			}
		}

		if(defaultConnection)
		{
			ThreadedServer server = ThreadedServer.getInstance();
			Thread serverThread = new Thread(server);
			serverThread.start();
		}
		else
		{
			ThreadedServer server = ThreadedServer.getInstance(hostname, port);
			Thread serverThread = new Thread(server);
			serverThread.start();
		}

		System.out.println("Chinese Game Server\nWorking on " + hostname + ":" + port);

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
