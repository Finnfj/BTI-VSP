

public class ClientMemory {
	private long deathTime;
	private Message lastSent;
	
	public ClientMemory(long deathTime, Message lastSent) {
		this.deathTime = deathTime;
		this.lastSent = lastSent;
	}

	public long getDeathTime() {
		return deathTime;
	}

	public void setDeathTime(long deathTime) {
		this.deathTime = deathTime;
	}

	public Message getLastSent() {
		return lastSent;
	}

	public void setLastSent(Message lastSent) {
		this.lastSent = lastSent;
	}
}
