import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ManageClients implements Runnable {
	Scanner sc = new Scanner(System.in);
	public String clientid;
	final DataInputStream fromClient;
	final DataOutputStream outToClient;
	Socket connSocket;
	String onlineClient;
	ServerSocket ss;

	public void MemberListResponse() {
		ManageClients hh = this;
		// hh.outToClient.writeUTF("Server 1 has:");
		for (int i = 0; i < Multiple_Servers.servers.get(0).a.size(); i++) {

			try {
				hh.outToClient.writeUTF("Server 1 has: " + (Multiple_Servers.servers.get(0).a.get(i)).clientid+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < Multiple_Servers.servers.get(1).a.size(); i++) {
			try {
				hh.outToClient.writeUTF("Server 2 has: " + (Multiple_Servers.servers.get(1).a.get(i)).clientid+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public ManageClients(Socket connSocket, String clientid, DataInputStream fromClient, DataOutputStream outToClient,
			ServerSocket ss) {
		// this.onlineClient=true;
		this.onlineClient = "Yes";
		this.connSocket = connSocket;
		this.clientid = clientid;
		this.fromClient = fromClient;
		this.outToClient = outToClient;
		this.ss = ss;
	}

	public void run() {
		while (true) {
			try {
				String sentence = fromClient.readUTF();
				//System.out.println("gaga");// print the sentence u just sent
				
				if (sentence.equalsIgnoreCase("closeit")) {
					this.onlineClient = "No";
					this.connSocket.close();

					break;

				}

				else if (sentence.equals("GetMemberList()")) {
					MemberListResponse();
				}
				// String[] c = sentence.split("[^a-zA-Z0-9']+"); // CHAT

				else {
					String[] c = sentence.split("[^a-zA-Z0-9']+"); // CHAT
					String me = c[1];//my name
					String towho = c[2];//to who
					String msg = c[3];//the actual msg
					String ttl = c[4];//time to live

					if (senderexistswhere(towho) == senderexistswhere(me)) {
						if (Integer.parseInt(ttl) >= 2)
							Route(msg, towho);
						else {
							ManageClients hh = this;
							hh.outToClient.writeUTF("TTL TOO SMALL CANNOT SEND MSG \n");
						}
					} else {
						if (senderexistswhere(towho) != -1) {

							if (Integer.parseInt(ttl) >= 3) {
								Route(msg, towho);
							} else {
								ManageClients hh = this;
								hh.outToClient.writeUTF("TTL TOO SMALL CANNOT SEND MSG \n");
							}
						} else {
							ManageClients hh = this;
							hh.outToClient.writeUTF("The person you're sending to doesn't exist \n");
						}
					}
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		// disconnect input/output from it since ur done
		try {
			this.outToClient.close();// and output
			this.fromClient.close();// close input
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int senderexistswhere(String fromwho) {
		for (int i = 0; i < Multiple_Servers.servers.get(1).a.size(); i++) {
			ManageClients h = Multiple_Servers.servers.get(1).a.get(i);
			if (h.clientid.equals(fromwho) && h.onlineClient.equalsIgnoreCase("Yes"))

			{
				return 2;

			}
		}
		for (int i = 0; i < Multiple_Servers.servers.get(0).a.size(); i++) {
			ManageClients h = Multiple_Servers.servers.get(0).a.get(i);
			if (h.clientid.equals(fromwho) && h.onlineClient.equalsIgnoreCase("Yes"))

			{
				return 1;

			}
		}

		// else
		return -1;

	}

	public void Route(String msg, String towho) {
		boolean flag = false;
		for (int i = 0; i < Multiple_Servers.servers.get(0).a.size(); i++) {
			ManageClients h = Multiple_Servers.servers.get(0).a.get(i);
			if (h.clientid.equals(towho) && h.onlineClient.equalsIgnoreCase("Yes") && !(msg.equalsIgnoreCase("bye")))

			{
				flag = true;
				try {

					h.outToClient.writeUTF(this.clientid + ": " + msg.toUpperCase()+"\n");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		}
		if (flag == false) {
			for (int i = 0; i < Multiple_Servers.servers.get(1).a.size(); i++) {
				ManageClients h = Multiple_Servers.servers.get(1).a.get(i);
				if (h.clientid.equals(towho) && h.onlineClient.equalsIgnoreCase("Yes")
						&& !(msg.equalsIgnoreCase("bye")))

				{
					flag = true;
					try {

						h.outToClient.writeUTF(this.clientid + ": " + msg.toUpperCase()+"\n");

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
			}
		}

	}

}
