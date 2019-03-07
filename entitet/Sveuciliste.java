package hr.java.vjezbe.entitet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sveuciliste<T extends ObrazovnaUstanova> extends Entitet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2485743473987255455L;
	private List<T> popisObrazovnihUstanova;

	public Sveuciliste(long id) {
		super(id);
		popisObrazovnihUstanova = new ArrayList<>();
	}
	
	/**Dodaje predanu obrazovnu ustanovu u listu obrazovnih ustanova
	 * @param objekt obrazovna ustanova
	 */
	public void dodajObrazovnuUstanovu(T objekt) {
		popisObrazovnihUstanova.add(objekt);
	}
	
	/**Dohvaæa obrazovnu ustanovu koja odgovara predanom indexu
	 * @param index index tražene obrazovne ustanove
	 * @return tražena obrazovna ustanova
	 */
	public T dohvatiObrazovnuUstanovu(Integer index) {
		Integer i = 0;
		T odabrana = null;
		for (T t : popisObrazovnihUstanova) {
			if (i == index) {
				odabrana = t;
			}
		}
		return odabrana;
	}
	
	/**Dohvaæa listu obrazovnih ustanova
	 * @return lista obrazovnih ustanova
	 */
	public List<T> dohvatiObrazovneUstanove() {
		return popisObrazovnihUstanova;
	}

	public int compareObrazovnaUstanova(T o1, T o2) {
		if (o1.getPopisStudenata().size() > o2.getPopisStudenata().size()) {
			return 1;
		}
		else if (o1.getPopisStudenata().size() < o2.getPopisStudenata().size()) {
			return -1;
		}
		else if (o1.getPopisStudenata().size() == o2.getPopisStudenata().size()) {
			return o1.getNazivUstanove().compareTo(o2.getNazivUstanove());
		}
		else return 0;
	}
	
	/**Dohvaæa broj studenata predane obrazovne ustanove
	 * @param o Obrazovna ustanova za koju želimo dohvatiti broj studenata
	 * @return broj studenata obrazovne ustanove
	 */
	public Integer getNumOfStudents(T o) {
		return o.popisStudenata.size();
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((popisObrazovnihUstanova == null) ? 0 : popisObrazovnihUstanova.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Sveuciliste other = (Sveuciliste) obj;
		if (popisObrazovnihUstanova == null) {
			if (other.popisObrazovnihUstanova != null)
				return false;
		} else if (!popisObrazovnihUstanova.equals(other.popisObrazovnihUstanova))
			return false;
		return true;
	}

	@SuppressWarnings("unused")
	private void order(List<T> obrazovneUstanove) {

	    Collections.sort(obrazovneUstanove, new Comparator<Object>() {

	        public int compare(Object o1, Object o2) {

	            @SuppressWarnings("unchecked")
				String x1 = ((T) o1).getNazivUstanove();
	            @SuppressWarnings("unchecked")
				String x2 = ((T) o2).getNazivUstanove();
	            int sComp = x1.compareTo(x2);

	            if (sComp != 0) {
	               return sComp;
	            } 

	            @SuppressWarnings("unchecked")
				Integer y1 = ((T) o1).getPopisStudenata().size();
	            @SuppressWarnings("unchecked")
				Integer y2 = ((T) o2).getPopisStudenata().size();
	            return x1.compareTo(x2);
	    }});
	}
	
}
