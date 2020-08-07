package ws.rest.springcloud.transaction.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Data  
@AllArgsConstructor  
@NoArgsConstructor  
@Getter
@Setter
@ToString
@Document( collection = "personalCustomer")
public class PersonalCustomer {
	 @Id
	    private String id;

	    private String name;
	    private String lastname;
	    private String motherLastname;
	    private String idBankAccount;
	    private String numberBankAccount;
	    private double maxCreditLimit;
	
	
}
