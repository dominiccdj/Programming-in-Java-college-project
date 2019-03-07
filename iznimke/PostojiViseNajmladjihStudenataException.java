package hr.java.vjezbe.iznimke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Neoznaèena iznimka koja se poziva ako postoje 2 studenta (najmlaða) s istim
 * datumom roðenja
 * 
 * @author domin
 *
 */
public class PostojiViseNajmladjihStudenataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1142088483730368726L;
	private static final Logger logger = LoggerFactory.getLogger(PostojiViseNajmladjihStudenataException.class);

	public PostojiViseNajmladjihStudenataException() {
		// TODO Auto-generated constructor stub
	}

	public PostojiViseNajmladjihStudenataException(String message) {
		super(message);
		System.out.println(message);
		logger.info(message);

	}

	public PostojiViseNajmladjihStudenataException(Throwable cause) {
		super(cause);
		System.out.println(cause);
		// TODO Auto-generated constructor stub
	}

	public PostojiViseNajmladjihStudenataException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PostojiViseNajmladjihStudenataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
