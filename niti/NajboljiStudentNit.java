package hr.java.vjezbe.niti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.glavnafx.Main;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class NajboljiStudentNit implements Runnable {

	@Override
	public void run() {

		Timeline prikazNajboljeg = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {

						List<Student> studenti = new ArrayList<Student>();
						List<Ispit> ispiti = new ArrayList<Ispit>();
						List<Double> prosjeci = new ArrayList<Double>();

						try {
							studenti = BazaPodataka.dohvatiStudentePremaKriterijima(new Student(0, "", "", "", null));
							ispiti = BazaPodataka.dohvatiIspitaPremaKriterijima(new Ispit(0, null, null, null, null));
						} catch (BazaPodatakaException e) {
							e.printStackTrace();
						}

						for (Student student : studenti) {
							List<Ispit> ispitiZaStudenta = ispiti.stream().filter(i -> i.getStudent().equals(student))
									.collect(Collectors.toList());

							Double prosjek = null;
							if (ispitiZaStudenta.size() > 0) {
								prosjek = ispitiZaStudenta.stream().mapToDouble(i -> i.getOcjena())
										.average().getAsDouble();
							}
							else prosjek = 1.0;
							
							prosjeci.add(prosjek);
							
						}
						
						Integer maxIndex = 0;
						for (int j = 0; j < prosjeci.size(); j++) {
							if (prosjeci.get(j) >= prosjeci.get(maxIndex)) {
								maxIndex = j;
							}
						}
						
						setTitle("Najbolji student: " + studenti.get(maxIndex).getImePrezime() + " (" + prosjeci.get(maxIndex) + ")");

					}
				});
			}
		}));
		prikazNajboljeg.setCycleCount(Timeline.INDEFINITE);
		prikazNajboljeg.play();

	}

	private void setTitle(String string) {
		Main.setStageTitle(string);
	}

}
