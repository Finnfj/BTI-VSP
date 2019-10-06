package chat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Server extends UnicastRemoteObject implements RemoteInterface {
	private static final String INTERFACENAME = "MessageService";
	private int queueLen;
	private int keepHistoryTime;
	private int messageCounter;
	private List<Message> messageQueue;
	private Map<String,ClientMemory> history;
	
	private Logger logger = Logger.getLogger("ServerLog");
	private FileHandler fh;
	private Registry registry;

	// StringProperty for reading Server log externally
	private StringProperty logMsg = new SimpleStringProperty();
	public StringProperty getLogMsg() {
		return logMsg;
	}

	public Server(int queueLen, int keepHistoryTime) throws RemoteException {
		this.queueLen = queueLen;
		this.keepHistoryTime = keepHistoryTime;

		messageCounter = 0;
		messageQueue = new ArrayList<Message>();
		history = new HashMap<String,ClientMemory>();

		// Log starten
	    try {   
	    	fh = new FileHandler("./Server.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();
	        fh.setFormatter(formatter);   
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } 
	    
		// Set up cleaner Thread that deletes old Clients
		Thread cleaner = new CleanerThread();
		cleaner.start();
		try {
			try {
				registry = LocateRegistry.createRegistry(1099);
			} catch (Exception e) {}
			registry = LocateRegistry.getRegistry();
			registry.rebind(INTERFACENAME, this);
			log("Server started with Messagebuffer=" + queueLen + " and Client caching of " + keepHistoryTime + "s");
		} catch (RemoteException e) {
			log("Server could not be started");
			e.printStackTrace();
		}
	}

	@Override
	public void newMessage(String clientID, String message) throws RemoteException {
		if (messageQueue.size() >= queueLen) {
			messageQueue.remove(0);
			log("receiving message while buffer is full, delete oldest message...");
		}
		Message m = new Message(clientID, messageCounter++, message, System.currentTimeMillis());
		messageQueue.add(m);
		log("received following message: " + m.toString());
	}

	@Override
	public String nextMessage(String clientID) throws RemoteException {
		Message message = null;
		ClientMemory client;
		
		if (history.containsKey(clientID)) {
			client = history.get(clientID);
			
			if (client != null) {
				client.setDeathTime(System.currentTimeMillis() + keepHistoryTime*1000);
				Message lastMessage = client.getLastSent();
				
				if (lastMessage != null) {
					int lastMessageIndex = messageQueue.indexOf(lastMessage);
					if (lastMessageIndex == -1) {
						message = sendOldest();
					} else {
						// die n�chste Nachricht senden
						if (lastMessageIndex + 1 < messageQueue.size()) {
							message = messageQueue.get(lastMessageIndex + 1);
						} else {
							log(clientID + " reading message but nothing to deliver");
							return "";
						}
					}
				} else {
					message = sendOldest();
				}
			} else {
				System.out.println("Client key in map aber keine Entry");
			}
		} else {
			message = sendOldest();
			
			// Neue Memory anlegen
			client = new ClientMemory(System.currentTimeMillis() + keepHistoryTime*1000, message);
			history.put(clientID, client);
		}

		client.setLastSent(message);
		if (message == null) {
			log(clientID + " reading message but nothing to deliver");
			return "";
		} else { 
			log(clientID + " reading message, delivering message: " + message.toString());
			return message.toString() + "\n";
		}
	}
	
	private Message sendOldest() {
		Message message = null;
		// �lteste bekannte senden
		if (!messageQueue.isEmpty()) {
			// sende �lteste Nachricht
			message = messageQueue.get(0);
		}
		return message;
	}
	
	private void log(String s) {
		logger.info(s);
		logMsg.set(s);
	}
	
	class CleanerThread extends Thread {
		@Override
        public void run() {
            while (true) {
                cleanHist();
                try {
                    Thread.sleep(keepHistoryTime*1000 / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

		private void cleanHist() {
			long currTime = System.currentTimeMillis();
			Collection<ClientMemory> hist = history.values();
			for (ClientMemory c : hist) {
				if (c.getDeathTime() <= currTime) {
					log("TTL of a client exceeded, removing from cache...");
					hist.remove(c);
				}
			}
		}
	}
}
