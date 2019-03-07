package hr.java.vjezbe.entitet;

public enum Ocjena {
	NEDOVOLJAN(1, "nedovoljan"), DOVOLJAN(2, "dovoljan"), DOBAR(3, "dobar"), VRLO_DOBAR(4, "vrlo dobar"), ODLICAN(5, "odli�an");
	
	private Integer ocjena;
	private String ocjenaString;
	
	Ocjena(Integer ocjena, String ocjenaString) {
		this.ocjena = ocjena;
		this.ocjenaString = ocjenaString;
		
	}

	public Integer getOcjena() {
		return ocjena;
	}

	public void setOcjena(Integer ocjena) {
		this.ocjena = ocjena;
	}

	public String getOcjenaString() {
		return ocjenaString;
	}

	public void setOcjenaString(String ocjenaString) {
		this.ocjenaString = ocjenaString;
	}
	
	
}
