package hr.java.vjezbe.entitet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.glavna.Glavna;

/**
 * Predstavlja predmet (kolegij). On ima svoju šifru, naziv, broj ECTS bodova,
 * profesora koji je nositelj predmeta te popis studenata koji pohaðaju predmet
 * 
 * @author domin
 */
public class Predmet extends Entitet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5223035687628121944L;
	private String sifra;
	private String naziv;
	private Integer brojEctsBodova;
	private Profesor nositelj;
	private Set<Student> popisStudenata;
	private static final Logger logger = LoggerFactory.getLogger(Predmet.class);

	public Predmet(long id, String sifra, String naziv, Integer brojEctsBodova, Profesor nositelj) {
		super(id);
		this.sifra = sifra;
		this.naziv = naziv;
		this.brojEctsBodova = brojEctsBodova;
		this.nositelj = nositelj;
		this.popisStudenata = new HashSet<>();
	}

	/**
	 * Dohvaæa šifru predmeta
	 * 
	 * @return šifra predmeta
	 */
	public String getSifra() {
		return sifra;
	}

	/**
	 * Postavlja šifru predmeta
	 * 
	 * @param sifra šifra predmeta
	 */
	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	/**
	 * Dohvaæa naziv predmeta
	 * 
	 * @return naziv predmeta
	 */
	public String getNaziv() {
		return naziv;
	}

	/**
	 * Postavlja naziv predmeta
	 * 
	 * @param naziv naziv predmeta
	 */
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	/**
	 * Dohvaæa broj ECTS bodova predmeta
	 * 
	 * @return broj ECTS bodova predmeta
	 */
	public Integer getBrojEctsBodova() {
		return brojEctsBodova;
	}

	/**
	 * Postavlja broj ECTS bodova predmeta
	 * 
	 * @param brojEctsBodova broj ECTS bodova predmeta
	 */
	public void setBrojEctsBodova(Integer brojEctsBodova) {
		this.brojEctsBodova = brojEctsBodova;
	}

	/**
	 * Dohvaæa profesora koji je nositelj predmeta
	 * 
	 * @return profesor koji je nositelj predmeta
	 */
	public Profesor getNositelj() {
		return nositelj;
	}

	/**
	 * Postavlja profesora koji je nositelj predmeta
	 * 
	 * @param nositelj profesor koji je nositelj predmeta
	 */
	public void setNositelj(Profesor nositelj) {
		this.nositelj = nositelj;
	}

	/**
	 * Dohvaæa popis studenata koji pohaðaju predmet
	 * 
	 * @return popis studenata koji pohaðaju predmet
	 */
	public Set<Student> getPopisStudenata() {
		return popisStudenata;
	}

	/**
	 * Postavlja popis studenata koji pohaðaju predmet
	 * 
	 * @param popisStudenata popis studenata koji pohaðaju predmet
	 */
	public void setPopisStudenata(Set<Student> popisStudenata) {
		this.popisStudenata = popisStudenata;
	}

	/**
	 * Služi za dodavanje studenata u listu studenata za odreðeni predmet
	 * 
	 * @param s student za dodavanje u listu
	 */
	public void appendPopisStudenata(Student s) {
		this.popisStudenata.add(s);
	}
	
	

	@Override
	public String toString() {
		return naziv + ", ECTS=" + brojEctsBodova + " nositelj=" + nositelj;
	}

	/**
	 * Unosi predmet. Korisnik unosi šifru, naziv, broj ECTS bodova i broj studenata
	 * koji pohaðaju predmet te bira profesora koji je nositelj kolegija
	 * 
	 * @param input     Scanner za unos s tipkovnice
	 * @param profesori niz profesora iz kojeg se bira nositelj
	 * @return unešen predmet
	 */

	public static Predmet unesiPredmet(Scanner input, List<Profesor> profesori) {

		boolean nastaviPetlju = false;
		Integer odabirProfesora = 0;
		Integer brojEctsBodova = 0;

		long id = Glavna.unesiInteger("Unesi ID predmeta", input);

		System.out.print("Unesite šifru predmeta: ");
		String sifra = input.nextLine();

		System.out.print("Unesite naziv predmeta: ");
		String naziv = input.nextLine();

		brojEctsBodova = Glavna.unesiInteger("Unesite broj ECTS bodova za predmet '" + naziv + "': ", input);

		// Odabir profesora
		System.out.println("Odaberite profesora ");
		int i = 0;
		for (Profesor p : profesori) {
			System.out.println((i + 1) + " " + p.getIme() + " " + p.getPrezime());
			i++;
		}
		do {
			try {
				System.out.print("Odabir >> ");
				odabirProfesora = input.nextInt();
				input.nextLine();
				nastaviPetlju = false;
			} catch (InputMismatchException ex1) {
				System.out.println("Morate unijeti brojèane vrijednosti.");
				input.nextLine();
				nastaviPetlju = true;
				logger.info("Nije unesena brojèana vrijednost! " + ex1);
			}
		} while (nastaviPetlju || (odabirProfesora < 1 || odabirProfesora > profesori.size()));
		odabirProfesora--; // zato sto ako je unos 2, to je element na broju 1 u arrayu

		return new Predmet(id, sifra, naziv, brojEctsBodova, profesori.get(odabirProfesora));
	}

	public static List<Predmet> dohvatiPredmete(List<String> sviPredmeti, Integer brojPredmeta, Integer brUstanove,
			List<Profesor> profesori, List<Student> studenti) {

		List<Predmet> predmeti = new ArrayList<>();
		List<String> listaStringova = new ArrayList<>();
		Integer linenum = 1;
		final Integer brZapisa = 6;

		for (String string : sviPredmeti) {
			if (linenum > (brUstanove * brojPredmeta * brZapisa)
					&& linenum <= ((brUstanove + 1) * brojPredmeta * brZapisa)) {
				listaStringova.add(string);
			}
			++linenum;
		}

		for (int i = 0; i < brojPredmeta * brZapisa; i += brZapisa) {
			long id = Long.parseLong(listaStringova.get(i));
			String sifra = listaStringova.get(i + 1);
			String ime = listaStringova.get(i + 2);
			Integer brojEctsBodova = Integer.parseInt(listaStringova.get(i + 3));
			Integer idProfesora = Integer.parseInt(listaStringova.get(i + 4));
			String ideviStudenata = listaStringova.get(i + 5);
			Profesor nositelj = null;
			Set<Student> setStudenata = new HashSet<>();

			// odreðivanje traženog profesora prema predanom indexu iz datoteke
			for (Profesor profesor : profesori) {
				if (profesor.getId() == idProfesora) {
					nositelj = profesor;
				}
			}

			setStudenata = setStudenataIzNjihovihIndexa(studenti, stringToListInteger(ideviStudenata));

			Predmet temp = new Predmet(id, sifra, ime, brojEctsBodova, nositelj);
			temp.setPopisStudenata(setStudenata);

			predmeti.add(temp);
		}

		return predmeti;

	}
	
	
	public static List<String> dohvatiSvePredmete(String filePath) {

		List<String> listaStringova = new ArrayList<>();

		System.out.println("Uèitavanje predmeta…");

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

	public static List<Integer> stringToListInteger(String string) {
		Integer nOfIntegers = (string.length() + 1) / 2;
		List<Integer> integerList = new ArrayList<>();
		for (int i = 0; i < nOfIntegers * 2; i += 2) {
			integerList.add(Character.getNumericValue(string.charAt(i)));
		}
		return integerList;
	}

	protected static Set<Student> setStudenataIzNjihovihIndexa(List<Student> studenti, List<Integer> listaIndexa) {
		Set<Student> setStudenata = new HashSet<>();

		for (Student student : studenti) {
			if (listaIndexa.contains((int) (long) student.getId())) {
				setStudenata.add(student);
			}
		}

		return setStudenata;
	}

}
