package ws.rest.springcloud.model;

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
@Document( collection =  "transaction")
public class Transaction {

	@Id
	private String id;
	private String prodid;
	@NotNull(message = "no puede ser nulo")
	private String prodtype; 
	private String transtype; 	
	private String idHeadLine;
	@Builder.Default
	private LocalDateTime transactdate= LocalDateTime.now();
	private Double amount; 
	private Double commission;
	private Double postamount;
	private String bank;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProdid() {
		return prodid;
	}
	public void setProdid(String prodid) {
		this.prodid = prodid;
	}
	public String getProdtype() {
		return prodtype;
	}
	public void setProdtype(String prodtype) {
		this.prodtype = prodtype;
	}
	public String getTranstype() {
		return transtype;
	}
	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}
	public String getIdHeadLine() {
		return idHeadLine;
	}
	public void setIdHeadLine(String idHeadLine) {
		this.idHeadLine = idHeadLine;
	}
	public LocalDateTime getTransactdate() {
		return transactdate;
	}
	public void setTransactdate(LocalDateTime transactdate) {
		this.transactdate = transactdate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	public Double getPostamount() {
		return postamount;
	}
	public void setPostamount(Double postamount) {
		this.postamount = postamount;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	
	
	
	
}
