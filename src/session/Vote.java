package session;

import java.util.ArrayList;

public class Vote {
	
	private final int ID;
	
	/**
	 * Vote constructor
	 * @param i - ID for the vote object
	 */
	public Vote(int i) {
		this.ID = i;
	}
	
	public int getID() { return ID; }
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vote)) {
			return false;
		}
		
		if (o == this) {
			return true;
		}
		
		Vote v = (Vote) o;
		return this.ID == v.getID();
	}
}
