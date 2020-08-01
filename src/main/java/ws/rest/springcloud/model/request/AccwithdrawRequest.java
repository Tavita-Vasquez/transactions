package ws.rest.springcloud.model.request;
 
 

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AccwithdrawRequest {
	
	private String id;
	private String prodtype; 
	private String titular;
	private Double amount;
	private Double commission;
	private LocalDate dateRequest;
	
	 
}
