package ws.rest.springcloud.transaction.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Creditpaymentmultibankrequest {

	private String id;
	private String prodtype; 
	private String titular;
	private Double amountpayment;
	private List<String> listBank; // LISTA DE BANCOS
	private String descbank;
	private String accountnumber; // NUMERO DE CUENTA BANCARIA .
	private Double amountowed; // MONTO ADEUDADO
	private Double availableBalance;//SALDO DISPONIBLE
	
	
	
	
	
}
