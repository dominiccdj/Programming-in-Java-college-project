package hr.java.vjezbe.iznimke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PopisProfesoraException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6811074496687711757L;
	private static final Logger logger = LoggerFactory.getLogger(PopisProfesoraException.class);

	public PopisProfesoraException() {
		// TODO Auto-generated constructor stub
	}

	public PopisProfesoraException(String message) {
		super(message);
		System.out.println(message);
		logger.info(message);
		// TODO Auto-generated constructor stub
	}

	public PopisProfesoraException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public PopisProfesoraException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PopisProfesoraException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
