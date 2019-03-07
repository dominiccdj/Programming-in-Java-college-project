package hr.java.vjezbe.entitet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.glavna.Glavna;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

/**
 * Predstavlja veleuèilište Jave koji je obrazovna ustanova. Sadrži metode za
 * izraèun konaène ocjene studija za studenta, najuspješnijeg studenta te metodu
 * za unos ocjena završnog rada te na temelju toga izraèun konaène ocjene
 * 
 * @author domin
 *
 */
public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2604012211958873858L;
	private static final Logger logger = LoggerFactory.getLogger(VeleucilisteJave.class);

	public VeleucilisteJave(long id, String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori,
			List<Student> studenti, List<Ispit> ispitniRokovi) {
		super(id, nazivUstanove, predmeti, profesori, studenti, ispitniRokovi);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.java.vjezbe.entitet.Visokoskolska#izracunajKonacnuOcjenuStudijaZaStudenta(
	 * hr.java.vjezbe.entitet.Ispit[], float, float)
	 */
	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> tempIspiti, float ocjenaPismenog,
			float ocjenaObraneZavrsnogRada) {

		BigDecimal konacnaOcjena = new BigDecimal(0);
		try {
			konacnaOcjena = odrediProsjekOcjenaNaIspitima(tempIspiti);
			konacnaOcjena = konacnaOcjena.multiply(new BigDecimal(2));
			konacnaOcjena = konacnaOcjena.add(new BigDecimal(ocjenaObraneZavrsnogRada))
					.add(new BigDecimal(ocjenaPismenog));
			konacnaOcjena = konacnaOcjena.divide(new BigDecimal(4));
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			System.out.println("Student " + tempIspiti.get(0).getStudent().getIme() + " "
					+ tempIspiti.get(0).getStudent().getPrezime()
					+ " zbog negativne ocjene na jednom od ispita ima prosjek „nedovoljan (1)“!");
			logger.info(e.getMessage());
			konacnaOcjena = new BigDecimal(1);
		}

		return konacnaOcjena;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hr.java.vjezbe.entitet.ObrazovnaUstanova#odrediNajuspjesnijegStudentaNaGodini
	 * (float)
	 */
	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(float brGodine) {

		List<Student> studenti = getPopisStudenata();
		BigDecimal najveciProsjek = new BigDecimal(0);
		Integer indexNajboljegStudenta = 0;
		Integer i = 0;
		// prolazak petljom kroz studente
		for (Student student : studenti) {
			List<Ispit> ispitiZaStudenta = filtrirajIspitePoStudentu(getPopisIspita(), student);
			BigDecimal prosjekStudenta = new BigDecimal(0);

			try {
				prosjekStudenta = odrediProsjekOcjenaNaIspitima(ispitiZaStudenta);
			} catch (NemoguceOdreditiProsjekStudentaException e) {
				// Student ima jednu negativnu ocjenu te mu je prosjek = 1
				prosjekStudenta = new BigDecimal(1);
			}

			if (prosjekStudenta.compareTo(najveciProsjek) == 1) {
				najveciProsjek = prosjekStudenta;
				indexNajboljegStudenta = i;
			} else if (prosjekStudenta.compareTo(najveciProsjek) == 0) {
				najveciProsjek = prosjekStudenta;
				indexNajboljegStudenta = i;
			}

			i++;

		}

		return studenti.get(indexNajboljegStudenta);
	}

	/**
	 * Na temelju predanih parametara (naziv, polja predmeta, profesora, studenata i
	 * ispita) stvoriti objekt VeleucilisteJave i odrediti najuspješnijeg studenta
	 * na godini
	 * 
	 * @param nazivUstanove naziv ustanove
	 * @param predmeti      niz predmeta
	 * @param profesori     niz profesora
	 * @param studenti      niz studenata
	 * @param ispitniRokovi niz ispita
	 * @param input         Scanner za unos s tipkovnice
	 * @return unešeno veleuèilište Jave
	 */
	public static VeleucilisteJave unesiVeleucilisteJave(long id, String nazivUstanove, List<Predmet> predmeti,
			List<Profesor> profesori, List<Student> studenti, List<Ispit> ispitniRokovi, Scanner input, Integer brUstanove) {

		ObrazovnaUstanova tempObrazovnaUstanova = new VeleucilisteJave(id, nazivUstanove, predmeti, profesori, studenti,
				ispitniRokovi);

		((VeleucilisteJave) tempObrazovnaUstanova).unosOcjenaIIzracunKonacneDatoteke(brUstanove, 
				tempObrazovnaUstanova.getPopisStudenata(), tempObrazovnaUstanova.getPopisIspita());

		Student najuspjesniji = ((VeleucilisteJave) tempObrazovnaUstanova)
				.odrediNajuspjesnijegStudentaNaGodini(Float.parseFloat("2018"));
		System.out.println("Najbolji student " + "2018" + ". godine je " + najuspjesniji.getIme() + " "
				+ najuspjesniji.getPrezime() + " JMBAG: " + najuspjesniji.getJmbag());

		return (VeleucilisteJave) tempObrazovnaUstanova;
	}

	/**
	 * Unosi ocjene završnog rada te raèuna prosjeènu za svakog studenta na temelju
	 * ocjena završnog rada te ocjena iz ispita. Ako student ima jednu negativnu
	 * ocjenu onda ne može pristupiti diplomskom radu te mu je konaèna ocjena
	 * nedovoljan (1)
	 * 
	 * @param input    Scanner za unos s tipkovnice
	 * @param studenti niz studenata
	 * @param ispiti   niz ispita
	 */
	public void unosOcjenaIIzracunKonacne(Scanner input, List<Student> studenti, List<Ispit> ispiti) {
		List<Student> tempStudenti = studenti;

		for (Student student : tempStudenti) {
			float tempOcjenaZavrsnog = 0;
			float tempOcjenaObrane = 0;
			List<Ispit> tempIspiti = filtrirajIspitePoStudentu(ispiti, student);

			// Provjera ima li student koju negativnu ocjenu iz ispita
			boolean sviIspitiPolozeni = sviIspitiPolozeni(tempIspiti);

			if (sviIspitiPolozeni) {
				// Unos ocjene završnog rada
				tempOcjenaZavrsnog = Glavna.unesiOcjenu("Unesite ocjenu završnog rada za studenta: " + student.getIme()
						+ " " + student.getPrezime() + ": ", input);

				// Unos ocjene obrane završnog rada
				tempOcjenaObrane = Glavna.unesiOcjenu("Unesite ocjenu obrane završnog rada za studenta: "
						+ student.getIme() + " " + student.getPrezime() + ": ", input);

				BigDecimal tempKonacnaOcjena = izracunajKonacnuOcjenuStudijaZaStudenta(tempIspiti, tempOcjenaZavrsnog,
						tempOcjenaObrane);
				System.out.println("Konaèna ocjena studija studenta " + student.getIme() + " " + student.getPrezime()
						+ " je " + tempKonacnaOcjena);
			} else {
				System.out.println("Student " + student.getIme() + " " + student.getPrezime()
						+ " zbog negativne ocjene na jednom od ispita ima prosjek „nedovoljan (1)“! ");
			}

		}

	}

	public void unosOcjenaIIzracunKonacneDatoteke(Integer brUstanove, List<Student> studenti, List<Ispit> ispiti) {
		List<Student> tempStudenti = studenti;
		Integer counter = 0;

		// unos linija s podacima ocjena završnog za jednu obrazovnu u listu stringova

		List<String> listaStringova = new ArrayList<>();
		Integer linenum = 1;
		final Integer brZapisa = 1;

		try (BufferedReader in = new BufferedReader(new FileReader("dat/obrane.txt"))) {
			String line;

			while ((line = in.readLine()) != null) {
				if (linenum > (brUstanove * studenti.size() * brZapisa)
						&& linenum <= ((brUstanove + 1) * studenti.size() * brZapisa)) {
					listaStringova.add(line);
				}
				++linenum;
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		for (Student student : tempStudenti) {
			float tempOcjenaZavrsnog = 0;
			float tempOcjenaObrane = 0;
			List<Ispit> tempIspiti = filtrirajIspitePoStudentu(ispiti, student);

			// Provjera ima li student koju negativnu ocjenu iz ispita
			boolean sviIspitiPolozeni = sviIspitiPolozeni(tempIspiti);

			if (sviIspitiPolozeni) {
				// Unos ocjene završnog rada
				 tempOcjenaZavrsnog = Character.getNumericValue(listaStringova.get(counter).charAt(0));
				 
				// Unos ocjene obrane završnog rada
				 tempOcjenaObrane = Character.getNumericValue(listaStringova.get(counter).charAt(2));
				 
				BigDecimal tempKonacnaOcjena = izracunajKonacnuOcjenuStudijaZaStudenta(tempIspiti, tempOcjenaZavrsnog,
						tempOcjenaObrane);
				System.out.println("Konaèna ocjena studija studenta " + student.getIme() + " " + student.getPrezime()
						+ " je " + tempKonacnaOcjena);
			} else {
				System.out.println("Student " + student.getIme() + " " + student.getPrezime()
						+ " zbog negativne ocjene na jednom od ispita ima prosjek „nedovoljan (1)“! ");
			}

			counter++;
			
		}

	}

}
