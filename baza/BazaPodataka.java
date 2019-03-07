package hr.java.vjezbe.baza;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;

public class BazaPodataka {

	private static final String DATABASE_FILE = "dat/database.properties";
	private static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);

	private static Connection spajanjeNaBazu() throws SQLException, IOException {
		Properties svojstva = new Properties();

		svojstva.load(new FileReader(DATABASE_FILE));

		String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
		String korisnickoIme = svojstva.getProperty("korisnickoIme");
		String lozinka = svojstva.getProperty("lozinka");

		Connection veza = DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);
		return veza;

	}

	public static List<Profesor> dohvatiProfesorePremaKriterijima(Profesor profesor) throws BazaPodatakaException {
		List<Profesor> listaProfesora = new ArrayList<>();
		try (Connection veza = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT * FROM PROFESOR WHERE 1 = 1");
			if (Optional.ofNullable(profesor).isEmpty() == false) {
				/*
				 * if (Optional.ofNullable(profesor).map(Profesor::getId).isPresent()) {
				 * sqlUpit.append(" AND ID = " + profesor.getId()); }
				 */
				if (Optional.ofNullable(profesor.getSifra()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND SIFRA LIKE '%" + profesor.getSifra() + "%'");
				}
				if (Optional.ofNullable(profesor.getIme()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND IME LIKE '%" + profesor.getIme() + "%'");
				}
				if (Optional.ofNullable(profesor.getPrezime()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND PREZIME LIKE '%" + profesor.getPrezime() + "%'");
				}
				if (Optional.ofNullable(profesor.getTitula()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND TITULA LIKE '%" + profesor.getTitula() + "%'");
				}
			}
			Statement upit = veza.createStatement();
			ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {

				Long id = resultSet.getLong("ID");
				String sifra = resultSet.getString("SIFRA");
				String ime = resultSet.getString("IME");
				String prezime = resultSet.getString("PREZIME");
				String titula = resultSet.getString("TITULA");
				Profesor noviProfesor = new Profesor(id, sifra, ime, prezime, titula);
				listaProfesora.add(noviProfesor);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaProfesora;
	}

	public static List<Predmet> dohvatiPredmetePremaKriterijima(Predmet predmet) throws BazaPodatakaException {

		List<Predmet> listaPredmeta = new ArrayList<>();

		// dohvat svih profesora
		List<Profesor> listaProfesora = new ArrayList<Profesor>();
		listaProfesora = dohvatiProfesorePremaKriterijima(new Profesor(0, "", "", "", ""));

		try (Connection veza = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT * FROM PREDMET WHERE 1 = 1");
			if (Optional.ofNullable(predmet).isEmpty() == false) {

				if (Optional.ofNullable(predmet.getSifra()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND SIFRA LIKE '%" + predmet.getSifra() + "%'");
				}
				if (Optional.ofNullable(predmet.getNaziv()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND NAZIV LIKE '%" + predmet.getNaziv() + "%'");
				}
				if (Optional.ofNullable(predmet).map(Predmet::getBrojEctsBodova).isPresent()) {
					sqlUpit.append(" AND BROJ_ECTS_BODOVA LIKE '%" + predmet.getBrojEctsBodova() + "%'");
				}
				if (Optional.ofNullable(predmet).map(Predmet::getNositelj).isPresent()) {
					sqlUpit.append(" AND PROFESOR_ID LIKE '%" + predmet.getNositelj().getId() + "%'");
				}

			}
			Statement upit = veza.createStatement();
			ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {

				Long id = resultSet.getLong("ID");
				String sifra = resultSet.getString("SIFRA");
				String naziv = resultSet.getString("NAZIV");
				Integer brojEctsBodova = resultSet.getInt("BROJ_ECTS_BODOVA");
				Integer idProfesora = resultSet.getInt("PROFESOR_ID");

				Profesor nositelj = listaProfesora.stream().filter(p -> p.getId() == idProfesora).findFirst().get();

				Predmet noviPredmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj);
				listaPredmeta.add(noviPredmet);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaPredmeta;
	}

	public static List<Ispit> dohvatiIspitaPremaKriterijima(Ispit ispit) throws BazaPodatakaException {

		List<Ispit> listaIspita = new ArrayList<>();

		// dohvat svih predmeta
		List<Predmet> listaPredmeta = new ArrayList<Predmet>();
		listaPredmeta = dohvatiPredmetePremaKriterijima(new Predmet(0, "", "", null, null));

		// dohvat svih studenata
		List<Student> listaStudenata = new ArrayList<Student>();
		listaStudenata = dohvatiStudentePremaKriterijima(new Student(0, "", "", "", null));

		try (Connection veza = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT * FROM ISPIT WHERE 1 = 1");

			if (Optional.ofNullable(ispit).isEmpty() == false) {

				if (Optional.ofNullable(ispit.getPredmet()).isPresent()) {
					sqlUpit.append(" AND PREDMET_ID LIKE '%" + ispit.getPredmet().getId() + "%'");
				}
				if (Optional.ofNullable(ispit.getStudent()).isPresent()) {
					sqlUpit.append(" AND STUDENT_ID LIKE '%" + ispit.getStudent().getId() + "%'");
				}
				if (Optional.ofNullable(ispit.getOcjena()).isPresent()) {
					sqlUpit.append(" AND OCJENA LIKE '%" + ispit.getOcjena() + "%'");
				}
				if (Optional.ofNullable(ispit.getDatumIVrijeme()).isPresent()) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
					sqlUpit.append(" AND DATUM_I_VRIJEME = '" + ispit.getDatumIVrijeme().format(formatter) + "'");
				}

			}
			Statement upit = veza.createStatement();
			ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {

				Long id = resultSet.getLong("ID");
				Integer predmetId = resultSet.getInt("PREDMET_ID");
				Integer studentId = resultSet.getInt("STUDENT_ID");
				Integer ocjena = resultSet.getInt("OCJENA");
				LocalDateTime datumIVrijemeIspita = resultSet.getTimestamp("DATUM_I_VRIJEME").toLocalDateTime();

				// Profesor nositelj = listaProfesora.stream().filter(p -> p.getId() ==
				// idProfesora).findFirst().get();

				Predmet predmet = listaPredmeta.stream().filter(p -> p.getId() == predmetId).findFirst().get();

				Student student = listaStudenata.stream().filter(s -> s.getId() == studentId).findFirst().get();

				Ispit noviIspit = new Ispit(id, predmet, student, ocjena, datumIVrijemeIspita);

				listaIspita.add(noviIspit);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaIspita;
	}

	public static List<Student> dohvatiStudentePremaKriterijima(Student student) throws BazaPodatakaException {

		List<Student> listaStudenata = new ArrayList<>();
		try (Connection veza = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT * FROM STUDENT WHERE 1 = 1");
			if (Optional.ofNullable(student).isEmpty() == false) {
				/*
				 * if (Optional.ofNullable(profesor).map(Profesor::getId).isPresent()) {
				 * sqlUpit.append(" AND ID = " + profesor.getId()); }
				 */
				if (Optional.ofNullable(student.getIme()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND IME LIKE '%" + student.getIme() + "%'");
				}
				if (Optional.ofNullable(student.getPrezime()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND PREZIME LIKE '%" + student.getPrezime() + "%'");
				}
				if (Optional.ofNullable(student.getJmbag()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND JMBAG LIKE '%" + student.getJmbag() + "%'");
				}
				if (Optional.ofNullable(student.getDatumRodjenja()).isPresent()) {
					sqlUpit.append(" AND DATUM_RODJENJA = '"
							+ student.getDatumRodjenja().format(DateTimeFormatter.ISO_DATE) + "'");
				}
			}
			Statement upit = veza.createStatement();
			ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {

				Long id = resultSet.getLong("ID");
				String ime = resultSet.getString("IME");
				String prezime = resultSet.getString("PREZIME");
				String jmbag = resultSet.getString("JMBAG");
				LocalDate datumRodjenja = resultSet.getTimestamp("DATUM_RODJENJA").toInstant()
						.atZone(ZoneId.systemDefault()).toLocalDate();
				Student noviStudent = new Student(id, ime, prezime, jmbag, datumRodjenja);
				listaStudenata.add(noviStudent);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaStudenata;
	}

	public static List<Student> dohvatiStudenteRodjeneDanas() throws BazaPodatakaException {

		Student student = new Student(0, "", "", "", LocalDate.now());
				
		List<Student> listaStudenata = new ArrayList<>();
		try (Connection veza = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT * FROM STUDENT WHERE 1 = 1");
			if (Optional.ofNullable(student).isEmpty() == false) {
				/*
				 * if (Optional.ofNullable(profesor).map(Profesor::getId).isPresent()) {
				 * sqlUpit.append(" AND ID = " + profesor.getId()); }
				 */
				if (Optional.ofNullable(student.getIme()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND IME LIKE '%" + student.getIme() + "%'");
				}
				if (Optional.ofNullable(student.getPrezime()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND PREZIME LIKE '%" + student.getPrezime() + "%'");
				}
				if (Optional.ofNullable(student.getJmbag()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND JMBAG LIKE '%" + student.getJmbag() + "%'");
				}
				if (Optional.ofNullable(student.getDatumRodjenja()).isPresent()) {
					sqlUpit.append(" AND DATUM_RODJENJA = '"
							+ student.getDatumRodjenja().format(DateTimeFormatter.ISO_DATE) + "'");
				}
			}
			Statement upit = veza.createStatement();
			ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {

				Long id = resultSet.getLong("ID");
				String ime = resultSet.getString("IME");
				String prezime = resultSet.getString("PREZIME");
				String jmbag = resultSet.getString("JMBAG");
				LocalDate datumRodjenja = resultSet.getTimestamp("DATUM_RODJENJA").toInstant()
						.atZone(ZoneId.systemDefault()).toLocalDate();
				Student noviStudent = new Student(id, ime, prezime, jmbag, datumRodjenja);
				listaStudenata.add(noviStudent);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaStudenata;
	}
	
	
	public static void spremiNovogProfesora(Profesor profesor) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("INSERT INTO PROFESOR(ime, prezime, sifra, titula) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, profesor.getIme());
			preparedStatement.setString(2, profesor.getPrezime());
			preparedStatement.setString(3, profesor.getSifra());
			preparedStatement.setString(4, profesor.getTitula());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void spremiNoviPredmet(Predmet predmet) throws BazaPodatakaException {

		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza.prepareStatement(
					"INSERT INTO PREDMET(SIFRA, NAZIV, BROJ_ECTS_BODOVA, PROFESOR_ID) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, predmet.getSifra());
			preparedStatement.setString(2, predmet.getNaziv());
			preparedStatement.setInt(3, predmet.getBrojEctsBodova());
			preparedStatement.setInt(4, (int) (long) predmet.getNositelj().getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void spremiNoviIspit(Ispit ispit) throws BazaPodatakaException {

		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza.prepareStatement(
					"INSERT INTO ISPIT(PREDMET_ID, STUDENT_ID, OCJENA, DATUM_I_VRIJEME) VALUES (?, ?, ?, ?)");
			preparedStatement.setLong(1, ispit.getPredmet().getId());
			preparedStatement.setLong(2, ispit.getStudent().getId());
			preparedStatement.setInt(3, ispit.getOcjena());
			preparedStatement.setTimestamp(4, Timestamp.valueOf(ispit.getDatumIVrijeme()));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void spremiNovogStudenta(Student student) throws BazaPodatakaException {

		try (Connection veza = spajanjeNaBazu()) {

			PreparedStatement preparedStatement = veza
					.prepareStatement("INSERT INTO STUDENT(ime, prezime, jmbag, datum_rodjenja) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, student.getIme());
			preparedStatement.setString(2, student.getPrezime());
			preparedStatement.setString(3, student.getJmbag());
			preparedStatement.setDate(4, Date.valueOf(student.getDatumRodjenja()));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}

	}

}
