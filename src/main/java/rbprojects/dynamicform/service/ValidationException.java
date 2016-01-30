package rbprojects.dynamicform.service;

import rbprojects.dynamicform.vo.ErrorMessageDTO;

public class ValidationException extends Exception {

	/**
	 * @author Rodrigo
	 */
	private static final long serialVersionUID = 1659959557366903275L;
	private ErrorMessageDTO[] errors;

	public ValidationException() {
		super();

	}

	public ValidationException(ErrorMessageDTO[] errors) {
		super("Erro de validação");
		this.errors = errors;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}

	public ErrorMessageDTO[] getErrors() {
		return errors;
	}

	

}
