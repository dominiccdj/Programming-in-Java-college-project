package hr.java.vjezbe.glavnafx;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class IspitController {

	private List<Ispit> listaIspita = new ArrayList<>();
	private List<Predmet> listaPredmeta = new ArrayList<>();
	private List<Student> listaStudenata = new ArrayList<>();

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
	private Button pretragaButton;

	@FXML
	private TableView<Ispit> ispitTableView;

	@FXML
	private TableColumn<Ispit, String> nazivPredmetaColumn;
	@FXML
	private TableColumn<Ispit, String> studentColumn;
	@FXML
	private TableColumn<Ispit, String> ocjenaColumn;
	@FXML
	private TableColumn<Ispit, String> datumVrijemeColumn;

	@FXML
	public void initialize() {
		nazivPredmetaColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Ispit, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getPredmet().getNaziv().toString());
					}
				});

		studentColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Ispit, String> param) {
						return new ReadOnlyObjectWrapper<String>(
								param.getValue().getStudent().getImePrezime().toString());
					}
				});

		ocjenaColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Ispit, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getOcjena().toString());
					}
				});

		datumVrijemeColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Ispit, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getDatumIVrijeme()
								.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")));
					}
				});

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

	public void prikaziIspite() {
		List<Ispit> filtriraniIspiti = new ArrayList<>();

		Predmet predmet = null;
		Student student = null;
		Integer ocjena = null;
		String vrijeme = "";
		LocalDate datum = null;
		
		String datumIVrijemeString = "";
		LocalDateTime datumIVrijeme = null;

		if (predmetComboBox.getSelectionModel().isEmpty() == false) {
			predmet = predmetComboBox.getSelectionModel().getSelectedItem();
		}

		if (studentComboBox.getSelectionModel().isEmpty() == false) {
			student = studentComboBox.getSelectionModel().getSelectedItem();
		}

		if (ocjenaComboBox.getSelectionModel().isEmpty() == false) {
			ocjena = ocjenaComboBox.getSelectionModel().getSelectedItem();
		}

		if (vrijemeTextField.getText().isEmpty() == false && datumDatePicker.getValue() != null) {
			vrijeme += vrijemeTextField.getText();
			datum = datumDatePicker.getValue();
			
			datumIVrijemeString += datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + "T" + vrijeme;
			datumIVrijeme = LocalDateTime.parse(datumIVrijemeString,
					DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm"));
		}
		
		Ispit noviIspit = new Ispit(0, predmet, student, ocjena, datumIVrijeme);
		
		try {
			filtriraniIspiti = BazaPodataka.dohvatiIspitaPremaKriterijima(noviIspit);
		} catch (BazaPodatakaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObservableList<Ispit> listaIspita= FXCollections.observableArrayList(filtriraniIspiti);
		ispitTableView.setItems(listaIspita);
	}

}
