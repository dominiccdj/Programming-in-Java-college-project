package hr.java.vjezbe.niti;

import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;


public class DatumRodjenjaNit implements Runnable {
	

	@Override
	public void run() {
		
		Timeline prikazSlavljenika = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						
						List <Student> listaStudenata = new ArrayList<>();
						
						// dohvat studenata
						try {
							listaStudenata = BazaPodataka.dohvatiStudenteRodjeneDanas();
						} catch (BazaPodatakaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (listaStudenata.size() > 0) {
							// string koji će ispisati sve studente koji imaju rodendan danas
							String studenti = "";
							for (Student s : listaStudenata) {
								studenti += s.getImePrezime() + "\n";
							}
							
							
							// alert koji se pokazuje i ispisuje koji studenti imaju rodendan
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Rođendan studenata");
							alert.setHeaderText("Čestitajte rođendan sljedećim studentima:");
							alert.setContentText(studenti);
							alert.showAndWait();
						}
						
					}
				});
			}
		}));
		prikazSlavljenika.setCycleCount(Timeline.INDEFINITE);
		prikazSlavljenika.play();

	}

}
