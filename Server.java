import java.net.*;
import java.util.*;
import java.io.*;

public class Server {
	private ServerSocket listen;						// for accepting connections
	private ArrayList<ServerCommunicator> comms;	// all the connections with clients

	public Server(ServerSocket listen) {
		this.listen = listen;
		comms = new ArrayList<ServerCommunicator>();
	}

	/**
	 * The usual loop of accepting connections and firing off new threads to handle them
	 */
	public void getConnections() throws IOException {
		while (true) {
			//listen.accept in next line blocks until new connection
			ServerCommunicator comm = new ServerCommunicator(listen.accept(), this);
			comm.setDaemon(true);
			comm.start();
			addCommunicator(comm);
		}
	}

	/**
	 * Adds the handler to the list of current client handlers
	 */
	public synchronized void addCommunicator(ServerCommunicator comm) {
		comms.add(comm);
	}

	/**
	 * Removes the handler from the list of current client handlers
	 */
	public synchronized void removeCommunicator(ServerCommunicator comm) {
		comms.remove(comm);
	}

	/**
	 * Sends the message from the one client handler to all the others (but not echoing back to the originator)
	 */
	public synchronized void broadcast(ServerCommunicator from, String msg) {
		for (ServerCommunicator c : comms) {
			if (c != from) {
				c.send(msg);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("waiting for connections");
		new Server(new ServerSocket(4242)).getConnections();
	}
}