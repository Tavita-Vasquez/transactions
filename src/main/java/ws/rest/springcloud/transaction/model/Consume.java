package ws.rest.springcloud.transaction.model;

import java.time.LocalDate;

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
@Document
@ToString 
@Getter
@Setter
public class Consume {
	  private String id;
	  private Double amount;
	  private Double notpayedamount;
	  private LocalDate month;
	  private LocalDate maxmonth;
	  private String productid;
	  private String titular;
	  private Boolean payed;
}
