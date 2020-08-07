package ws.rest.springcloud.transaction.service;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ws.rest.springcloud.transaction.model.Transaction;
import ws.rest.springcloud.transaction.model.dto.ATMTransactionDTO;
import ws.rest.springcloud.transaction.model.dto.BankAccountDto;
import ws.rest.springcloud.transaction.model.dto.CreditDto;
import ws.rest.springcloud.transaction.model.request.AccdepositRequest;
import ws.rest.springcloud.transaction.model.request.AccwithdrawRequest;
import ws.rest.springcloud.transaction.model.request.Creditconsumerequest;
import ws.rest.springcloud.transaction.model.request.Creditpaymentmultibankrequest;
import ws.rest.springcloud.transaction.model.request.Creditpaymentrequest;
import ws.rest.springcloud.transaction.model.request.Transferpaymentrequest;
import ws.rest.springcloud.transaction.model.request.Updatetransactionreq;
import ws.rest.springcloud.transaction.model.response.TransactionResponse;

public interface ITransactionService{
	
	  Mono<TransactionResponse> depositatm(ATMTransactionDTO atmrequest, Mono<BankAccountDto> atmwc, WebClient webclient);
	  Mono<TransactionResponse> withdrawatm(ATMTransactionDTO atmrequest, Mono<BankAccountDto> atmwc, WebClient webclient);
	  
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
		 
	 /*El sistema deberá permitir el pago de tarjetas de créditos de 
	 diferentes bancos pertenecientes al sistema y debitar el pago a 
	 una cuenta que pertenezca a un banco diferente al banco que pertenezca la tarjeta de crédito.*/
	  Mono<Transaction> creditpaymentmultiBank(Creditpaymentmultibankrequest cpaymentmultibankrequest , Mono<BankAccountDto> accountpayment, Mono<CreditDto> creditpayment,WebClient accwebclient, WebClient credwebclient);
	
	  Mono<Transaction> multibankTransPay(Transferpaymentrequest tpaymentrequest, Mono<BankAccountDto> account,
			Mono<CreditDto> credit, WebClient accwebclient, WebClient credwebclient);
	  
}
