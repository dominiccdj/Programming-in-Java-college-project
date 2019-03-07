package hr.java.vjezbe.glavnafx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import hr.java.vjezbe.util.DatotekeZapis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class IspitUnosController {

	private List<Ispit> listaIspita = new ArrayList<>();
	private List<Predmet> listaPredmeta = new ArrayList<>();
	private List<Student> listaStudenata = new ArrayList<>();
	private List<Profesor> listaProfesora = new ArrayList<>();

	@FXML
	private ComboBox<Predmet> predmetComboBox;

	@FXML
	private ComboBox<Student> studentComboBox;

	@FXML
	private ComboBox<Integer> ocjenaComboBox;

	@FXML
	private TextField vrijemeTextField;

	@FXML
	private DatePicker datumDatePicker;

	@FXML
	private Button unosButton;

	@FXML
	public void initialize() {
		try {
			listaStudenata = BazaPodataka.dohvatiStudentePremaKriterijima(new Student(0, "", "", "", null));
			listaStudenata.add(null);
		} catch (BazaPodatakaException e) {
			e.printStackTrace();
		}
		
		try {
			listaPredmeta = BazaPodataka.dohvatiPredmetePremaKriterijima(new Predmet(0, "", "", null, null));
			listaPredmeta.add(null);
		} catch (BazaPodatakaException e) {
			e.printStackTrace();
		}

		ObservableList<Predmet> obListPredmet = FXCollections.observableList(listaPredmeta);
		predmetComboBox.getItems().clear();
		predmetComboBox.setItems(obListPredmet);

		ObservableList<Student> obListStudent = FXCollections.observableList(listaStudenata);
		studentComboBox.getItems().clear();
		studentComboBox.setItems(obListStudent);

		ObservableList<Integer> obListOcjene = FXCollections.observableArrayList(null, 1, 2, 3, 4, 5);
		ocjenaComboBox.getItems().clear();
		ocjenaComboBox.setItems(obListOcjene);
	}

	@FXML
	private void unesiIspitniRok() {
		if (predmetComboBox.getSelectionModel().isEmpty() || studentComboBox.getSelectionModel().isEmpty()
				|| ocjenaComboBox.getSelectionModel().isEmpty() || vrijemeTextField.getText().isEmpty() || datumDatePicker.getValue() == null) {
			String poruka = "";

			if (predmetComboBox.getSelectionModel().isEmpty()) {
				poruka += "Predmet je obavezan unos!\n";
			}

			if (studentComboBox.getSelectionModel().isEmpty()) {
				poruka += "Student je obavezan unos!\n";
			}

			if (ocjenaComboBox.getSelectionModel().isEmpty()) {
				poruka += "Ocjen je obavezan unos!\n";
			}

			if (vrijemeTextField.getText().isEmpty()) {
				poruka += "Vrijeme ispita je obavezan unos!\n";
			}
			
			if (datumDatePicker.getValue() == null) {
				poruka += "Datum ispita je obavezan unos!\n";
			}

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Neispravan unos!");
			alert.setHeaderText("Neispravan unos!");
			alert.setContentText(poruka);
			alert.showAndWait();
		}
		
		else {
			
			String vrijeme = vrijemeTextField.getText();
			String datum = datumDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
			String datumIVrijemeString = datum + "T" + vrijeme;
			
			LocalDateTime datumIVrijeme = LocalDateTime.parse(datumIVrijemeString,
					DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm"));
			
			Ispit noviIspit = new Ispit(0, predmetComboBox.getSelectionModel().getSelectedItem(), studentComboBox.getSelectionModel().getSelectedItem(), ocjenaComboBox.getSelectionModel().getSelectedItem(), datumIVrijeme);
			
			
			try {
				BazaPodataka.spremiNoviIspit(noviIspit);
			} catch (BazaPodatakaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Uspjeh!");
			alert.setHeaderText("Uspjeh");
			alert.setContentText("Ispit iz predmeta " + predmetComboBox.getSelectionModel().getSelectedItem().getNaziv() + " uspješno unesen!");
			alert.showAndWait();
		}

	}
}
