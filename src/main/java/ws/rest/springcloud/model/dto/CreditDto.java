package ws.rest.springcloud.model.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor  
@Getter
@Setter
public class CreditDto {
	private String id;
	private String headline;//titular
	private String credittype;//personal, empresarial, tarjetas de crédito y adelanto de efectivo).
	private Double baseline;
	private Double availablebalance; //saldo disponible.
	private Double consume;
	private LocalDate datecredit;
	private String bank;
		
}
