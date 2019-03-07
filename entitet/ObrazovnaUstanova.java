package hr.java.vjezbe.entitet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Obrazovna ustanova koja ima naziv te polja predmeta, profesora, studenata i
 * ispita
 * 
 * @author domin
 */
public abstract class ObrazovnaUstanova extends Entitet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7680950945654509118L;
	private String nazivUstanove;
	List<Predmet> popisPredmeta = new ArrayList<>();
	List<Profesor> popisProfesora = new ArrayList<>();
	List<Student> popisStudenata = new ArrayList<>();
	List<Ispit> popisIspita = new ArrayList<>();

	public ObrazovnaUstanova(long id, String nazivUstanove, List<Predmet> popisPredmeta, List<Profesor> popisProfesora,
			List<Student> popisStudenata, List<Ispit> popisIspita) {
		super(id);
		this.nazivUstanove = nazivUstanove;
		this.popisPredmeta = popisPredmeta;
		this.popisProfesora = popisProfesora;
		this.popisStudenata = popisStudenata;
		this.popisIspita = popisIspita;
	}

	/**
	 * Dohvaæa naziv obrazovne ustanove
	 * 
	 * @return naziv obrazovne ustanov
	 */
	public String getNazivUstanove() {
		return nazivUstanove;
	}

	/**
	 * Postavlja naziv obrazovne ustanov
	 * 
	 * @param nazivUstanove naziv obrazovne ustanove
	 */
	public void setNazivUstanove(String nazivUstanove) {
		this.nazivUstanove = nazivUstanove;
	}

	/**
	 * Dohvaæa popis predmeta obrazovne ustanove
	 * 
	 * @return popis predmeta obrazovne ustanove
	 */
	public List<Predmet> getPopisPredmeta() {
		return popisPredmeta;
	}

	/**
	 * Postavlja popis predmeta obrazovne ustanove
	 * 
	 * @param popisPredmeta popis predmeta obrazovne ustanove
	 */
	public void setPopisPredmeta(List<Predmet> popisPredmeta) {
		this.popisPredmeta = popisPredmeta;
	}

	/**
	 * Dohvaæa popis profesora obrazovne ustanove
	 * 
	 * @return popis profesora obrazovne ustanove
	 */
	public List<Profesor> getPopisProfesora() {
		return popisProfesora;
	}

	/**
	 * Postavlja popis profesora obrazovne ustanove
	 * 
	 * @param popisProfesora popis profesora obrazovne ustanove
	 */
	public void setPopisProfesora(List<Profesor> popisProfesora) {
		this.popisProfesora = popisProfesora;
	}

	/**
	 * Dohvaæa popis studenata obrazovne ustanove
	 * 
	 * @return popis studenata obrazovne ustanove
	 */
	public List<Student> getPopisStudenata() {
		return popisStudenata;
	}

	/**
	 * Postavlja popis studenata obrazovne ustanove
	 * 
	 * @param popisStudenata popis studenata obrazovne ustanove
	 */
	public void setPopisStudenata(List<Student> popisStudenata) {
		this.popisStudenata = popisStudenata;
	}

	/**
	 * Dohvaæa popis ispita obrazovne ustanove
	 * 
	 * @return popis ispita obrazovne ustanove
	 */
	public List<Ispit> getPopisIspita() {
		return popisIspita;
	}

	/**
	 * Postavlja popis ispita obrazovne ustanove
	 * 
	 * @param popisIspita popis ispita obrazovne ustanove
	 */
	public void setPopisIspita(List<Ispit> popisIspita) {
		this.popisIspita = popisIspita;
	}

	/**
	 * Odreðuje najuspješnijeg studenta na godini
	 * 
	 * @param brGodine broj godine u kojoj se traži najuspješniji student
	 * @return najuspješnijeg studenta na godini
	 */
	public abstract Student odrediNajuspjesnijegStudentaNaGodini(float brGodine);
	
	public int getBrojStudenata() {
		return popisStudenata.size();
	}
	
	public static List<String> dohvatiSveObrazovneUstanove(String filePath) {

		List<String> listaStringova = new ArrayList<>();

		System.out.println("Uèitavanje obrazovnih ustanova…");

		try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = in.readLine()) != null) {

				listaStringova.add(line);

			}
		} catch (IOException e) {
			System.err.println(e);
		}

		return listaStringova;

	}

}
