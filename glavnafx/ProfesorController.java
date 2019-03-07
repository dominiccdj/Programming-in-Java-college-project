package hr.java.vjezbe.glavnafx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class ProfesorController {

	@FXML
	private TextField sifraTextField;

	@FXML
	private TextField prezimeTextField;

	@FXML
	private TextField imeTextField;

	@FXML
	private TextField titulaTextField;

	@FXML
	private Button pretragaButton;

	@FXML
	private TableView<Profesor> profesorTableView;

	@FXML
	private TableColumn<Profesor, String> sifraColumn;
	@FXML
	private TableColumn<Profesor, String> prezimeColumn;
	@FXML
	private TableColumn<Profesor, String> imeColumn;
	@FXML
	private TableColumn<Profesor, String> titulaColumn;

	@FXML
	public void initialize() {
		sifraColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Profesor, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Profesor, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getSifra().toString());
					}
				});

		prezimeColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Profesor, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Profesor, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getPrezime().toString());
					}
				});

		imeColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Profesor, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Profesor, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getIme().toString());
					}
				});

		titulaColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Profesor, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Profesor, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getTitula().toString());
					}
				});


	}

	public void prikaziProfesore() {
		
		String sifra = "", ime = "", prezime = "", titula = "";
		List<Profesor> filtriraniProfesori = new ArrayList<Profesor>();
		
		if (sifraTextField.getText().isEmpty() == false) {
			sifra += sifraTextField.getText();
		}

		if (prezimeTextField.getText().isEmpty() == false) {
			prezime += prezimeTextField.getText();

		}
		
		if (imeTextField.getText().isEmpty() == false) {
			ime += imeTextField.getText();
			
		}
		
		if (titulaTextField.getText().isEmpty() == false) {
			titula += titulaTextField.getText();
			
		}
		
		Profesor noviProfesor = new Profesor(0, sifra, ime, prezime, titula);
		try {
			filtriraniProfesori = BazaPodataka.dohvatiProfesorePremaKriterijima(noviProfesor);
		} catch (BazaPodatakaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObservableList<Profesor> listaProfesora = FXCollections.observableArrayList(filtriraniProfesori);
		profesorTableView.setItems(listaProfesora);
	}

}
