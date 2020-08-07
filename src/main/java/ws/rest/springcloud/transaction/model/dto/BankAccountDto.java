package ws.rest.springcloud.transaction.model.dto;

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
public class BankAccountDto {
	private String id;  
	private String acctype; // tipo de cuenta bancaria ... ahorro, cuentas corrientes, o cuentas a plazo fijo)
	private List<String> headline; // titular Las cuentas bancarias pueden tener uno o más titulares
	private List<String> authorizedsigner; // y cero o más firmantes autorizados.
	private Double availablebalance; //saldo disponible.
	private Double balancetotal;
	private String acctypedesc;
	private LocalDate dateaccount;
	private String bankId; // codigo del banco al que realizará el pago de credito
	private String titular;	
}
