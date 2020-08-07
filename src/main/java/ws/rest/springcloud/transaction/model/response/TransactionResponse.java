package ws.rest.springcloud.transaction.model.response;

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
public class TransactionResponse {
	private String transactid;
	private String productid;
	private Double amount;
	private Double totalcommission;
}
