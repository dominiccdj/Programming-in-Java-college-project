package hr.java.vjezbe.glavna;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.FakultetRacunarstva;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.ObrazovnaUstanova;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.Sveuciliste;
import hr.java.vjezbe.entitet.VeleucilisteJave;
import hr.java.vjezbe.iznimke.PopisProfesoraException;
import hr.java.vjezbe.sortiranje.StudentSorter;

public class GlavnaDatoteke {

	private static final int BROJ_PROFESORA = 2;
	private static final int BROJ_PREDMETA = 3;
	private static final int BROJ_STUDENATA = 2;
	private static final int BROJ_ISPITNIH_ROKOVA = BROJ_STUDENATA;
	private static final Logger logger = LoggerFactory.getLogger(Glavna.class);
	public static final String SERIALIZATION_FILE_NAME = "dat/obrazovne-ustanove.dat";

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in); // radi kompatibilnosti sa metodama stare Glavna klase
		Sveuciliste<ObrazovnaUstanova> sveuciliste = new Sveuciliste<>(1);
		
		List<String> sviProfesori = Profesor.dohvatiSveProfesore("dat/profesori.txt");
		List<String> sviStudenti = Student.dohvatiSveStudente("dat/studenti.txt");
		List<String> sviPredmeti = Predmet.dohvatiSvePredmete("dat/predmeti.txt");
		List<String> sviIspiti = Ispit.dohvatiSveIspite("dat/ispitniRokovi.txt");
		List<String> sveObrazovneUstanove = ObrazovnaUstanova.dohvatiSveObrazovneUstanove("dat/obrazovneUstanove.txt");
		
		System.out.println();
		
		Integer brojUstanova = izracunajBrojUstanova();

		for (int i = 0; i < brojUstanova; i++) {

			List<Profesor> profesori = new ArrayList<>();
			List<Predmet> predmeti = new ArrayList<>();
			List<Student> studenti = new ArrayList<>();
			List<Ispit> ispitniRokovi = new ArrayList<>();
			Map<Profesor, List<Predmet>> mapaProfesora = new HashMap<>();
			Set<Profesor> keys = mapaProfesora.keySet();

			profesori = Profesor.dohvatiProfesore(sviProfesori, BROJ_PROFESORA, i);

			studenti = Student.dohvatiStudente(sviStudenti, BROJ_STUDENATA, i);

			predmeti = Predmet.dohvatiPredmete(sviPredmeti, BROJ_PREDMETA, i, profesori, studenti);

			ispitniRokovi = Ispit.dohvatiIspite(sviIspiti, BROJ_ISPITNIH_ROKOVA, i, predmeti, studenti);

			// unos linija s podacima jednu obrazovnu ustanovu za jednu obrazovnu u listu
			// stringova
			Integer linenum = 1;
			final Integer brZapisa = 3;
			List<String> listaStringova = new ArrayList<>();
			
			for (String string : sveObrazovneUstanove) {
				if (linenum > (i * brZapisa) && linenum <= ((i + 1) * brZapisa)) {
					listaStringova.add(string);
				}
				++linenum;
			}

			// Dodavanje profesora i nj predmeta u mapu
			for (Predmet p : predmeti) {
				if (mapaProfesora.containsKey(p.getNositelj())) {
					mapaProfesora.get(p.getNositelj()).add(p);
				} else {
					List<Predmet> tempList = new ArrayList<>();
					tempList.add(p);
					mapaProfesora.put(p.getNositelj(), tempList);
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
				throw new PopisProfesoraException("Ne postoji profesor koji predaje više od jednog predmeta!");
			}

			// Ispis profesora i predmeta koje oni predaju
			Integer k;
			for (Profesor profesor : keys) {
				k = 0;
				System.out.println("Profesor " + profesor.getImePrezime() + " predaje sljedeæe predmete: ");
				for (Predmet predmet : mapaProfesora.get(profesor)) {
					System.out.println((++k) + ") " + predmet.getNaziv());
				}
			}

			// Sortiranje i ispis studenata upisanih na odreðeni predmet iz lista predmet
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

			int j = 0;
			long id = Long.parseLong(listaStringova.get(j));
			Integer odabir = Integer.parseInt(listaStringova.get(j + 1));
			String nazivUstanove = listaStringova.get(j + 2);

			switch (odabir) {
			case 1:
				sveuciliste.dodajObrazovnuUstanovu(VeleucilisteJave.unesiVeleucilisteJave(id, nazivUstanove, predmeti,
						profesori, studenti, ispitniRokovi, input, i));
				System.out.println();
				break;
			case 2:
				sveuciliste.dodajObrazovnuUstanovu(FakultetRacunarstva.unesiFakultetRacunarstva(id, nazivUstanove,
						predmeti, profesori, studenti, ispitniRokovi, input, i));
				System.out.println();
				break;
			default:
				break;
			}

		}
		
		System.out.println("Program završava s izvoðenjem.");

		// komparazor za usporedbu 2 obrazovne ustanove (da lambda za sorted bude
		// urednija)
		System.out.println("Sortirane obrazovne ustanove prema broju studenata: ");
		Comparator<ObrazovnaUstanova> comparator = (o1, o2) -> o1.getNazivUstanove().compareTo(o2.getNazivUstanove());
		comparator.thenComparing(
				(o1, o2) -> Integer.toString(o1.getBrojStudenata()).compareTo(Integer.toString(o2.getBrojStudenata())));

		// dohvaæanje obrazovnih ustanova za sortiranje
		// sortiranje obrazovnih ustanova
		List<ObrazovnaUstanova> sortObrazovneUstanove = sveuciliste.dohvatiObrazovneUstanove().stream()
				.sorted(comparator).collect(Collectors.toList());

		// ispis obrazovnih ustanova
		sortObrazovneUstanove.stream()
				.forEach(o -> System.out.println(o.getNazivUstanove() + ": " + o.getBrojStudenata() + " student(a)"));

		// serijalizacija obrazovnih ustanova
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SERIALIZATION_FILE_NAME))) {
			sveuciliste.dohvatiObrazovneUstanove().stream().forEach(obrazovna -> {
				try {
					out.writeObject(obrazovna);
				} catch (IOException e) {
					System.err.println(e);
				}
			});
		} catch (IOException ex) {
			System.err.println(ex);
		}

		logger.info("Program je završio s radom\n");

	}

	public static Integer izracunajBrojUstanova() {
		Integer brojUstanova = 0;
		Integer brojLinijaProf = 0;

		try (BufferedReader in = new BufferedReader(new FileReader("dat/profesori.txt"))) {
			@SuppressWarnings("unused")
			String line;
			while ((line = in.readLine()) != null) {
				brojLinijaProf += 1;
			}
		} catch (IOException e) {
			System.err.println(e);
		}
		brojUstanova = (brojLinijaProf / BROJ_PROFESORA) / 5;

		return brojUstanova;
	}

}
