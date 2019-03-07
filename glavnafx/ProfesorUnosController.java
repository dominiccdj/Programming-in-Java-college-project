package hr.java.vjezbe.glavnafx;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import hr.java.vjezbe.util.DatotekeZapis;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ProfesorUnosController {

	@FXML
	private TextField sifraTextField;

	@FXML
	private TextField prezimeTextField;

	@FXML
	private TextField imeTextField;

	@FXML
	private TextField titulaTextField;

	@FXML
	private Button unosButton;

	@FXML
	public void initialize() {}

	@FXML
	public void unesiProfesora() {

		if (sifraTextField.getText().isEmpty() || prezimeTextField.getText().isEmpty()
				|| imeTextField.getText().isEmpty() || titulaTextField.getText().isEmpty()) {

			String poruka = "";

			if (sifraTextField.getText().isEmpty()) {
				poruka += "Sifra je obavezan unos!\n";
			}

			if (prezimeTextField.getText().isEmpty()) {
				poruka += "Prezime je obavezan unos!\n";
			}

			if (imeTextField.getText().isEmpty()) {
				poruka += "Ime je obavezan unos!\n";
			}

			if (titulaTextField.getText().isEmpty()) {
				poruka += "Titula je obavezan unos!\n";
			}

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Neispravan unos!");
			alert.setHeaderText("Neispravan unos!");
			alert.setContentText(poruka);
			alert.showAndWait();
			
		} else {
			
			Profesor noviProfesor = new Profesor(0, sifraTextField.getText(),
					imeTextField.getText(), prezimeTextField.getText(), titulaTextField.getText());
			
			try {
				BazaPodataka.spremiNovogProfesora(noviProfesor);
			} catch (BazaPodatakaException e) {
				e.getMessage();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Uspjeh!");
			alert.setHeaderText("Uspjeh");
			alert.setContentText("Profesor " + imeTextField.getText() + " " + prezimeTextField.getText() + " uspješno unesen!");
			alert.showAndWait();
		}
	}

}
