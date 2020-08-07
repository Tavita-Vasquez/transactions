package ws.rest.springcloud.transaction.model.request;
 
 

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document
public class AccwithdrawRequest {
	
	private String id;
	private String prodtype; 
	private String titular;
	private Double amount;
	private Double commission;
	
	
	public AccwithdrawRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccwithdrawRequest(String id, String prodtype, String titular, Double amount, Double commission) {
		super();
		this.id = id;
		this.prodtype = prodtype;
		this.titular = titular;
		this.amount = amount;
		this.commission = commission;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProdtype() {
		return prodtype;
	}

	public void setProdtype(String prodtype) {
		this.prodtype = prodtype;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
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

	@Override
	public String toString() {
		return "AccwithdrawRequest [id=" + id + ", prodtype=" + prodtype + ", titular=" + titular + ", amount=" + amount
				+ ", commission=" + commission + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((commission == null) ? 0 : commission.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((prodtype == null) ? 0 : prodtype.hashCode());
		result = prime * result + ((titular == null) ? 0 : titular.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccwithdrawRequest other = (AccwithdrawRequest) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (commission == null) {
			if (other.commission != null)
				return false;
		} else if (!commission.equals(other.commission))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (prodtype == null) {
			if (other.prodtype != null)
				return false;
		} else if (!prodtype.equals(other.prodtype))
			return false;
		if (titular == null) {
			if (other.titular != null)
				return false;
		} else if (!titular.equals(other.titular))
			return false;
		return true;
	} 
	
	
	 
}
