package hr.java.vjezbe.glavnafx;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import hr.java.vjezbe.util.DatotekeZapis;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class StudentUnosController {

	@FXML
	private TextField jmbagTextField;

	@FXML
	private TextField prezimeTextField;

	@FXML
	private TextField imeTextField;

	@FXML
	private DatePicker datumRodjenjaDatePicker;

	@FXML
	private Button unosButton;

	@FXML
	public void initialize() {}

	@FXML
	public void unesiStudenta() {
		if (jmbagTextField.getText().isEmpty() || prezimeTextField.getText().isEmpty()
				|| imeTextField.getText().isEmpty() || datumRodjenjaDatePicker.getValue() == null) {
			String poruka = "";

			if (jmbagTextField.getText().isEmpty()) {
				poruka += "JMBAG je obavezan unos!\n";
			}

			if (prezimeTextField.getText().isEmpty()) {
				poruka += "Prezime je obavezan unos!\n";
			}

			if (imeTextField.getText().isEmpty()) {
				poruka += "Ime je obavezan unos!\n";
			}

			if (datumRodjenjaDatePicker.getValue() == null) {
				poruka += "Datum ro�enja je obavezan unos!\n";
			}

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Neispravan unos!");
			alert.setHeaderText("Neispravan unos!");
			alert.setContentText(poruka);

			alert.showAndWait();
		}

		else {
			
			Student noviStudent = new Student(0, imeTextField.getText(),
					prezimeTextField.getText(), jmbagTextField.getText(), datumRodjenjaDatePicker.getValue());
			
			try {
				BazaPodataka.spremiNovogStudenta(noviStudent);
			} catch (BazaPodatakaException e) {
				e.getLocalizedMessage();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Uspjeh!");
			alert.setHeaderText("Uspjeh");
			alert.setContentText("Student " + imeTextField.getText() + " " + prezimeTextField.getText() + " uspješno unesen!");
			alert.showAndWait();
		}

	}
}
