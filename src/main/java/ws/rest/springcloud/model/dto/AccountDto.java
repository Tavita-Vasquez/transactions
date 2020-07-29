package ws.rest.springcloud.model.dto;

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
public class AccountDto {
	private String id;  
	private String acctype;
	private List<String> titular;
	private List<String> firmantecode;
	private Double saldo;
	private String acctypedesc;
	
		
}
