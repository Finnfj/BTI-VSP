package chat;

import java.rmi.RemoteException;

import javax.swing.JButton;

public class Main extends javax.swing.JFrame {

	private void initGUI() {
        JButton jButton1 = new javax.swing.JButton();
        JButton jButton2 = new javax.swing.JButton();
        JButton jButton3 = new javax.swing.JButton();
        JButton jButton4 = new javax.swing.JButton();
        JButton jButton5 = new javax.swing.JButton();
        JButton jButton6 = new javax.swing.JButton();
 
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.FlowLayout());
 
        jButton1.setText("jButton1");
        getContentPane().add(jButton1);
 
        jButton2.setText("jButton2");
        getContentPane().add(jButton2);
 
        jButton3.setText("jButton3");
        getContentPane().add(jButton3);
 
        jButton4.setText("jButton4");
        getContentPane().add(jButton4);
 
        jButton5.setText("jButton5");
        getContentPane().add(jButton5);
 
        jButton6.setText("jButton6");
        getContentPane().add(jButton6);
 
        pack();
	}
	
	public static void main(String[] args) throws RemoteException {
		Main frame=new Main();
	    frame.setTitle("RMI-Chat");
	    frame.setSize(1000, 620);
	    frame.setResizable(false);
	    frame.setLocation(50, 50);
	    frame.setVisible(true);
	    frame.initGUI();
//		Server server = new Server(8, 30);
//		Client client = new Client("localhost", 30);
//
//		client.sendMessage("HAHAHAHA");
//		client.getMessage();
//		client.getMessage();
//		client.getMessage();
//		client.sendMessage("HöHAHAHA");
//		client.getMessage();
	}
}
