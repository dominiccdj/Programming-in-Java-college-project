package hr.java.vjezbe.glavnafx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.glavna.Glavna;
import hr.java.vjezbe.niti.DatumRodjenjaNit;
import hr.java.vjezbe.niti.NajboljiStudentNit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private static final int BROJ_PROFESORA = 2;
	private static final int BROJ_PREDMETA = 3;
	private static final int BROJ_STUDENATA = 2;
	private static final int BROJ_ISPITNIH_ROKOVA = BROJ_STUDENATA;
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

	private static BorderPane root;
	private static Stage primaryStage;

	@Override
	public void start(Stage stage) {
		primaryStage = stage;
		try {
			root = (BorderPane) FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root, 400, 530);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Još nema dostupnog rezultata");
			primaryStage.show();

			// ----------------------------------threads----------------------------------------

			ExecutorService executorService = Executors.newCachedThreadPool();

			DatumRodjenjaNit datumRodjenjaNit = new DatumRodjenjaNit();
			NajboljiStudentNit najboljiStudentNit = new NajboljiStudentNit();

			executorService.execute(datumRodjenjaNit);
			executorService.execute(najboljiStudentNit);

			executorService.shutdown();

			// -----------------------------------------------------------------------------------

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setStageTitle(String newTitle) {
        primaryStage.setTitle(newTitle);
    }

	public static void setCenterPane(BorderPane centerPane) { // umjesto setMainPage
		root.setCenter(centerPane);
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Integer izracunajBrojUstanova() {

		Integer brojUstanova = 0;
		Integer brojLinijaProf = 0;

		try (BufferedReader in = new BufferedReader(new FileReader("dat/profesori.txt"))) {
			@SuppressWarnings("unused")
			String line;
			while ((line = in.readLine()) != null) {
				brojLinijaProf += 1;
			}
		} catch (IOException e) {
			System.err.println(e);
		}
		brojUstanova = (brojLinijaProf / BROJ_PROFESORA) / 5;

		return brojUstanova;
	}

}
