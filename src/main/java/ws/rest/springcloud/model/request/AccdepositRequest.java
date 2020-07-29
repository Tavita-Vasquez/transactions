package ws.rest.springcloud.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccdepositRequest{  
	private String id;
	private String prodtype; 
	private String titular;
	private Double amount;
	private Double commission=0d;
	
	
}
