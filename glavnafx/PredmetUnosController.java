package hr.java.vjezbe.glavnafx;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
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
import javafx.scene.control.TextField;

public class PredmetUnosController {

	private List<Predmet> listaPredmeta = new ArrayList<>();
	private List<Profesor> listaProfesora = new ArrayList<>();

	@FXML
	private TextField sifraTextField;

	@FXML
	private TextField nazivTextField;

	@FXML
	private TextField brEctsTextField;

	@FXML
	private ComboBox<Profesor> nositeljComboBox;

	@FXML
	private Button unosButton;

	@FXML
	public void initialize() {

		try {
			listaProfesora = BazaPodataka.dohvatiProfesorePremaKriterijima(new Profesor(0, "", "", "", ""));
		} catch (BazaPodatakaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObservableList<Profesor> obListProfesor = FXCollections.observableList(listaProfesora);
		nositeljComboBox.getItems().clear();
		nositeljComboBox.setItems(obListProfesor);

	}

	@FXML
	private void unesiPredmet() {
		if (sifraTextField.getText().isEmpty() || nazivTextField.getText().isEmpty()
				|| brEctsTextField.getText().isEmpty() || nositeljComboBox.getSelectionModel().isEmpty()) {
			String poruka = "";

			if (sifraTextField.getText().isEmpty()) {
				poruka += "Šifra je obavezan unos!\n";
			}

			if (nazivTextField.getText().isEmpty()) {
				poruka += "Naziv je obavezan unos!\n";
			}

			if (brEctsTextField.getText().isEmpty()) {
				poruka += "Broj ECTS bodova je obavezan unos!\n";
			}

			if (nositeljComboBox.getSelectionModel().isEmpty()) {
				poruka += "Nositelj predmeta je obavezan unos!\n";
			}

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Neispravan unos!");
			alert.setHeaderText("Neispravan unos!");
			alert.setContentText(poruka);
			alert.showAndWait();
		}

		else {
			OptionalLong maksimalniId = listaPredmeta.stream().mapToLong(p -> p.getId()).max();

			Predmet noviPredmet = new Predmet(0, sifraTextField.getText(),
					nazivTextField.getText(), Integer.parseInt(brEctsTextField.getText()),
					nositeljComboBox.getSelectionModel().getSelectedItem());
			
			try {
				BazaPodataka.spremiNoviPredmet(noviPredmet);
			} catch (BazaPodatakaException e) {
				e.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Uspjeh!");
			alert.setHeaderText("Uspjeh");
			alert.setContentText("Predmet " + nazivTextField.getText() + " uspješno unesen!");
			alert.showAndWait();
		}

	}
}
