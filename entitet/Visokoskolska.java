package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

/**
 * Interface koji ima metode za izra�un kona�ne ocjene za studenta, za
 * odre�ivanje prosjeka na ispitima, za filtriranje(izdvajanje) polo�enih
 * ispita, za filtriranje(izdvajanje) ispita koje je polo�io odre�eni student te
 * za provjeru jesu li svi ispiti polo�eni
 * 
 * @author domin
 *
 */
public interface Visokoskolska {

	/**
	 * Ra�una kona�nu ocjenu za studenta na temelju ocjena iz ispita te ocjena iz
	 * pismemog dijela i obrane zavr�nog rada
	 * 
	 * @param ispiti                   niz ispita iz kojih se gledaju ocjene
	 * @param ocjenaPismenog           ocjena pismenog dijela zavr�nog rada
	 * @param ocjenaObraneZavrsnogRada ocjena obrane zavr�nog rada
	 * @return kona�na ocjena za studenta
	 */
	BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, float ocjenaPismenog,
			float ocjenaObraneZavrsnogRada);

	/**
	 * Odre�uje prosjek ocjena na ispitima
	 * 
	 * @param ispitiZaStudenta niz ispita iz kojih se gledaju ocjene
	 * @return prosjek ocjena
	 * @throws NemoguceOdreditiProsjekStudentaException ako student ima barem jednu
	 *                                                  negativnu ocjenu tada se ne
	 *                                                  ra�una prosjek, a on ima
	 *                                                  zaklju�nu ocjenu nedovoljan
	 *                                                  (1)
	 */
	default BigDecimal odrediProsjekOcjenaNaIspitima(List<Ispit> ispitiZaStudenta)
			throws NemoguceOdreditiProsjekStudentaException {
		BigDecimal zbrojPozOcjena = new BigDecimal(0);
		Integer brojacPozOcjena = 0;

		for (Ispit ispit : ispitiZaStudenta) {
			if (ispit.getOcjena() > 1) {
				zbrojPozOcjena = zbrojPozOcjena.add(new BigDecimal(ispit.getOcjena()));
				brojacPozOcjena++;
			} else if (ispit.getOcjena() == 1) {
				throw new NemoguceOdreditiProsjekStudentaException(
						"Negativna ocjena iz ispita " + ispit.getPredmet().getNaziv());
			}
		}

		zbrojPozOcjena = zbrojPozOcjena.divide(new BigDecimal(brojacPozOcjena));
		return zbrojPozOcjena;
	}

	/**
	 * Iz niza ispita uklanja ispite koji nisu polo�eni
	 * 
	 * @param ispiti niz ispita
	 * @return niz polo�enih ispita
	 */
	@SuppressWarnings("unused")
	default Ispit[] filtrirajPolozeneIspite(Ispit[] ispiti) {
		Integer brojacPozOcjena = 0;
		for (int i = 0; i < ispiti.length; i++) {
			if (ispiti[i].getOcjena() > 1) {
				brojacPozOcjena++;
			}
		}

		// inicijaliziran niz dugacak koliko ima pozitivnih ocjena
		Ispit[] pozitivniIspiti = new Ispit[brojacPozOcjena];

		brojacPozOcjena = 0;
		for (int i = 0; i < ispiti.length; i++) {
			if (ispiti[i].getOcjena() > 1) {
				pozitivniIspiti[brojacPozOcjena] = ispiti[i];
				brojacPozOcjena++;
			}
		}
		return pozitivniIspiti;
	}

	/**
	 * Iz niza ispita filtrira one ispite koje je dao izabrani student
	 * 
	 * @param ispiti    niz ispita
	 * @param student student �ije ispite treba filtrirati
	 * @return niz ispita za izabranog studenta
	 */
	default List<Ispit> filtrirajIspitePoStudentu(List<Ispit> ispiti, Student student) {

		List<Ispit> ispitiZaStudenta = new ArrayList<>();
	
		ispitiZaStudenta = ispiti.stream().filter(i -> i.getStudent() == student).collect(Collectors.toList());
	
		return ispitiZaStudenta;

	}

	/**
	 * Provjerava jesu li svi predani ispiti polo�eni
	 * 
	 * @param ispiti niz ispita
	 * @return true ako ispiti jesu polo�eni, false ako nisu
	 */
	default boolean sviIspitiPolozeni(List<Ispit> ispiti) {
		Integer brojacNegativnih = 0;
		for(Ispit ispit: ispiti) {
			if (ispit.getOcjena() == 1) {
				brojacNegativnih++;
			}
		}
		if (brojacNegativnih == 0) {
			return true;
		} else
			return false;
	}

}
