package com.everis.mstransact.expose;
 
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ws.rest.springcloud.model.Transaction;
import ws.rest.springcloud.model.dto.BankAccountDto;
import ws.rest.springcloud.model.dto.CreditDto;
import ws.rest.springcloud.model.request.AccdepositRequest;
import ws.rest.springcloud.model.request.AccwithdrawRequest;
import ws.rest.springcloud.model.request.Creditpaymentrequest;
import ws.rest.springcloud.model.request.Transferpaymentrequest;
import ws.rest.springcloud.model.request.Updatetransactionreq;
import ws.rest.springcloud.service.impl.TransactionServiceImpl;

@RestController
@RequestMapping("/apitransaction") 

public class MstransactionController {
	
	@Autowired
	private TransactionServiceImpl transactservice;
	private static final String URL_ACCOUNT= "http://localhost:8031/rest/bankAccount/findById";
	private static final String URL_CREDIT= "http://localhost:8030/rest/personalCustomer/findById";
	
	/*Realizar un retiro de dinero de una cuenta*/
	@PostMapping("/withdraw")
	public  Mono<Transaction> moneywithdraw(@RequestBody AccwithdrawRequest mwithdrawrequest){ 
		Mono<BankAccountDto> accountReq = WebClient.create(URL_ACCOUNT + "/findById/"+mwithdrawrequest.getId())
				                            .get().retrieve().bodyToMono(BankAccountDto.class);  
		return transactservice.moneywithdraw(mwithdrawrequest,accountReq, WebClient.create(URL_ACCOUNT+ "/updateaccount"));
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
		  @RequestParam(name = "date1", defaultValue ="01/01/1980" )@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  date1,
  	      @RequestParam(name = "date2", defaultValue = "01/01/4000") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date2){
	      return transactservice.findclienttransaction(titular, date1, date2);
    }
	
	/*Actualizar una transaccion*/
    @PutMapping("/updatetransaction")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Transaction> updatetransaction(@RequestBody Updatetransactionreq updatetransactionreq) {
      return transactservice.updatetransaction(updatetransactionreq);
    }
    
    
 
}
