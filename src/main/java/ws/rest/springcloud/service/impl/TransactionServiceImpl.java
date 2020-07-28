package ws.rest.springcloud.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ws.rest.springcloud.model.Transaction;
import ws.rest.springcloud.model.dto.AccountDto;
import ws.rest.springcloud.model.dto.CreditDto;
import ws.rest.springcloud.model.request.AccdepositRequest;
import ws.rest.springcloud.model.request.AccwithdrawRequest;
import ws.rest.springcloud.model.request.Creditconsumerequest;
import ws.rest.springcloud.model.request.Creditpaymentrequest;
import ws.rest.springcloud.repository.ITransactionrepo;

@Service
public class TransactionServiceImpl {

	@Autowired
	private ITransactionrepo transacrepo;
	
	
	public Mono<Transaction> moneywithdraw(AccwithdrawRequest mwithdrawrequest, Mono<AccountDto> account, WebClient webclient) { 
		return account.filter(acc-> acc.getTitular().contains(mwithdrawrequest.getTitular()))
				      .switchIfEmpty(Mono.error(new Exception("Titular not found")))
				      .flatMap(acc-> 
				    	  transacrepo.countByTitular(mwithdrawrequest.getTitular()).switchIfEmpty(Mono.error(new Exception("problema"))).log().map(count ->
				    	  {   mwithdrawrequest.setCommission(count>=Configtransaction.COMMISSION_FREE_TIMES?Configtransaction.COMMISSION_WITHDRAW_VALUE:0);  
				    	      return acc;
				    	  }) 
				      )
				      .filter(acc-> acc.getSaldo()-mwithdrawrequest.getAmount()-mwithdrawrequest.getCommission()>=0)
				      .switchIfEmpty(Mono.error(new Exception("Dont have enought money")))
				      .flatMap(refresh-> {
				    	  refresh.setSaldo(refresh.getSaldo()- mwithdrawrequest.getAmount()-mwithdrawrequest.getCommission());
				    	  return webclient.put().body(BodyInserters.fromValue(refresh)).retrieve().bodyToMono(AccountDto.class);
				      })
				      .switchIfEmpty(Mono.error(new Exception("Error refresh account")))
				      .flatMap(then->            transacrepo.save(Transaction.builder()
							                    .prodid(then.getId())
							                    .prodtype(then.getAcctype())
							                    .transtype("WITHDRAW")
							                    .titular(mwithdrawrequest.getTitular())
							                    .amount(mwithdrawrequest.getAmount())
							                    .commission(mwithdrawrequest.getCommission())
							                    .postamount(then.getSaldo())
							                    .build())); 
	}
	


	public Mono<Transaction> creditpayment(Creditpaymentrequest cpaymentrequest, Mono<CreditDto> credit, WebClient credwebclient) { 
		return  credit.filter(cred-> cred.getTitular().contains(cpaymentrequest.getTitular()))
				.switchIfEmpty(Mono.error(new Exception("Not credit found - cpayment"))) 
				.filter(cred -> cred.getConsume()-cpaymentrequest.getAmount()>=0)
				.switchIfEmpty(Mono.error(new Exception("Cant process the transaction")))
				.flatMap(refresh -> {
					refresh.setConsume(refresh.getConsume()-cpaymentrequest.getAmount());
					return credwebclient.put().body(BodyInserters.fromValue(refresh)).retrieve().bodyToMono(CreditDto.class) ;
				})
				 .switchIfEmpty(Mono.error(new Exception("Error refresh credit")))
				 .flatMap(then -> transacrepo.save(Transaction.builder
								                    .prodid(then.getId())
								                    .prodtype(then.getCredittype())
								                    .transtype("PAYMENT")
								                    .titular(cpaymentrequest.getTitular())
								                    .amount(cpaymentrequest.getAmount())
								                    .postamount(then.getBaseline()-then.getConsume())
								                    .build()));
				
	}
	 
	public Mono<Transaction> creditconsume(Creditconsumerequest cconsumerequest,  Mono<CreditDto> credit, WebClient credwebclient) { 
		return credit.filter(cred-> cred.getTitular().contains(cconsumerequest.getTitular()))
		             .switchIfEmpty(Mono.error(new Exception("Not credit found  - cconsume"))) 
				     .filter(cred -> (cred.getBaseline()-cred.getConsume()-cconsumerequest.getAmount())>=0)
				     .switchIfEmpty(Mono.error(new Exception("Cant process the transaction")))
				     .flatMap(refresh -> {
					  refresh.setConsume(refresh.getConsume()+cconsumerequest.getAmount());
					  return credwebclient.put().body(BodyInserters.fromValue(refresh)).retrieve().bodyToMono(CreditDto.class) ;
					 })
				     .flatMap(then-> transacrepo.save(Transaction.builder()
								                    .prodid(then.getId())
								                    .prodtype(then.getCredittype())
								                    .transtype("CONSUME")
								                    .titular(cconsumerequest.getTitular())
								                    .amount(cconsumerequest.getAmount())
								                    .postamount(then.getBaseline()-then.getConsume())
								                    .build()));
	}


	/*Deleted Transaction by Id*/
	public Mono<Void> deletetransaction(String id) { 
		return transacrepo.findById(id)
				.switchIfEmpty(Mono.error(new Exception("No encontrado")))
				.flatMap(transacrepo::delete);
	}

	/*Get All List for Transaction for Id Client*/
	public Flux<Transaction> findclienttransaction(String idHeadLine, LocalDate dateIni, LocalDate dateEnd) { 
		return transacrepo.findByTitularAndTransactdateBetween(idHeadLine,dateIni,dateEnd)
				          .switchIfEmpty(Mono.error(new Exception("Not data found 0 transactions.")));
	}
	
	/*Get All Transactions*/	
	public Flux<Transaction> findtransaction() { 
		return transacrepo.findAll();
	}
	
	/*Get All Transaction for By Id transac. */
	public Mono<Transaction> findtransactionbyid(String id) { 
		return transacrepo.findById(id)
				          .switchIfEmpty(Mono.error(new Exception("Not found transaction")));
	} 
	
	
	
}
