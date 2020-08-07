package ws.rest.springcloud.transaction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ATMTransactionDTO {

	private String  titular;
	private String  atmbank;
	private String  productid; 
	private Double  commission;
	private Double  amount;
	
}
