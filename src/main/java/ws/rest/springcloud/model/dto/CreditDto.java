package ws.rest.springcloud.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  
public class CreditDto {
	private String id;
	private String headline;//titular
	private String credittype;//personal, empresarial, tarjetas de crédito y adelanto de efectivo).
	private Double baseline;
	private Double availablebalance; //saldo disponible.
	private Double consume;
	private LocalDate datecredit;
	
		
}
