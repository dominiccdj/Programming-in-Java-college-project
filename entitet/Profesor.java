package hr.java.vjezbe.entitet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.java.vjezbe.glavna.Glavna;

/**
 * Predstavlja profesora koji ima svoju šifru, titulu i ostale pojedinosti
 * "Osobe"
 * 
 * @see Osoba
 * @author domin
 * 
 *
 */
/**
 * @author domin
 *
 */
/**
 * @author domin
 *
 */
public class Profesor extends Osoba implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7968209901391095903L;
	private String sifra;
	private String titula;

	public Profesor(long id, String sifra, String ime, String prezime, String titula) {
		super(id, ime, prezime);
		this.sifra = sifra;
		this.titula = titula;
	}

	/**
	 * Dohvaæa šifru profesora
	 * 
	 * @return šifra profesora
	 */
	public String getSifra() {
		return sifra;
	}

	/**
	 * Postavlja šifru profesora
	 * 
	 * @param sifra šifra profesora
	 */
	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	/**
	 * Dohvaæa titulu profesora
	 * 
	 * @return titula titula profesora
	 */
	public String getTitula() {
		return titula;
	}

	/**
	 * Postavlja titulu profesora
	 * 
	 * @param titula titula profesora
	 */
	public void setTitula(String titula) {
		this.titula = titula;
	}

	@Override
	public String toString() {
		return sifra + " " + titula + " " + getImePrezime();
	}

	/**
	 * Unosi profesora. Korisnik unosi šifru, ime, prezime i titulu profesora.
	 * 
	 * @param input Scanner za unos s tipkovnice
	 * @return unešen profesor
	 */
	public static Profesor unesiProfesora(Scanner input) {
		long id = Glavna.unesiInteger("Unesite ID profesora: ", input);

		System.out.print("Unesite šifru profesora: ");
		String sifra = input.nextLine();

		System.out.print("Unesite ime profesora: ");
		String ime = input.nextLine();

		System.out.print("Unesite prezime profesora: ");
		String prezime = input.nextLine();

		System.out.print("Unesite titulu profesora: ");
		String titula = input.nextLine();

		return new Profesor(id, sifra, ime, prezime, titula);
	}

	public static List<Profesor> dohvatiProfesore(List<String> sviProfesori, Integer brojProfesora,
			Integer brUstanove) {

		List<Profesor> profesori = new ArrayList<>();
		List<String> listaStringova = new ArrayList<>();
		Integer linenum = 1;
		final Integer brZapisa = 5;

		for (String string : sviProfesori) {
			if (linenum > (brUstanove * brojProfesora * brZapisa) && linenum <= ((brUstanove+1) * brojProfesora * brZapisa)) {
				listaStringova.add(string);
			}
			++linenum;
		}

		for (int i = 0; i < brojProfesora * brZapisa; i += brZapisa) {
			long id = Long.parseLong(listaStringova.get(i));
			String sifra = listaStringova.get(i + 1);
			String ime = listaStringova.get(i + 2);
			String prezime = listaStringova.get(i + 3);
			String titula = listaStringova.get(i + 4);

			Profesor temp = new Profesor(id, sifra, ime, prezime, titula);
			profesori.add(temp);
		}

		return profesori;

	}

	public static List<String> dohvatiSveProfesore(String filePath) {

		List<String> listaStringova = new ArrayList<>();

		System.out.println("Uèitavanje profesora…");

		// unos linija s podacima jednog profesora za jednu obrazovnu u listu stringova
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
