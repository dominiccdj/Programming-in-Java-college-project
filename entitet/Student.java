package hr.java.vjezbe.entitet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.glavna.Glavna;

/**
 * Predstavlja studenta koji ima JMBAG, datum roðenja i ostale pojedinosti
 * "Osobe"
 * 
 * @see Osoba
 * @author domin
 *
 */
public class Student extends Osoba {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8965536845939962674L;
	private String jmbag;
	private LocalDate datumRodjenja;
	private static final Logger logger = LoggerFactory.getLogger(Ispit.class);

	public Student(long id, String ime, String prezime, String jmbag, LocalDate datumRodjenja) {
		super(id, ime, prezime);
		this.jmbag = jmbag;
		this.datumRodjenja = datumRodjenja;
	}

	/**
	 * Dohvati JMBAG studenta
	 * 
	 * @return JMBAG studenta
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Postavi JMBAG studenta
	 * 
	 * @param jmbag JMBAG studenta
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * Dohvati datum roðenja studenta
	 * 
	 * @return datum roðenja studenta
	 */
	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}

	/**
	 * Postavi datum roðenja studenta
	 * 
	 * @param datumRodjenja datum roðenja studenta
	 */
	public void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((datumRodjenja == null) ? 0 : datumRodjenja.hashCode());
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (datumRodjenja == null) {
			if (other.datumRodjenja != null)
				return false;
		} else if (!datumRodjenja.equals(other.datumRodjenja))
			return false;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return jmbag + " " + getImePrezime() + " " + datumRodjenja.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
	}

	/**
	 * Unosi studenta. Korisnik unosi ime, prezime, datum roðenja te JMBAG.
	 * 
	 * @param input Scanner za unos s tipkovnice
	 * @return unešen student
	 */
	public static Student unesiStudenta(Scanner input) {

		boolean nastaviPetlju = false;
		LocalDate datumRodjenja = null;

		long id = Glavna.unesiInteger("Unesi ID studenta: ", input);

		System.out.print("Unesite ime studenta: ");
		String ime = input.next();
		input.nextLine();

		System.out.print("Unesite prezime studenta: ");
		String prezime = input.nextLine();

		do {
			try {
				System.out
						.print("Unesite datum roðenja studenta " + prezime + " " + ime + " u formatu (dd.MM.yyyy.): ");
				String datumRodjenjaString = input.nextLine();
				datumRodjenja = LocalDate.parse(datumRodjenjaString, DateTimeFormatter.ofPattern("dd.MM.yyyy."));
				nastaviPetlju = false;
			} catch (Exception e) {
				System.out.println("Krivo unešen datum!");
				logger.info("Krivo unešen datum! " + e);
				nastaviPetlju = true;
			}
		} while (nastaviPetlju);

		System.out.print("Unesite JMBAG studenta: ");
		String jmbag = input.nextLine();

		return new Student(id, ime, prezime, jmbag, datumRodjenja);
	}

	/**
	 * Vraæa niz studenata koji su polagali onaj predmet koji je dan kao argument
	 * 
	 * @param ispiti  niz ispita
	 * @param predmet predmet iz kojeg želimo izdvojiti studente
	 * @return niz studenata za odreðeni predmet
	 */
	public static Student[] razvrstajStudente(List<Ispit> ispiti, Predmet predmet) {

		Integer duljinaNizaStudenata = predmet.getPopisStudenata().size();
		Student[] tempStudenti = new Student[duljinaNizaStudenata];
		Integer tempStudentiBrojac = 0;

		for (Ispit ispit : ispiti) {
			if (predmet == ispit.getPredmet()) {
				tempStudenti[tempStudentiBrojac] = ispit.getStudent();
				tempStudentiBrojac++;
			}
		}

		return tempStudenti;
	}

	public static List<Student> dohvatiStudente(List<String> sviStudenti, Integer brojStudenta, Integer brUstanove) {

		List<Student> studenti = new ArrayList<>();
		List<String> listaStringova = new ArrayList<>();
		Integer linenum = 1;
		final Integer brZapisa = 5;
		
		for (String string : sviStudenti) {
			if (linenum > (brUstanove * brojStudenta * brZapisa)
					&& linenum <= ((brUstanove + 1) * brojStudenta * brZapisa)) {
				listaStringova.add(string);
			}
			++linenum;
		}

		for (int i = 0; i < brojStudenta * brZapisa; i += brZapisa) {
			long id = Long.parseLong(listaStringova.get(i));
			String ime = listaStringova.get(i + 1);
			String prezime = listaStringova.get(i + 2);
			String jmbag = listaStringova.get(i + 3);
			String datumRodjenjaString = listaStringova.get(i + 4);
			LocalDate datumRodjenja = LocalDate.parse(datumRodjenjaString, DateTimeFormatter.ofPattern("dd.MM.yyyy."));

			Student temp = new Student(id, ime, prezime, jmbag, datumRodjenja);
			studenti.add(temp);
		}

		return studenti;

	}

	public static List<String> dohvatiSveStudente(String filePath) {

		List<String> listaStringova = new ArrayList<>();

		System.out.println("Uèitavanje studenata…");

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
