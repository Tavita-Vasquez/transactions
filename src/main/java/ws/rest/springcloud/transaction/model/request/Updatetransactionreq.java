package ws.rest.springcloud.transaction.model.request;
 

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Updatetransactionreq {
	
	private String id;
	private String prodid;
	private String prodtype;//Account Bank o Credit Card
	private String transtype;
	private String titular;
	private Double amount;
    private LocalDateTime transactdate;
	private Double postamount; 
	private Double commission;
	 

}
