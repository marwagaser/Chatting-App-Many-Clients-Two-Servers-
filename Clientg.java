import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Clientg {

	static JButton send;
	static TextField tf;
	static TextArea ta;
	static JPanel panel;
	static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame("Chat Application");
		frame.setSize(5000, 5000);
		send = new JButton("Send");
		JButton s1 = new JButton("6000");
		JButton s2 = new JButton("6789");
		tf = new TextField(100);
		ta = new TextArea(100, 100);
		ta.setBackground(Color.PINK);
		frame.setSize(500, 500);
		frame.setLayout(new FlowLayout());
		frame.setBackground(Color.RED);
		panel = new JPanel();
		frame.add(panel);
		panel.add(send);
		panel.add(ta);
		panel.add(tf);
		panel.add(s1);
		panel.add(s2);
		frame.setVisible(true);
		send.setVisible(false);
		tf.setVisible(false);
		s1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				s1.setVisible(false);
				s2.setVisible(false);
				send.setVisible(true);
				tf.setVisible(true);
				// int portnumber = sc.nextInt();
				try {
					Socket clientSocket = null;
					clientSocket = new Socket("marwagaser", 6000);
					DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
						Composedmsg(tf, outToServer);
						ReceivedMsg(inFromServer, ta);
				
					

				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		s2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				s1.setVisible(false);
				s2.setVisible(false);
				send.setVisible(true);
				tf.setVisible(true);
				// int portnumber = sc.nextInt();
				try {
					Socket clientSocket;
					clientSocket = new Socket("marwagaser", 6789);
					DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
					Composedmsg(tf, outToServer);
					ReceivedMsg(inFromServer, ta);

				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

	}

	public static void Composedmsg(TextField tf, DataOutputStream outToServer) {// ADD TEXTFIELD FE EL MSG
		Thread outgoingmsg = new Thread(new Runnable() {
			@Override
			public void run() {

				send.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						// while (true) {
						String msg = tf.getText();
						tf.setText(" ");
						tf.setText("");
						try {
							if (msg.equalsIgnoreCase("GetMemberList()")) {
								outToServer.writeUTF("GetMemberList()");

							} else if (msg.equalsIgnoreCase("Quit") || msg.equalsIgnoreCase("Bye")) {
								outToServer.writeUTF("closeit");
								frame.dispose();
							} else if (msg.contains("chat(")) {
								outToServer.writeUTF(msg);
							}
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
					// }

				});
			}

		});
		outgoingmsg.start();

	}

	public static void ReceivedMsg(DataInputStream inFromServer, TextArea ja) {

		Thread incomingmsg = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// String display;
				while (true) {

					try {
						String showIT = inFromServer.readUTF();
						ja.append(showIT);
						System.out.println(showIT);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		});
		incomingmsg.start();

	}

}
