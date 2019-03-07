package hr.java.vjezbe.entitet;

/**
 * Predstavlja osobu, osoba ima ime i prezime
 * 
 * @author domin
 *
 */
public abstract class Osoba extends Entitet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6308440556204562093L;
	private String ime;
	private String prezime;

	public Osoba(long id, String ime, String prezime) {
		super(id);
		this.ime = ime;
		this.prezime = prezime;
	}

	/**
	 * Dohvaæa ime osobe
	 * 
	 * @return ime osobe
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * Postavlja ime osobe
	 * 
	 * @param ime ime osobe
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}

	/**
	 * Dohvaæa prezime osobe
	 * 
	 * @return prezime osobe
	 */
	public String getPrezime() {
		return prezime;
	}

	/**
	 * Postavlja prezime osobe
	 * 
	 * @param prezime prezime osobe
	 */
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	
	/**Dohvaæa ime i prezime osobe
	 * @return ime i prezime osobe
	 */
	public String getImePrezime() {
		return this.getIme() + " " + this.getPrezime();
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Osoba [ime=" + ime + ", prezime=" + prezime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ime == null) ? 0 : ime.hashCode());
		result = prime * result + ((prezime == null) ? 0 : prezime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Osoba other = (Osoba) obj;
		if (ime == null) {
			if (other.ime != null)
				return false;
		} else if (!ime.equals(other.ime))
			return false;
		if (prezime == null) {
			if (other.prezime != null)
				return false;
		} else if (!prezime.equals(other.prezime))
			return false;
		return true;
	}
	
	

}
