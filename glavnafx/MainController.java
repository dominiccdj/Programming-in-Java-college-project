package hr.java.vjezbe.glavnafx;



import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.MenuItem;

public class MainController {
	
	@FXML
	private MenuItem ProfesorPretraga;
	
	@FXML
	private MenuItem ProfesorUnos;
	
	@FXML
	private MenuItem StudentPretraga;
	
	@FXML
	private MenuItem StudentUnos;
	
	@FXML
	private MenuItem PredmetPretraga;
	
	@FXML
	private MenuItem PredmetUnos;
	
	@FXML
	private MenuItem IspitPretraga;
	
	@FXML
	private MenuItem IspitUnos;
	
	@FXML
	private void Dialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Look, an Information Dialog");
		alert.setContentText("I have a great message for you!");

		alert.showAndWait();
	}
	
	@FXML
	public void  prikaziPretraguProfesora() {
		try {
			BorderPane profPane = FXMLLoader.load(Main.class.getResource("Profesor.fxml"));
			Main.setCenterPane(profPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void  prikaziUnosProfesora() {
		try {
			BorderPane profPane = FXMLLoader.load(Main.class.getResource("ProfesorUnos.fxml"));
			Main.setCenterPane(profPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void  prikaziPretraguStudenata() {
		try {
			BorderPane profPane = FXMLLoader.load(Main.class.getResource("Student.fxml"));
			Main.setCenterPane(profPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void  prikaziUnosStudenata() {
		try {
			BorderPane profPane = FXMLLoader.load(Main.class.getResource("StudentUnos.fxml"));
			Main.setCenterPane(profPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void  prikaziPretraguPredmeta() {
		try {
			BorderPane profPane = FXMLLoader.load(Main.class.getResource("Predmet.fxml"));
			Main.setCenterPane(profPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void  prikaziUnosPredmeta() {
		try {
			BorderPane profPane = FXMLLoader.load(Main.class.getResource("PredmetUnos.fxml"));
			Main.setCenterPane(profPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void  prikaziPretraguIspita() {
		try {
			BorderPane profPane = FXMLLoader.load(Main.class.getResource("Ispit.fxml"));
			Main.setCenterPane(profPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void  prikaziUnosIspita() {
		try {
			BorderPane profPane = FXMLLoader.load(Main.class.getResource("IspitUnos.fxml"));
			Main.setCenterPane(profPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
