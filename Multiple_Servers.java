import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Multiple_Servers {
	public static ArrayList<server1> servers = new ArrayList<server1>(2);

	public static void main(String[] args) throws IOException {
		// ServerSocket ss1 = new ServerSocket(6789);
		// ServerSocket ss2 = new ServerSocket(6000);
		int port1 = 6789;
		server1 sOne = new server1(new ServerSocket(port1));
		int port2 = 6000;
		server1 sTwo = new server1(new ServerSocket(port2));
		servers.add(0, sOne);
		servers.add(1, sTwo);
		sOne.start();
		sTwo.start();
	}
}
