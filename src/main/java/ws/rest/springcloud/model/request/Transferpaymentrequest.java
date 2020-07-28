package ws.rest.springcloud.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transferpaymentrequest { 
	private String accountid;
	private String accounttitular;
	private String creditid;
	private String prodtype; 
	private String credittitular;
	private Double amount;
	private Double commission;
	

}
