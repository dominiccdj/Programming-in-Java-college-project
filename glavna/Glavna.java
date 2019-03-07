package hr.java.vjezbe.glavna;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.FakultetRacunarstva;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.ObrazovnaUstanova;
import hr.java.vjezbe.entitet.Ocjena;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.Sveuciliste;
import hr.java.vjezbe.entitet.VeleucilisteJave;
import hr.java.vjezbe.iznimke.PopisProfesoraException;
import hr.java.vjezbe.sortiranje.StudentSorter;

/**
 * Glavna klasa programa
 * 
 * @author domin
 *
 */
public class Glavna {

	private static final int BROJ_PROFESORA = 2;
	private static final int BROJ_PREDMETA = 3;
	private static final int BROJ_STUDENATA = 2;
	private static final int BROJ_ISPITNIH_ROKOVA = BROJ_STUDENATA;
	private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		logger.info("Program je pokrenut");

		boolean nastaviPetlju = false;

		Integer brojObrazovnihUstanova = unesiInteger("Unesite broj obrazovnih ustanova: ", input);
		
		long idSveucilista = unesiInteger("Unesi ID sveucilista", input);

		Sveuciliste<ObrazovnaUstanova> sveuciliste = new Sveuciliste<>(idSveucilista);

		for (int i = 0; i < brojObrazovnihUstanova; i++) {
			long idUstanove;
			List<Profesor> profesori = new ArrayList<>();
			List<Predmet> predmeti = new ArrayList<>();
			List<Student> studenti = new ArrayList<>();
			List<Ispit> ispitniRokovi = new ArrayList<>();
			Map<Profesor, List<Predmet>> mapaProfesora = new HashMap<>();
			Set<Profesor> keys = mapaProfesora.keySet();

			System.out.println("Unesite podatke za " + (i + 1) + ". obrazovnu ustanovu");
			
			idUstanove = unesiOcjenu("Unesi ID za " + (i + 1) + ". ustanovu: ", input);

			// Unos profesora
			for (int j = 0; j < BROJ_PROFESORA; j++) {
				System.out.println("Unos " + (j + 1) + ". profesora: ");
				profesori.add(Profesor.unesiProfesora(input));
			}

			// Unos predmeta
			for (int j = 0; j < BROJ_PREDMETA; j++) {
				System.out.println("Unos " + (j + 1) + ". predmeta: ");
				Predmet tempPredmet = Predmet.unesiPredmet(input, profesori);
				predmeti.add(tempPredmet);

				// Dodavanje profesora i nj predmeta u mapu
				if (mapaProfesora.containsKey(tempPredmet.getNositelj())) {
					mapaProfesora.get(tempPredmet.getNositelj()).add(tempPredmet);
				} else {
					List<Predmet> tempList = new ArrayList<>();
					tempList.add(tempPredmet);
					mapaProfesora.put(tempPredmet.getNositelj(), tempList);
				}
			}

			// Provjera ima li svaki profesor barem jedan predmet i postoji li barem jedan
			// profesoor
			// koji predaje >1 predmeta
			Integer brProfSViseOdJednogPred = 0;
			for (Profesor profesor : keys) {
				if (mapaProfesora.get(profesor).size() == 0) {
					throw new PopisProfesoraException("Postoji profesor koji ne predaje niti jedan predmet!");
				}

				if (mapaProfesora.get(profesor).size() > 1) {
					brProfSViseOdJednogPred++;
				}
			}
			if (brProfSViseOdJednogPred == 0) {
				throw new PopisProfesoraException("Ne postoji profesor koji predaje vi�e od jednog predmeta!");
			}

			// Ispis profesora i predmeta koje oni predaju
			Integer k;
			for (Profesor profesor : keys) {
				k = 0;
				System.out.println("Profesor " + profesor.getImePrezime() + " predaje sljede�e predmete: ");
				for (Predmet predmet : mapaProfesora.get(profesor)) {
					System.out.println((++k) + ") " + predmet.getNaziv());
				}
			}

			// Unos studenata
			for (int j = 0; j < BROJ_STUDENATA; j++) {
				System.out.println("Unos " + (j + 1) + ". studenta: ");
				studenti.add(Student.unesiStudenta(input));
			}

			// unos ispitnih rokova
			for (int j = 0; j < BROJ_ISPITNIH_ROKOVA; j++) {
				System.out.println("Unos " + (j + 1) + ". ispitnog roka: ");
				ispitniRokovi.add(Ispit.unesiIspitniRok(input, predmeti, studenti));
			}
			ispitniRokovi.stream().filter(is -> is.getOcjena() == 5).forEach(is -> System.out.println("Student " + is.getStudent().getImePrezime() + " je ostvario ocjenu '" + Ocjena.ODLICAN.getOcjenaString() + "' na predmetu '" + is.getPredmet().getNaziv()));

			// studenti koji su pristupili ispitima iz odre�enih predmeta staviti
			// u polje studenata za zasebni predmet
			for (Ispit ispit : ispitniRokovi) {
				for (Predmet predmet : predmeti) {
					if (ispit.getPredmet() == predmet) {
						predmet.appendPopisStudenata(ispit.getStudent());
					}
				}
			}

			// Sortiranje i ispis studenata upisanih na odre�eni predmet iz lista predmet
			for (Predmet predmet : predmeti) {
				System.out.println("Studenti upisani na predmet '" + predmet.getNaziv() + "' su: ");
				if (predmet.getPopisStudenata().size() == 0) {
					System.out.println("Nema studenata upisanih na predmet '" + predmet.getNaziv() + "'. ");
				} else {
					List<Student> temp = new ArrayList<>();
					temp.addAll(predmet.getPopisStudenata());
					Collections.sort(temp, new StudentSorter());
					temp.stream().forEach(student -> System.out.println(student.getImePrezime()));
				}

			}

			// ODABIR OBRZOVNE USTANOVE
			Integer odabirObrazovneUstanove = 0;
			do {
				try {
					System.out.print(
							"Odaberite obrazovnu ustanovu za navedene podatke koju �elite unijeti (1 - Veleu�ili�te Jave, 2 - Fakultet ra�unarstva): ");
					odabirObrazovneUstanove = input.nextInt();
					input.nextLine();
					nastaviPetlju = false;
				} catch (InputMismatchException ex1) {
					System.out.println("Morate unijeti broj�ane vrijednosti.");
					input.nextLine();
					nastaviPetlju = true;
					logger.info("Nije unesena broj�ana vrijednost! " + ex1);
				}
			} while (nastaviPetlju || (odabirObrazovneUstanove != 1 && odabirObrazovneUstanove != 2));

			// UNOS NAZIVA OBRAZOVNE USTANOVE
			System.out.print("Unesite naziv obrazovne ustanove: ");
			String nazivUstanove = input.nextLine();

			switch (odabirObrazovneUstanove) {
			case 1:
				sveuciliste.dodajObrazovnuUstanovu(VeleucilisteJave.unesiVeleucilisteJave(idUstanove, nazivUstanove, predmeti,
						profesori, studenti, ispitniRokovi, input, i));
				break;
			case 2:
				sveuciliste.dodajObrazovnuUstanovu(FakultetRacunarstva.unesiFakultetRacunarstva(idUstanove, nazivUstanove, predmeti,
						profesori, studenti, ispitniRokovi, input, i));
				break;
			default:
				break;
			}
			
		}
		
		// komparazor za usporedbu 2 obrazovne ustanove (da lambda za sorted bude urednija)
		Comparator<ObrazovnaUstanova> comparator = (o1, o2) -> Integer.toString(o1.getBrojStudenata()).compareTo(Integer.toString(o2.getBrojStudenata()));
		comparator.thenComparing( (o1, o2) -> o1.getNazivUstanove().compareTo(o2.getNazivUstanove()) );

		// dohva�anje obrazovnih ustanova za sortiranje
		List<ObrazovnaUstanova> sortObrazovneUstanove = sveuciliste.dohvatiObrazovneUstanove();     

		// sortiranje obrazovnih ustanova
		sortObrazovneUstanove.stream().sorted(comparator);
		
		// ispis obrazovnih ustanova
		sortObrazovneUstanove.stream().forEach(o -> System.out.println(o.getNazivUstanove() + ": " + o.getBrojStudenata() + " student(a)"));

		logger.info("Program je zavr�io s radom\n");
	}

	/**
	 * Vra�a trenutnu godinu u obliku Stringa
	 * 
	 * @return String trenutna godina
	 */
	public static String getCurrentYearString() {
		LocalDate localDate = LocalDate.now();// For reference
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
		String formattedString = localDate.format(formatter);
		return formattedString;
	}

	/**
	 * Unosi cijeli broj i vra�a ga, tako�er ispisuje poruku prije unosa broja
	 * 
	 * @param poruka poruka za ispis prije unosa broja
	 * @param input  Scanner za unos s tipkovnice
	 * @return une�eni broj
	 */
	public static Integer unesiInteger(String poruka, Scanner input) {
		boolean nastaviPetlju = false;
		Integer intZaUnos = 0;
		do {
			try {
				System.out.print(poruka);
				intZaUnos = input.nextInt();
				input.nextLine();
				nastaviPetlju = false;
			} catch (InputMismatchException ex1) {
				System.out.println("Morate unijeti broj�ane vrijednosti.");
				input.nextLine();
				nastaviPetlju = true;
				logger.info("Nije unesena broj�ana vrijednost! " + ex1);
			}
		} while (nastaviPetlju);

		return intZaUnos;
	}

	/**
	 * Unosi ocjenu u obliku cijelog broja i vra�a ju, tako�er ispisuje poruku prije
	 * unosa ocjene. Ocjena mora biti broj manji ili jednak 5 te ve�i ili jednak 1
	 * 
	 * @param poruka poruka za ispis prije unosa ocjene
	 * @param input  Scanner za unos s tipkovnice
	 * @return une�ena ocjena
	 */
	public static Integer unesiOcjenu(String poruka, Scanner input) {
		boolean nastaviPetlju = false;
		Integer intZaUnos = 0;
		do {
			try {
				System.out.print(poruka);
				intZaUnos = input.nextInt();
				input.nextLine();
				nastaviPetlju = false;
			} catch (InputMismatchException ex1) {
				System.out.println("Morate unijeti broj�ane vrijednosti.");
				input.nextLine();
				nastaviPetlju = true;
				logger.info("Nije unesena broj�ana vrijednost! " + ex1);
			}
		} while (nastaviPetlju || (intZaUnos < 1 || intZaUnos > 5));

		return intZaUnos;
	}

}
