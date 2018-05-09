
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class server1 extends Thread {
	// static ArrayList<ManageClients> a = new ArrayList<ManageClients>();

	ServerSocket s;
	ArrayList<ManageClients> a;

	public server1(ServerSocket s) {
		this.s = s;
		this.a = new ArrayList<ManageClients>();
	}

	public static void JoinResponse(String x) {
		if (x.equals("DONE")) {
			System.out.println("Joined");
		} else if (x.equals("tryagain")) {
			System.out.println("Username already exists.Try again.");
		} else
			System.out.println("Wrong Join format");
	}

	@Override
	public void run() {
		String r = "";
		boolean used = false;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// listening socket always listening to requests
			// ServerSocket listeningSocket = new ServerSocket(6789);
			while (true) {// always listening
				// create connection socket for each connection request you get
				Socket connectionSocket = s.accept();
				while (true) {
					System.out.println("Please enter your username in the form: Join(insert username here)");
					String username = br.readLine();
					if (username.contains("Join(")) {
						String answer = username.substring(username.indexOf("(") + 1, username.indexOf(")"));
						for (int i = 0; i < a.size(); i++) {
							String m = a.get(i).clientid;
							if (m.equals(answer)) {
								used = true;
								r = "tryagain";
							}
						}
						if (used != true) {
							r = "DONE";
							JoinResponse(r);
							// get input to connection socket from client
							DataInputStream intoConn = new DataInputStream(connectionSocket.getInputStream());
							// get output to client from connection socket
							DataOutputStream outConn = new DataOutputStream(connectionSocket.getOutputStream());
							ManageClients Manyusers = new ManageClients(connectionSocket, answer, intoConn, outConn, s);
							// create a thread to avoid non-parallel running process
							Thread td = new Thread(Manyusers);
							a.add(Manyusers);
							td.start();// start the thread
							break;

						} else {
							JoinResponse(r);
							used = false;
						}
					} else {
						JoinResponse(r);
						continue;
					}
				}

			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}
}