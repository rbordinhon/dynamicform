package rbprojects.dynamicform.vo;

public class ErrorMessageDTO {

	public ErrorMessageDTO() {
		
	}
	
	
   public ErrorMessageDTO(String message, String category) {
		super();
		this.message = message;
		this.category = category;
	}


public String message;
   public String category;
   
}
