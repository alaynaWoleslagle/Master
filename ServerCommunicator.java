import java.io.*;
import java.net.*;


public class ServerCommunicator extends Thread {
	private Socket sock;					// each instance is in a different thread and has its own socket
	private Server server;				// the main server instance
	private String name;					// client's name (first interaction with server)
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client

	public ServerCommunicator(Socket sock, Server server) {
		this.sock = sock;
		this.server = server;
	}

	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Identify -- first message is the name
			name = in.readLine();
			System.out.println("it's "+name);
			out.println("welcome "+name);
			server.broadcast(this, name + " entered the room");

			// Chat away
			String line;
			while ((line = in.readLine()) != null) {
				String msg = name + ":" + line;
				System.out.println(msg);
				server.broadcast(this, msg);
			}

			// Done
			System.out.println(name + " hung up");
			server.broadcast(this, name + " left the room");

			// Clean up -- note that also remove self from server's list of handlers so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
}
