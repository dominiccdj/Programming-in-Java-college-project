package hr.java.vjezbe.iznimke;

/**
 * Oznaèena iznimka koja se baca kad student ima jednu negativnu ocjenu iz
 * ispita te mu se zbog toga ne može odrediti prosjek
 * 
 * @author domin
 *
 */
public class NemoguceOdreditiProsjekStudentaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6525578718663918621L;

	public NemoguceOdreditiProsjekStudentaException() {
		// TODO Auto-generated constructor stub
	}

	public NemoguceOdreditiProsjekStudentaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NemoguceOdreditiProsjekStudentaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NemoguceOdreditiProsjekStudentaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NemoguceOdreditiProsjekStudentaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
