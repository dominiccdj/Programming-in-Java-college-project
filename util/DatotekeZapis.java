package hr.java.vjezbe.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;

public class DatotekeZapis {

	public static void zapisiProfesoreUDatoteku(List<Profesor> sviProfesori) {
		try (PrintWriter out = new PrintWriter(new FileWriter(new File("dat/profesori.txt")))) {
			for (Profesor profesor : sviProfesori) {
				out.println(profesor.getId());
				out.println(profesor.getSifra());
				out.println(profesor.getIme());
				out.println(profesor.getPrezime());
				out.println(profesor.getTitula());
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void zapisiStudenteUDatoteku(List<Student> sviStudenti) {
		try (PrintWriter out = new PrintWriter(new FileWriter(new File("dat/studenti.txt")))) {
			for (Student student : sviStudenti) {
				out.println(student.getId());
				out.println(student.getIme());
				out.println(student.getPrezime());
				out.println(student.getJmbag());
				out.println(student.getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void zapisiPredmeteUDatoteku(List<Predmet> sviPredmeti) {
		try (PrintWriter out = new PrintWriter(new FileWriter(new File("dat/predmeti.txt")))) {
			for (Predmet predmet : sviPredmeti) {
				out.println(predmet.getId());
				out.println(predmet.getSifra());
				out.println(predmet.getNaziv());
				out.println(predmet.getBrojEctsBodova());
				out.println(predmet.getNositelj().getId());
				
				if (predmet.getPopisStudenata().isEmpty() == false) {
					predmet.getPopisStudenata().stream().forEach(s -> out.print(s.getId()+ " "));
					out.println("");
				}
				else {
					out.println("");
				}
				
				
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void zapisiIspiteUDatoteku(List<Ispit> sviIspiti) {
		try (PrintWriter out = new PrintWriter(new FileWriter(new File("dat/ispitniRokovi.txt")))) {
			for (Ispit ispit : sviIspiti) {
				out.println(ispit.getId());
				out.println(ispit.getPredmet().getId());
				out.println(ispit.getStudent().getId());
				out.println(ispit.getOcjena());
				out.println(ispit.getDatumIVrijeme().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm")));
			}
				
				
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
