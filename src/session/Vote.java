package session;

/**
 * A vote object
 * @author Jay
 *
 */
public class Vote {
	
	private String ID;
	
	/**
	 * Vote constructor
	 * @param i - ID for the vote object
	 */
	public Vote(String i) {
		this.ID = i;
	}
	
	public String getID() { return ID; }
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vote)) {
			return false;
		}
		
		if (o == this) {
			return true;
		}
		
		Vote v = (Vote) o;
		
		return ID.equals(v.getID());
	}
}
