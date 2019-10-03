package chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
	public void newMessage(String clientID, String message) throws RemoteException;
	public String nextMessage(String clientID) throws RemoteException;
}
