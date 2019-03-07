package hr.java.vjezbe.glavnafx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class StudentController {

	private List<Student> listaStudenata = new ArrayList<>();

	@FXML
	private TextField jmbagTextField;

	@FXML
	private TextField prezimeTextField;

	@FXML
	private TextField imeTextField;

	@FXML
	private DatePicker datumRodjenjaDatePicker;

	@FXML
	private Button pretragaButton;
	
	@FXML
	private TableView<Student> studentTableView;

	@FXML
	private TableColumn<Student, String> jmbagColumn;
	@FXML
	private TableColumn<Student, String> prezimeColumn;
	@FXML
	private TableColumn<Student, String> imeColumn;
	@FXML
	private TableColumn<Student, String> datumRodjenjaColumn;

	@FXML
	public void initialize() {
		jmbagColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Student, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getJmbag().toString());
					}
				});

		prezimeColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Student, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getPrezime().toString());
					}
				});

		imeColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Student, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getIme().toString());
					}
				});

		datumRodjenjaColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Student, String> param) {
						return new ReadOnlyObjectWrapper<String>(param.getValue().getDatumRodjenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
					}
				});

		listaStudenata = Datoteke.dohvatiStudente();

	}
	
	public void prikaziStudente() {
		
		String ime = "", prezime = "", jmbag = "";
		LocalDate datumRodjenja = null;
		
		List<Student> filtriraniStudenti = new ArrayList<Student>();

		// �ifra check
		if (jmbagTextField.getText().isEmpty() == false) {
			jmbag += jmbagTextField.getText();
		}

		if (prezimeTextField.getText().isEmpty() == false) {
			prezime += prezimeTextField.getText();

		}
		
		if (imeTextField.getText().isEmpty() == false) {
			ime += imeTextField.getText();
			
		}
		
		if (datumRodjenjaDatePicker.getValue() != null) {
			datumRodjenja = datumRodjenjaDatePicker.getValue();
			
		}

		Student noviStudent = new Student(0, ime, prezime, jmbag, datumRodjenja);
		
		try {
			filtriraniStudenti = BazaPodataka.dohvatiStudentePremaKriterijima(noviStudent);
		} catch (BazaPodatakaException e) {
			e.getLocalizedMessage();
		}
		
		ObservableList<Student> listaStudenata = FXCollections.observableArrayList(filtriraniStudenti);
		studentTableView.setItems(listaStudenata);
	}
	
}
