package hr.java.vjezbe.entitet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.glavna.Glavna;

/**
 * Predstavlja ispit, ima pripadni predmet, studenta koji ga je polagao, ocjenu
 * koju je student dobio na tom ispitu te datum i vrijeme pisanja ispita
 * 
 * @author domin
 */
public class Ispit extends Entitet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1470895886051177045L;
	private Predmet predmet;
	private Student student;
	private Integer ocjena;
	private LocalDateTime datumIVrijeme;
	private static final Logger logger = LoggerFactory.getLogger(Ispit.class);

	public Ispit(long id, Predmet predmet, Student student, Integer ocjena, LocalDateTime datumIVrijeme) {
		super(id);
		this.predmet = predmet;
		this.student = student;
		this.ocjena = ocjena;
		this.datumIVrijeme = datumIVrijeme;
	}

	/**
	 * Dohva�a predmet iz kojeg je pisan ispit
	 * 
	 * @return predmet iz kojeg je pisan ispit
	 */
	public Predmet getPredmet() {
		return predmet;
	}

	/**
	 * Postavlja predmet iz kojeg je pisan ispit
	 * 
	 * @param predmet iz kojeg je pisan ispit
	 */
	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

	/**
	 * Dohva�a studenta koji je pisao ispit
	 * 
	 * @return student koji je pisao ispit
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * Postavlja studenta koji je pisao ispit
	 * 
	 * @param student koji je pisao ispit
	 */
	public void setStudent(Student student) {
		this.student = student;
	}

	/**
	 * Dohva�a ocjenu iz ispita
	 * 
	 * @return ocjena iz ispita
	 */
	public Integer getOcjena() {
		return ocjena;
	}

	/**
	 * Postavlja ocjenu iz ispita
	 * 
	 * @param ocjena iz ispita
	 */
	public void setOcjena(Integer ocjena) {
		this.ocjena = ocjena;
	}

	/**
	 * Dohva�a datum i vrijeme u koje je pisan ispit
	 * 
	 * @return datum i vrijeme u koje je pisan ispit
	 */
	public LocalDateTime getDatumIVrijeme() {
		return datumIVrijeme;
	}

	/**
	 * Postavlja datum i vrijeme u koje je pisan ispit u formatu "dd.MM.yyyy.Thh:mm"
	 * 
	 * @param datumIVrijeme datum i vrijeme u koje je pisan ispit
	 */
	public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
		this.datumIVrijeme = datumIVrijeme;
	}
	
	

	@Override
	public String toString() {
		return "Ispit [predmet=" + predmet + ", student=" + student + ", ocjena=" + ocjena;
	}

	/**
	 * Unosi ispitni rok, korisnik bira iz kojeg predmeta, koji student je pisao te
	 * unosi koju je ocjenu student dobio i datum/vrijeme pisanja ispita
	 * 
	 * @param input    Scanner za unos s tipkovnice
	 * @param predmeti niz svih dstupnih predmeta
	 * @param studenti niz svih dostupnih studenata
	 * @return une�en ispitni rok
	 */
	public static Ispit unesiIspitniRok(Scanner input, List<Predmet> predmeti, List<Student> studenti) {

		boolean nastaviPetlju = false;
		Integer odabirPredmeta = 0;
		Integer odabirStudenta = 0;
		Integer ocjena = 0;
		Integer i = 0;
		LocalDateTime datumIVrijeme = null;

		long idIspita = Glavna.unesiOcjenu("Unesi ID ispita: ", input);

		// odabir predmeta iz arraya
		System.out.println("Odaberite predmet: ");
		i = 0;
		for (Predmet p : predmeti) {
			System.out.println((i + 1) + ". " + p.getNaziv());
			i++;
		}
		do {
			try {
				System.out.print("Odabir >> ");
				odabirPredmeta = input.nextInt();
				input.nextLine();
				nastaviPetlju = false;
			} catch (InputMismatchException ex1) {
				System.out.println("Morate unijeti broj�ane vrijednosti.");
				input.nextLine();
				nastaviPetlju = true;
				logger.info("Nije unesena broj�ana vrijednost! " + ex1);
			}
		} while (nastaviPetlju || (odabirPredmeta < 1 || odabirPredmeta > predmeti.size()));
		odabirPredmeta--; // zato sto ako je unos 2, to je element na broju 1 u arrayu

		// odabir studenta iz arraya
		System.out.println("Odaberite studenta: ");
		i = 0;
		for (Student s : studenti) {
			System.out.println((i + 1) + ". " + s.getIme() + " " + s.getPrezime());
			i++;
		}
		do {
			try {
				System.out.print("Odabir >> ");
				odabirStudenta = input.nextInt();
				input.nextLine();
				nastaviPetlju = false;
			} catch (InputMismatchException ex1) {
				System.out.println("Morate unijeti broj�ane vrijednosti.");
				input.nextLine();
				nastaviPetlju = true;
				logger.info("Nije unesena broj�ana vrijednost! " + ex1);
			}
		} while (nastaviPetlju || (odabirStudenta < 1 || odabirStudenta > studenti.size()));
		odabirStudenta--; // zato sto ako je unos 2, to je element na broju 1 u arrayu

		// Unos ocjene na ispitu
		ocjena = Glavna.unesiOcjenu("Unesite ocjenu na ispitu: ", input);

		// pretvorba ocjene u broju u ocjenu u tekstualnom obliku
		@SuppressWarnings("unused")
		String ocjenaString;
		switch (ocjena) {
		case 1:
			ocjenaString = Ocjena.NEDOVOLJAN.getOcjenaString();
			break;
		case 2:
			ocjenaString = Ocjena.DOVOLJAN.getOcjenaString();
			break;
		case 3:
			ocjenaString = Ocjena.DOBAR.getOcjenaString();
			break;
		case 4:
			ocjenaString = Ocjena.VRLO_DOBAR.getOcjenaString();
			break;
		case 5:
			ocjenaString = Ocjena.ODLICAN.getOcjenaString();
			break;
		default:
			ocjenaString = "Pogre�no unesena ocjena";
			break;
		}

		do {
			try {
				System.out.print("Unesite datum i vrijeme ispita u formatu (dd.MM.yyyy.THH:mm): ");
				String datumIVrijemeString = input.nextLine();
				datumIVrijeme = LocalDateTime.parse(datumIVrijemeString,
						DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm"));
				nastaviPetlju = false;
			} catch (Exception e) {
				System.out.println("Krivo une�en datum!");
				logger.info("Krivo une�en datum!" + e);
				nastaviPetlju = true;

			}
		} while (nastaviPetlju);

		System.out.println("Student " + studenti.get(odabirStudenta).getIme() + " " +
		studenti.get(odabirStudenta).getPrezime()
		+ " je ostvario ocjenu '" + ocjenaString + "' na predmetu '" +
		predmeti.get(odabirPredmeta).getNaziv()
		 + "'");

		return new Ispit(idIspita, predmeti.get(odabirPredmeta), studenti.get(odabirStudenta), ocjena, datumIVrijeme);
	}

	public static List<Ispit> dohvatiIspite(List<String> sviIspiti, Integer brojIspita, Integer brUstanove,
			List<Predmet> predmeti, List<Student> studenti) {

		List<Ispit> ispiti = new ArrayList<>();
		List<String> listaStringova = new ArrayList<>();
		Integer linenum = 1;
		final Integer brZapisa = 5;
		
		for (String string : sviIspiti) {
			if (linenum > (brUstanove * brojIspita * brZapisa)
					&& linenum <= ((brUstanove + 1) * brojIspita * brZapisa)) {
				listaStringova.add(string);
			}
			++linenum;
		}

		for (int i = 0; i < brojIspita * brZapisa; i += brZapisa) {

			Predmet izabraniPredmet = null;
			Student izabraniStudent = null;

			long id = Long.parseLong(listaStringova.get(i));
			Long predmetIndex = (long) Integer.parseInt(listaStringova.get(i + 1));
			Long studentIndex = (long) Integer.parseInt(listaStringova.get(i + 2));
			Integer ocjena = Integer.parseInt(listaStringova.get(i + 3));
			String datumIVrijemeString = listaStringova.get(i + 4);
			
			for (Predmet predmet : predmeti) {
				if (predmetIndex == predmet.getId()) {
					izabraniPredmet = predmet;
				}
			}

			for (Student student: studenti) {
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
	
	public static List<String> dohvatiSveIspite(String filePath) {

		List<String> listaStringova = new ArrayList<>();

		System.out.println("U�itavanje ispita�");

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
