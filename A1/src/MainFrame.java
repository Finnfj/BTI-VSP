

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JCheckBox chckbxAutomatisch;
	private static JButton btnSenden = new JButton("Senden");
	
	private Client client;
	private Server server;
	private StringProperty chatLog = new SimpleStringProperty();
	private Thread PMThread;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
					frame.getRootPane().setDefaultButton(btnSenden);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		//setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		setTitle("RMI-Chatprogramm");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{188, 0};
		gbl_panel_2.rowHeights = new int[]{0, 69, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblClient = new JLabel("Client");
		lblClient.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblClient = new GridBagConstraints();
		gbc_lblClient.insets = new Insets(5, 0, 10, 0);
		gbc_lblClient.gridx = 0;
		gbc_lblClient.gridy = 0;
		panel_2.add(lblClient, gbc_lblClient);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		panel_2.add(panel, gbc_panel);
		panel.setBorder(null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblServeradresse = new JLabel("Serveradresse");
		GridBagConstraints gbc_lblServeradresse = new GridBagConstraints();
		gbc_lblServeradresse.insets = new Insets(0, 5, 5, 5);
		gbc_lblServeradresse.gridx = 0;
		gbc_lblServeradresse.gridy = 0;
		panel.add(lblServeradresse, gbc_lblServeradresse);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 10, 5, 10);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblTimeout = new JLabel("Timeout in S");
		GridBagConstraints gbc_lblTimeout = new GridBagConstraints();
		gbc_lblTimeout.insets = new Insets(0, 5, 5, 5);
		gbc_lblTimeout.gridx = 0;
		gbc_lblTimeout.gridy = 1;
		panel.add(lblTimeout, gbc_lblTimeout);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 10, 5, 10);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		panel.add(textField_1, gbc_textField_1);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 2;
		panel.add(panel_3, gbc_panel_3);

		btnSenden.setEnabled(false);
		JButton btnTrennen = new JButton("Trennen");
		JButton btnVerbinden = new JButton("Verbinden");
		btnVerbinden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String address = textField.getText();
				String timeoutString = textField_1.getText();
				
				// check if necessary information is existent
				if (address.equals("") || timeoutString.equals("")) {
					JOptionPane.showMessageDialog(null, "Bitte geben Sie Adresse/Timeout an!");
			        return;
				}
				
				int timeout;
				// check if timeout is really a number
				try {
					timeout = Integer.parseInt(timeoutString);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Bitte geben Sie einen Timeout in Sekunden an!");
					return;
				}
				
				client = new Client(address, timeout);
				// keine Erkennung auf fehlgeschlagene Verbindung
				
				// succesful connection
				chatLog.set("");
				PMThread = new PullMessageThread();
				PMThread.start();
				//Button logic
				btnVerbinden.setEnabled(false);
				btnTrennen.setEnabled(true);
				btnSenden.setEnabled(true);
			}
		});
		panel_3.add(btnVerbinden);

		btnTrennen.setEnabled(false);
		btnTrennen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.stop();
				client = null;
				PMThread = null;
				
				//Button logic
				btnSenden.setEnabled(false);
				btnTrennen.setEnabled(false);
				btnVerbinden.setEnabled(true);
			}
		});
		panel_3.add(btnTrennen);
		
		JTextArea textPane = new JTextArea();
		JScrollPane scrollpane = new JScrollPane(textPane);
		chatLog.addListener((new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				textPane.setText(arg2);
			}
        }));
		textPane.setEditable(false);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.insets = new Insets(5, 5, 5, 5);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 2;
		panel_2.add(scrollpane, gbc_textPane);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 3;
		panel_2.add(panel_4, gbc_panel_4);
		
		textField_2 = new JTextField();
		textField_2.setColumns(30);
		panel_4.add(textField_2);
		
		btnSenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					client.sendMessage(textField_2.getText());
					textField_2.setText("");
				} catch (RemoteException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel_4.add(btnSenden);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 4;
		panel_2.add(panel_5, gbc_panel_5);

		JCheckBox chckbxAlleNeuenNachrichten = new JCheckBox("Alle neuen Nachrichten abrufen");
		JButton btnNachrichtenAbrufen = new JButton("Nachrichten abrufen");
		btnNachrichtenAbrufen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String message = client.getMessage();
					if (chckbxAlleNeuenNachrichten.isSelected()) {
						while (message != null) {
							chatLog.set(chatLog.getValueSafe() + message + "\n");
							message = client.getMessage();
						}
					} else {
						if (message != null) {
							chatLog.set(chatLog.getValueSafe() + message + "\n");
						}
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel_5.add(btnNachrichtenAbrufen);
		
		panel_5.add(chckbxAlleNeuenNachrichten);
		
		chckbxAutomatisch = new JCheckBox("Automatisch");
		panel_5.add(chckbxAutomatisch);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{45, 0};
		gbl_panel_1.rowHeights = new int[]{17, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblServer = new JLabel("Server");
		lblServer.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblServer = new GridBagConstraints();
		gbc_lblServer.insets = new Insets(5, 0, 10, 0);
		gbc_lblServer.anchor = GridBagConstraints.NORTH;
		gbc_lblServer.gridx = 0;
		gbc_lblServer.gridy = 0;
		panel_1.add(lblServer, gbc_lblServer);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(null);
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.insets = new Insets(0, 0, 5, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 1;
		panel_1.add(panel_6, gbc_panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{0, 0, 0};
		gbl_panel_6.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_6.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JLabel lblPuffergre = new JLabel("Puffergr\u00F6\u00DFe");
		GridBagConstraints gbc_lblPuffergre = new GridBagConstraints();
		gbc_lblPuffergre.insets = new Insets(0, 5, 5, 5);
		gbc_lblPuffergre.gridx = 0;
		gbc_lblPuffergre.gridy = 0;
		panel_6.add(lblPuffergre, gbc_lblPuffergre);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(0, 10, 5, 10);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 0;
		panel_6.add(textField_3, gbc_textField_3);
		
		JLabel lblClienterinnerungszeit = new JLabel("Client-Erinnerungszeit in S");
		GridBagConstraints gbc_lblClienterinnerungszeit = new GridBagConstraints();
		gbc_lblClienterinnerungszeit.insets = new Insets(0, 5, 5, 5);
		gbc_lblClienterinnerungszeit.gridx = 0;
		gbc_lblClienterinnerungszeit.gridy = 1;
		panel_6.add(lblClienterinnerungszeit, gbc_lblClienterinnerungszeit);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.insets = new Insets(0, 10, 5, 10);
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 1;
		panel_6.add(textField_4, gbc_textField_4);
		
		JPanel panel_7 = new JPanel();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 1;
		gbc_panel_7.gridy = 2;
		panel_6.add(panel_7, gbc_panel_7);

		JTextArea textPane_1 = new JTextArea();
		JScrollPane scrollpane_1 = new JScrollPane(textPane_1);
		textPane_1.setEditable(false);
		GridBagConstraints gbc_textPane_1 = new GridBagConstraints();
		gbc_textPane_1.insets = new Insets(5, 5, 5, 5);
		gbc_textPane_1.fill = GridBagConstraints.BOTH;
		gbc_textPane_1.gridx = 0;
		gbc_textPane_1.gridy = 2;
		panel_1.add(scrollpane_1, gbc_textPane_1);

		JButton btnStoppen = new JButton("Stoppen");
		JButton btnStarten = new JButton("Starten");
		btnStarten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int bufferS = Integer.parseInt(textField_3.getText());
				int histTime = Integer.parseInt(textField_4.getText());
				try {
					server = new Server(bufferS, histTime);
					server.getLogMsg().addListener((new ChangeListener<String>() {
						@Override
						public void changed(ObservableValue<? extends String> arg0,
								String arg1, String arg2) {
							textPane_1.append(new Timestamp(System.currentTimeMillis()) + "\n" + arg2 + "\n");
						}
			        }));
					// get first logMsg, which is initialization
					textPane_1.setText(new Timestamp(System.currentTimeMillis()) + "\n" + server.getLogMsg().get() + "\n");
					
					// Button logic
					btnStarten.setEnabled(false);
					btnStoppen.setEnabled(true);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel_7.add(btnStarten);

		btnStoppen.setEnabled(false);
		btnStoppen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server.stop();
				server = null;
				
				// Button logic
				btnStoppen.setEnabled(false);
				btnStarten.setEnabled(true);
			}
		});
		panel_7.add(btnStoppen);
	}
	

	
	class PullMessageThread extends Thread {
		@Override
        public void run() {
            while (true) {
                pull();
                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

		private void pull() {
			if (client != null && chckbxAutomatisch.isSelected()) {
				try {
					String message = client.getMessage();
					while (message != null) {
						chatLog.set(chatLog.getValueSafe() + message + "\n");
						message = client.getMessage();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
