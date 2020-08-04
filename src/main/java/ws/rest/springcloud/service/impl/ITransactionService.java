package ws.rest.springcloud.service.impl;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ws.rest.springcloud.model.Transaction;
import ws.rest.springcloud.model.dto.BankAccountDto;
import ws.rest.springcloud.model.dto.CreditDto;
import ws.rest.springcloud.model.request.AccdepositRequest;
import ws.rest.springcloud.model.request.AccwithdrawRequest;
import ws.rest.springcloud.model.request.Creditconsumerequest;
import ws.rest.springcloud.model.request.Creditpaymentrequest;
import ws.rest.springcloud.model.request.Transferpaymentrequest;
import ws.rest.springcloud.model.request.Updatetransactionreq;

public interface ITransactionService{
	
	  Mono<Transaction> moneywithdraw(AccwithdrawRequest mwithdrawrequest, Mono<BankAccountDto> account,WebClient accwebclient);
	  Mono<Transaction> moneydeposit(AccdepositRequest mdepositrequest, Mono<BankAccountDto> account, WebClient accwebclient);
	  Mono<Transaction> creditpayment(Creditpaymentrequest cpaymentrequest, Mono<CreditDto> credit, WebClient credwebclient);
	  Mono<Transaction> creditconsume(Creditconsumerequest cpaymentrequest, Mono<CreditDto> credit, WebClient credwebclient);
	  Mono<Transaction> transferpayment(Transferpaymentrequest tpaymentrequest, Mono<BankAccountDto> account, Mono<CreditDto> credit, WebClient accwebclient,  WebClient credwebclient);
	  
	  Mono<Void> deletetransaction(String id); 
	  Flux<Transaction> findtransaction();	
	   Mono<Transaction> updatetransaction(Updatetransactionreq updatetransactionreq);
	// Mono delete(String id); 
	  Mono<Boolean> checkforexpiredcredit(String titular);
	  
	  
	  //Reports :
	  
	  Flux<Transaction> findclienttransaction(String titular,  LocalDate  date1,  LocalDate date2);
	  Mono<Transaction> findtransactionbyid(String id);
		 
	  
	  
}
