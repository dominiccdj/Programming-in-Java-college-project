package hr.java.vjezbe.entitet;

import java.io.Serializable;

public class Entitet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8645209795747170807L;
	private long id;

	public Entitet(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

}
