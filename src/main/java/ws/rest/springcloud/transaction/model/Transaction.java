package ws.rest.springcloud.transaction.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString 
@Getter
@Setter
@Document( collection =  "transaction")
public class Transaction {

	@Id
	private String id;
	private String prodid;
	@NotNull(message = "no puede ser nulo")
	private String prodtype; 
	private String transtype; 	
	private String headline;
	@Builder.Default
	private LocalDateTime transactdate= LocalDateTime.now();
	private Double amount; 
	private Double commission;
	private Double postamount;
	private String bank;
	private String titular;
	
	
	
	
}
