package chat;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends UnicastRemoteObject implements RemoteInterface {
	private static final String INTERFACENAME = "MessageService";
	private int queueLen;
	private int keepHistoryTime;
	private int messageCounter;
	private List<Message> messageQueue;
	private Map<String,ClientMemory> history;
	

	public Server(int queueLen, int keepHistoryTime) throws RemoteException {
		this.queueLen = queueLen;
		this.keepHistoryTime = keepHistoryTime;

		messageCounter = 0;
		messageQueue = new ArrayList<Message>();
		history = new HashMap<String,ClientMemory>();
		
		Thread cleaner = new CleanerThread();
		cleaner.start();
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry = LocateRegistry.getRegistry();
			registry.rebind(INTERFACENAME, this);
			System.out.println("Server erfolgreich initialisiert");
		} catch (RemoteException e) {
			System.out.println("Server nicht erfolgreich initialisiert");
			e.printStackTrace();
		}
	}

	@Override
	public void newMessage(String clientID, String message) throws RemoteException {
		if (messageQueue.size() >= queueLen) {
			messageQueue.remove(0);
		}
		messageQueue.add(new Message(clientID, messageCounter++, message, System.currentTimeMillis()));
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
						// die nächste Nachricht senden
						if (lastMessageIndex + 1 < messageQueue.size()) {
							message = messageQueue.get(lastMessageIndex + 1);
						} else {
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
			return "";
		} else { 
			return message.toString();
		}
	}
	
	private Message sendOldest() {
		Message message = null;
		// älteste bekannte senden
		if (!messageQueue.isEmpty()) {
			// sende älteste Nachricht
			message = messageQueue.get(0);
		}
		return message;
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
					hist.remove(c);
				}
			}
		}
	}
}
