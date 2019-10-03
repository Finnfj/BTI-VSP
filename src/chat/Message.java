package chat;

import java.sql.Timestamp;

public class Message {
	private String clientID;
	private int messageID;
	private String message;
	private long recTime;
	
	public Message(String clientID, int messageID, String message, long recTime) {
		this.clientID = clientID;
		this.messageID = messageID;
		this.message = message;
		this.recTime = recTime;
	}

	public String getClientID() {
		return clientID;
	}

	public int getMessageID() {
		return messageID;
	}

	public String getMessage() {
		return message;
	}

	public long getRecTime() {
		return recTime;
	}
	
	public String toString() {
		return this.clientID + "-" + this.messageID + ":" + this.message + ", " + new Timestamp(this.recTime);
	}
}
