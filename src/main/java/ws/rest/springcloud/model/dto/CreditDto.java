package ws.rest.springcloud.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  
public class CreditDto {
	private String id;
	private String titular;
	private String credittype;
	private Double baseline;
	private Double consume;
	
	
	public CreditDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreditDto(String id, String titular, String credittype, Double baseline, Double consume) {
		super();
		this.id = id;
		this.titular = titular;
		this.credittype = credittype;
		this.baseline = baseline;
		this.consume = consume;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public String getCredittype() {
		return credittype;
	}
	public void setCredittype(String credittype) {
		this.credittype = credittype;
	}
	public Double getBaseline() {
		return baseline;
	}
	public void setBaseline(Double baseline) {
		this.baseline = baseline;
	}
	public Double getConsume() {
		return consume;
	}
	public void setConsume(Double consume) {
		this.consume = consume;
	}
	
	
	
	
}
