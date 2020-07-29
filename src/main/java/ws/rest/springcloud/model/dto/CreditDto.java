package ws.rest.springcloud.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  
public class CreditDto {
	private String id;
	private String titular;
	private String credittype;
	private Double baseline;
	private Double consume;
	
	
		
}
