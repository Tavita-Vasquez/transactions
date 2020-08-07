package ws.rest.springcloud.transaction.controller;
 
import java.time.LocalDate; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus; 
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ws.rest.springcloud.transaction.model.Transaction;
import ws.rest.springcloud.transaction.model.dto.BankAccountDto;
import ws.rest.springcloud.transaction.model.dto.CreditDto;
import ws.rest.springcloud.transaction.model.request.AccdepositRequest;
import ws.rest.springcloud.transaction.model.request.AccwithdrawRequest;
import ws.rest.springcloud.transaction.model.request.Creditconsumerequest;
import ws.rest.springcloud.transaction.model.request.Creditpaymentmultibankrequest;
import ws.rest.springcloud.transaction.model.request.Creditpaymentrequest;
import ws.rest.springcloud.transaction.model.request.Transferpaymentrequest;
import ws.rest.springcloud.transaction.model.request.Updatetransactionreq;
import ws.rest.springcloud.transaction.service.TransactionServiceImpl;

@RequestMapping("/rest/transact") 
@RestController
public class TransactionController {
	
	@Autowired
	private TransactionServiceImpl transactservice;
	
	private static final String URL_ACCOUNT= "http://localhost:8031/rest/bankAccount";
	private static final String URL_CUSTOMER= "http://localhost:8030/rest/personalCustomer";
	private static final String URL_CREDIT="http://localhost:8033/rest/credit";
	
	
	/*Realizar un retiro de dinero de una cuenta*/
	@PostMapping("/withdraw")
	public  Mono<Transaction> moneywithdraw(@RequestBody AccwithdrawRequest mwithdrawrequest){ 
		System.out.println("Begin - method moneywithdrar!!");
		Mono<BankAccountDto> accountReq = WebClient.create(URL_ACCOUNT + "/findById/"+mwithdrawrequest.getId())
				                            .get().retrieve().bodyToMono(BankAccountDto.class);  
		return transactservice.moneywithdraw(mwithdrawrequest,accountReq, WebClient.create(URL_ACCOUNT+ "/updateaccount"));
	}

	
	/*Depositar a una cuenta*/
	@PostMapping("/deposit")
	public Mono<Transaction> moneydeposit(@RequestBody AccdepositRequest mdepositrequest){
		Mono<BankAccountDto> accountReq = WebClient.create( URL_ACCOUNT + "/findacc/"+mdepositrequest.getId())
                .get().retrieve().bodyToMono(BankAccountDto.class);
		return transactservice.moneydeposit(mdepositrequest, accountReq, WebClient.create(URL_ACCOUNT + "/updateaccount"));
	}
	
	/*Depositar a una cuenta*/
	@PostMapping("/payment")
	public Mono<Transaction> creditpayment(@RequestBody Creditpaymentrequest cpaymentrequest){
		Mono<CreditDto> credit = WebClient.create( URL_CREDIT + "/findcred/"+cpaymentrequest.getId())
                .get().retrieve().bodyToMono(CreditDto.class);
		return transactservice.creditpayment(cpaymentrequest, credit, WebClient.create(URL_CREDIT + "/updatecredit"));
	}
	
	
	/*Realizar un consumo a un producto de credito*/
	@PostMapping("/consume")
	public Mono<Transaction> creditconsume(@RequestBody Creditconsumerequest cconsumerequest){
		Mono<CreditDto> credit = WebClient.create( URL_CREDIT + "/findcred/"+cconsumerequest.getId())
                .get().retrieve().bodyToMono(CreditDto.class);
		return transactservice.creditconsume(cconsumerequest, credit, WebClient.create(URL_CREDIT + "/updatecredit"));
	}
	
	
	/*Eliminar de un cuenta*/
	@DeleteMapping("/delete/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
	public Mono<Void> deletetransaction(@PathVariable String id){
		return transactservice.deletetransaction(id);
	}
	
	
	/*Busqueda de todas las transacciones*/
	@GetMapping("/find")
	public Flux<Transaction> findtransaction(){
		System.out.println("::findtransaction::");
		
	      return transactservice.findtransaction();
    }
	
	/*Busqueda de transacciones por id*/
	@GetMapping("/find/{id}")
	public Mono<Transaction> findtransactionbyid(@PathVariable String id){
	      return transactservice.findtransactionbyid(id);
    }
	
	/*Busqueda de transacciones por fecha*/
	@GetMapping("/findbytitular/{titular}") 
	public Flux<Transaction> findtitulartransaction(@PathVariable String titular,
		  @RequestParam(name = "date1", defaultValue ="01/01/1988" )@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  date1,
  	      @RequestParam(name = "date2", defaultValue = "01/01/2020") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date2){
	      return transactservice.findclienttransaction(titular, date1, date2);
    }
	
	/*Actualizar una transaccion*/
    @PutMapping("/updatetransaction")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Transaction> updatetransaction(@RequestBody Updatetransactionreq updatetransactionreq) {
      return transactservice.updatetransaction(updatetransactionreq);
    }
    
  	/*Pago de un credito mediante transferencia de una cuenta del mismo banco*/
	@PostMapping("/transferpayment")
	public Mono<Transaction> transferpayment(@RequestBody Transferpaymentrequest tpaymentrequest){
		
		return null;
	}
	
	/*Pago de un credito mediante transferencia de una cuenta del mismo banco a otra cuenta de otro banco*/
	@PostMapping("/transferpaymultibank")
	public Mono<Transaction> transferpaymultibank(@RequestBody Transferpaymentrequest tpaymentrequest){
		Mono<BankAccountDto> account = WebClient.create( URL_ACCOUNT + "/findacc/"+tpaymentrequest.getAccountid())
                .get().retrieve().bodyToMono(BankAccountDto.class); 
		Mono<CreditDto> credit = WebClient.create( URL_CREDIT + "/findcred/"+tpaymentrequest.getCreditid())
                .get().retrieve().bodyToMono(CreditDto.class);
		return transactservice.multibankTransPay(tpaymentrequest, account, credit, WebClient.create(URL_ACCOUNT + "/updateaccount"), WebClient.create(URL_CREDIT + "/updatecredit"));
	}
	
	
	
 
}
