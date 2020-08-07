package ws.rest.springcloud.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ATMTransactionDTO {

	private String  titular;
	private String  atmbank;
	private String  productid; 
	private Double  commission;
	private Double  amount;
	
}
