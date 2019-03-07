package hr.java.vjezbe.glavnafx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class PredmetController {

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
	private Button pretragaButton;

	@FXML
	private TableView<Predmet> predmetTableView;

	@FXML
	private TableColumn<Predmet, String> sifraColumn;
	@FXML
	private TableColumn<Predmet, String> nazivColumn;
	@FXML
	private TableColumn<Predmet, String> brEctsColumn;
	@FXML
	private TableColumn<Predmet, String> nositeljColumn;
	
	@FXML
	public void initialize() {
		sifraColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Predmet, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Predmet, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getSifra().toString());
					}
				});

		nazivColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Predmet, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Predmet, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getNaziv().toString());
					}
				});

		brEctsColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Predmet, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Predmet, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getBrojEctsBodova().toString());
					}
				});

		nositeljColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Predmet, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Predmet, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getNositelj().getImePrezime().toString());
					}
				});

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

	public void prikaziPredmete() {
		List<Predmet> filtriraniPredmeti = new ArrayList<>();
		
		Integer brojEctsBodova = null;
		String sifra = "", naziv = "";
		Profesor nositelj = null;
		
		if (sifraTextField.getText().isEmpty() == false) {
			sifra += sifraTextField.getText();
		}

		if (nazivTextField.getText().isEmpty() == false) {
			naziv += nazivTextField.getText();

		}
		
		if (brEctsTextField.getText().isEmpty() == false) {
			brojEctsBodova = Integer.valueOf(brEctsTextField.getText());
			
		}

		if (nositeljComboBox.getSelectionModel().isEmpty() == false) {
			nositelj = nositeljComboBox.getSelectionModel().getSelectedItem();
		}
		
		Predmet noviPredmet = new Predmet(0, sifra, naziv, brojEctsBodova, nositelj);
		
		try {
			filtriraniPredmeti = BazaPodataka.dohvatiPredmetePremaKriterijima(noviPredmet);
		} catch (BazaPodatakaException e) {
			e.printStackTrace();
		}

		ObservableList<Predmet> listaPredmeta = FXCollections.observableArrayList(filtriraniPredmeti);
		predmetTableView.setItems(listaPredmeta);
	}
}
