package hr.java.vjezbe.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;

public class Datoteke {

	public static List<Profesor> dohvatiProfesore() {

		List<String> sviProfesori = new ArrayList<>();

		// unos linija s podacima jednog profesora za jednu obrazovnu u listu stringova
		try (BufferedReader in = new BufferedReader(new FileReader("dat/profesori.txt"))) {
			String line;

			while ((line = in.readLine()) != null) {

				sviProfesori.add(line);

			}
		} catch (IOException e) {
			System.err.println(e);
		}

		List<Profesor> profesori = new ArrayList<>();

		final Integer brZapisa = 5;
		final Integer brProfesora = sviProfesori.size() / brZapisa;

		for (int i = 0; i < brProfesora * brZapisa; i += brZapisa) {
			long id = Long.parseLong(sviProfesori.get(i));
			String sifra = sviProfesori.get(i + 1);
			String ime = sviProfesori.get(i + 2);
			String prezime = sviProfesori.get(i + 3);
			String titula = sviProfesori.get(i + 4);

			Profesor temp = new Profesor(id, sifra, ime, prezime, titula);
			profesori.add(temp);
		}

		return profesori;

	}

	public static List<Student> dohvatiStudente() {

		List<String> sviStudenti = new ArrayList<>();

		// unos linija s podacima jednog profesora za jednu obrazovnu u listu stringova
		try (BufferedReader in = new BufferedReader(new FileReader("dat/studenti.txt"))) {
			String line;

			while ((line = in.readLine()) != null) {

				sviStudenti.add(line);

			}
		} catch (IOException e) {
			System.err.println(e);
		}

		List<Student> studenti = new ArrayList<>();

		final Integer brZapisa = 5;
		final Integer brStudenata = sviStudenti.size() / brZapisa;

		for (int i = 0; i < brStudenata * brZapisa; i += brZapisa) {
			long id = Long.parseLong(sviStudenti.get(i));
			String ime = sviStudenti.get(i + 1);
			String prezime = sviStudenti.get(i + 2);
			String jmbag = sviStudenti.get(i + 3);
			String datumRodjenjaString = sviStudenti.get(i + 4);
			LocalDate datumRodjenja = LocalDate.parse(datumRodjenjaString, DateTimeFormatter.ofPattern("dd.MM.yyyy."));

			Student temp = new Student(id, ime, prezime, jmbag, datumRodjenja);
			studenti.add(temp);
		}

		return studenti;

	}

	public static List<Predmet> dohvatiPredmete(List<Student> studenti, List<Profesor> profesori) {

		List<String> sviPredmeti = new ArrayList<>();

		// unos linija s podacima jednog predmeta za jednu obrazovnu u listu stringova
		try (BufferedReader in = new BufferedReader(new FileReader("dat/predmeti.txt"))) {
			String line;

			while ((line = in.readLine()) != null) {

				sviPredmeti.add(line);

			}
		} catch (IOException e) {
			System.err.println(e);
		}

		List<Predmet> predmeti = new ArrayList<>();

		final Integer brZapisa = 6;
		final Integer brStudenata = sviPredmeti.size() / brZapisa;

		for (int i = 0; i < brStudenata * brZapisa; i += brZapisa) {
			long id = Long.parseLong(sviPredmeti.get(i));
			String sifra = sviPredmeti.get(i + 1);
			String ime = sviPredmeti.get(i + 2);
			Integer brojEctsBodova = Integer.parseInt(sviPredmeti.get(i + 3));
			Integer idProfesora = Integer.parseInt(sviPredmeti.get(i + 4));
			String ideviStudenata = sviPredmeti.get(i + 5);
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

	public static List<Ispit> dohvatiIspite(List<Student> studenti, List<Predmet> predmeti) {

		List<String> sviIspiti = new ArrayList<>();

		// unos linija s podacima jednog predmeta za jednu obrazovnu u listu stringova
		try (BufferedReader in = new BufferedReader(new FileReader("dat/ispitniRokovi.txt"))) {
			String line;

			while ((line = in.readLine()) != null) {

				sviIspiti.add(line);

			}
		} catch (IOException e) {
			System.err.println(e);
		}

		List<Ispit> ispiti = new ArrayList<>();

		final Integer brZapisa = 5;
		final Integer brStudenata = sviIspiti.size() / brZapisa;

		for (int i = 0; i < brStudenata * brZapisa; i += brZapisa) {
			Predmet izabraniPredmet = null;
			Student izabraniStudent = null;

			long id = Long.parseLong(sviIspiti.get(i));
			Long predmetIndex = (long) Integer.parseInt(sviIspiti.get(i + 1));
			Long studentIndex = (long) Integer.parseInt(sviIspiti.get(i + 2));
			Integer ocjena = Integer.parseInt(sviIspiti.get(i + 3));
			String datumIVrijemeString = sviIspiti.get(i + 4);

			for (Predmet predmet : predmeti) {
				if (predmetIndex == predmet.getId()) {
					izabraniPredmet = predmet;
				}
			}

			for (Student student : studenti) {
				if (studentIndex == student.getId()) {
					izabraniStudent = student;
				}
			}

			LocalDateTime datumIVrijeme = LocalDateTime.parse(datumIVrijemeString,
					DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm"));

			Ispit temp = new Ispit(id, izabraniPredmet, izabraniStudent, ocjena, datumIVrijeme);
			ispiti.add(temp);
		}

		return ispiti;

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
