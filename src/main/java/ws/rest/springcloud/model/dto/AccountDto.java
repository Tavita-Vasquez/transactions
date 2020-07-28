package ws.rest.springcloud.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
	private String id;  
	private String acctype;
	private List<String> titular;
	private List<String> firmantecode;
	private Double saldo;
	private String acctypedesc;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAcctype() {
		return acctype;
	}
	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}
	public List<String> getTitular() {
		return titular;
	}
	public void setTitular(List<String> titular) {
		this.titular = titular;
	}
	public List<String> getFirmantecode() {
		return firmantecode;
	}
	public void setFirmantecode(List<String> firmantecode) {
		this.firmantecode = firmantecode;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public String getAcctypedesc() {
		return acctypedesc;
	}
	public void setAcctypedesc(String acctypedesc) {
		this.acctypedesc = acctypedesc;
	}
	
	
}
