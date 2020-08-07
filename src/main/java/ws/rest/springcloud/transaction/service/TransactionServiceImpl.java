package ws.rest.springcloud.transaction.service;

import java.time.LocalDate;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.util.concurrent.AtomicDouble;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ws.rest.springcloud.transaction.config.Configtransaction;
import ws.rest.springcloud.transaction.model.Consume;
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
import ws.rest.springcloud.transaction.repository.IConsumeRepository;
import ws.rest.springcloud.transaction.repository.ITransactionRepository;


@Service
public class TransactionServiceImpl implements ITransactionService {

	@Autowired
	private ITransactionRepository repository;

	@Autowired
	private IConsumeRepository consumerepo;
	
	 @Autowired
	 MessageSource i18n;
	 
	//log
		private static final Logger log=(Logger) LogManager.getLogger(TransactionServiceImpl.class);

		
		
	/*RETIRO DINERO*/
	/*1. Se valida la petición de usuario que generó la solicitud(http request)*/
	/*2. Se contabiliza el conteo total de peticiones por cada transactions y comparamos si es > a parameter COMMISSION_FREE_TIMES le añadimos el valor de comission. de lo contrario comission = 0 */
	/*3. Despues se filtra si el monto a retirar con comision es > al saldo DISPONIBLE de la cuenta*/
	/*4. ACTUALIZA nuevo saldo disponible  con los nuevos valores en el microservicio de Bank ACCOUNTS */
	/*5. REGISTRA Transaction como RETIRO (WITHDRAW).*/
	
	public Mono<Transaction> moneywithdraw(AccwithdrawRequest mwithdrawrequest, Mono<BankAccountDto> account,
			WebClient accwebclient){ 
		return account.filter(acc-> acc.getHeadline().contains(mwithdrawrequest.getTitular()))
			      .switchIfEmpty(Mono.error(new Exception("getHeadLine not found"))) 
			      .flatMap(acc-> 
			      repository.countTransacByTitular(mwithdrawrequest.getTitular()).switchIfEmpty(Mono.error(new Exception("Exceptions."))).log().map(count ->
			    	  {   mwithdrawrequest.setCommission(count>=Configtransaction.COMMISSION_FREE_TIMES?Configtransaction.COMMISSION_WITHDRAW_VALUE:0);  
			    	      return acc;
			    	  }) 
			      )
			      .filter(acc-> acc.getAvailablebalance()-mwithdrawrequest.getAmount()-mwithdrawrequest.getCommission()>=0)
			      .switchIfEmpty(Mono.error(new Exception("Dont have enought money"))) //no hay saldo disponible...
			      .flatMap(refresh-> {//sino si hay saldo disponible actualizar el setAvailablebalance (saldo disponible)
			    	  refresh.setAvailablebalance(refresh.getAvailablebalance()-mwithdrawrequest.getAmount()-mwithdrawrequest.getCommission());//(refresh.getAvailablebalance()- mwithdrawrequest.getAmount()-mwithdrawrequest.getCommission());
			    	  return accwebclient.put().body(BodyInserters.fromValue(refresh)).retrieve().bodyToMono(BankAccountDto.class);
			      })
			      .switchIfEmpty(Mono.error(new Exception("Error refresh account")))
			      .flatMap(then->            repository.save(Transaction.builder()
						                    .prodid(then.getId())
						                    .prodtype(then.getAcctype())
						                    .transtype("WITHDRAW")
						                    .headline(mwithdrawrequest.getTitular())
						                    .amount(mwithdrawrequest.getAmount())
						                    .commission(mwithdrawrequest.getCommission())
						                    .postamount(then.getAvailablebalance())
						                    .build())); 
	}
	
	/*DEPOSITO DINERO*/
	/*1. Validar el usuario que generó la solicitud existe(HTTP REQUEST)*/
	/*2. Genera un conteo del total transacciones procesadas y comparamos si es > a value parameter COMMISSION_FREE_TIMES entonces se añade el valor COMISSION.*/ 
	/*3. ACTUALIZAR el nuevo valor de saldo disponible para el microservicio de BANK ACCOUNT*/
	/*4. REGISTRAR transaccion como DEPOSIT */
	@Override
	public Mono<Transaction> moneydeposit(AccdepositRequest mdepositrequest, Mono<BankAccountDto> account,
			WebClient accwebclient) {
		return account.filter(acc-> acc.getHeadline().contains(mdepositrequest.getTitular()))
		              .switchIfEmpty(Mono.error(new Exception("Titular not found")))
				      .flatMap(acc-> repository.countTransacByTitular(mdepositrequest.getTitular())//(mdepositrequest.getTitular())
				    		  .flatMap(count ->
				              {      mdepositrequest.setCommission(count>=Configtransaction.COMMISSION_FREE_TIMES?Configtransaction.COMMISSION_DEPOSIT_VALUE:0); 
				    		         acc.setAvailablebalance(acc.getBalancetotal()+mdepositrequest.getAmount()-mdepositrequest.getCommission());//(acc.getBalance() + mdepositrequest.getAmount()-mdepositrequest.getCommission());
					    	         return accwebclient.put().body(BodyInserters.fromValue(acc)).retrieve().bodyToMono(BankAccountDto.class);
			                  })
				      )  
				      .switchIfEmpty(Mono.error(new Exception("Error refresh account")))
				      .flatMap(then->               repository.save(Transaction.builder()
								                    .prodid(then.getId())
								                    .prodtype(then.getAcctype())
								                    .transtype("DEPOSIT")
								                    .headline(mdepositrequest.getTitular())
								                    .amount(mdepositrequest.getAmount())
								                    .commission(mdepositrequest.getCommission())
								                    .postamount(then.getAvailablebalance())
								                    .build()));
	}
	
	
	/*Pago de crédito*/
	
	/*1. VALIDAR El Status de credito retornado si contiene el titular que generó la Solicitud Petición (HTTP REQUEST) */
	/*2. FILTRAR si el AMOUNT de la peticion es >  al consumo actual*/ 
	/*3. ACTUALIZAR Saldo de credito con los nuevos valores en el microservicio de credit*/
	/*4. REGISTRA la transaccion como consumo*/
	/*Se actualiza los consumos realizados por el usuario, se toma el mas antiguo y se va pagando de acuerdo a los montos de los consumos sin pagar*/
	
	@Override
	public Mono<Transaction> creditpayment(Creditpaymentrequest cpaymentrequest, Mono<CreditDto> credit,
			WebClient credwebclient) {
		// TODO Auto-generated method stub
		return  credit.filter(cred-> cred.getHeadline().contains(cpaymentrequest.getTitular())) 
				.switchIfEmpty(Mono.error(new Exception("Not credit found - cpayment"))) 
				.filter(cred -> cred.getConsume()-cpaymentrequest.getAmount()>=0)
				.switchIfEmpty(Mono.error(new Exception("Cant process the transaction")))
				.flatMap(refresh -> {
					refresh.setConsume(refresh.getConsume()-cpaymentrequest.getAmount());
					return credwebclient.put().body(BodyInserters.fromValue(refresh)).retrieve().bodyToMono(CreditDto.class);
				})
				 .switchIfEmpty(Mono.error(new Exception("Error refresh credit")))
				 .flatMap(then -> repository.save(Transaction.builder()
		                    .prodid(then.getId())
		                    .prodtype(then.getCredittype())
		                    .transtype("PAYMENT")
		                    .headline(cpaymentrequest.getTitular())
		                    .amount(cpaymentrequest.getAmount())
		                    .postamount(then.getBaseline()-then.getConsume())
		                    .build())) 
				 .flatMap(transaction-> {
					AtomicDouble amountss=new AtomicDouble();
					amountss.set(transaction.getAmount());
					return consumerepo.findByProductidAndPayedOrderByMonthAsc(transaction.getProdid(), false)  
					                  .map(consum ->{ 
					                	  if(amountss.doubleValue()>=consum.getNotpayedamount()){ 
					                		  amountss.set(amountss.doubleValue()-consum.getNotpayedamount());
					                		  consum.setNotpayedamount(0d);
					                		  consum.setPayed(true);
					                 	  }else{ 
					                	      consum.setNotpayedamount(consum.getNotpayedamount()-amountss.get());
					                		  amountss.set(0d); 
					                	  } 
					                	  return consum; 
					                   }).flatMap(consumerepo::save).then()
					                  .thenReturn(transaction); 
				  });
	}

	/* CONSUMIR CREDITO */
	/*1. VALIDAR si el credito existe el titular que generó la petición (HTTP REQUEST)*/
	/*2. Se filtra si el consumo de la peticion es mayor a la linea base del credito menos el consumo ya realizado anteriormente*/ 
	/*3. ACTUALIZAR el saldo del credito con los nuevos valores en el microservicio de CREDIT*/
	/*4. REGISTRAR un NUEVO consumo y se asigna como fecha de pago el dia final del mes siguiente*/
	/*Se registra la transaccion como consumo*/
	
	@Override
	public Mono<Transaction> creditconsume(Creditconsumerequest cconsumerequest, Mono<CreditDto> credit,
			WebClient credwebclient) {
		// TODO Auto-generated method stub
		return credit.filter(cred-> cred.getHeadline().contains(cconsumerequest.getTitular()))
	             .switchIfEmpty(Mono.error(new Exception("Not credit found  - cconsume"))) 
			     .filter(cred -> (cred.getBaseline()-cred.getConsume()-cconsumerequest.getAmount())>=0)
			     .switchIfEmpty(Mono.error(new Exception("Cant process the transaction")))
			     .flatMap(refresh -> {
				  refresh.setConsume(refresh.getConsume()+cconsumerequest.getAmount());
				  return credwebclient.put().body(BodyInserters.fromValue(refresh)).retrieve().bodyToMono(CreditDto.class) ;
				 }) 
			     .flatMap(then->  consumerepo.save(Consume.builder()
			    		   .amount(cconsumerequest.getAmount())
			    		   .notpayedamount(cconsumerequest.getAmount())
                          .productid(then.getId())
                          .titular(then.getHeadline())
                          .month(LocalDate.now())
                          .maxmonth(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().plusMonths(1L).getMonth(),LocalDate.now().plusMonths(1L).lengthOfMonth()))
                          .payed(false)
                          .build()).thenReturn(then)
                   )
			     .flatMap(then->  repository.save(Transaction.builder()
							                    .prodid(then.getId())
							                    .prodtype(then.getCredittype())
							                    .transtype("CONSUME")
							                    .headline(cconsumerequest.getTitular())
							                    .amount(cconsumerequest.getAmount())
							                    .postamount(then.getBaseline()-then.getConsume())
							                    .build())
			    	);
	}

	@Override
	public Mono<Transaction> transferpayment(Transferpaymentrequest tpaymentrequest, Mono<BankAccountDto> account,
			Mono<CreditDto> credit, WebClient accwebclient, WebClient credwebclient) {
		// TODO Auto-generated method stub
		return null;
	}

	/*1. VALIDAR si TRANSACITON por id no SEA NULL (HTTP REQUEST)*/
	/*2. Se procede a ELIMINAR la transaction por ID.*/
	
	@Override
	public Mono<Void> deletetransaction(String id) {
		Mono delete=null;
		 final Mono<Transaction> dbTransaction = repository.findById(id);
		  if (Objects.isNull(dbTransaction)) {
		   return Mono.empty();
		   
		  }
		  delete =  repository.findById(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull).flatMap(transactiontoBeDeleted -> repository
				    .delete(transactiontoBeDeleted).then(Mono.just(transactiontoBeDeleted)));
		 
	return delete;
	}
	
	/*1. Busqueda de todas las Transacciones Disponibles (HTTP REQUEST)*/
	/**/

	 
	public Flux<Transaction> findtransaction() {
		return repository.findAll();
	}
	

	/*1. Búsqueda de varias(FLUX) transactions BY ID CLIENT AND DATE INI AND DATE END*/
	/* */	
	@Override
	public Flux<Transaction> findclienttransaction(String idHeadLine, LocalDate dateIni, LocalDate dateEnd) {
		// TODO Autreposio-generated method stub
		return repository.findByTitularAndTransactdateBetween(idHeadLine, dateIni, dateEnd)
				.switchIfEmpty(Mono.error(new Exception("Not process transaction findclienttransaction..")));
	}

	
	/*1. Busqueda de una transaction por su Id Primary.*/
	
	@Override
	public Mono<Transaction> findtransactionbyid(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id)
				.switchIfEmpty(Mono.error(new Exception("No process transaction by Id findtransactionbyid..")));
	}

	/*1. Update model Transaction */
	
	@Override
	public Mono<Transaction> updatetransaction(Updatetransactionreq updatetransactionreq) {
		// TODO Auto-generated method stub
		return repository.findById(updatetransactionreq.getId())
				.switchIfEmpty(Mono.error(new Exception("No data.")))
				.flatMap(a->repository.save(Transaction.builder()
						.id(a.getId())
						.prodid(updatetransactionreq.getProdid())
						.prodtype(updatetransactionreq.getProdtype())
						.transtype(updatetransactionreq.getTranstype())
						.headline(updatetransactionreq.getTitular())
						.transactdate(updatetransactionreq.getTransactdate())
						.amount(updatetransactionreq.getAmount())
						.commission(updatetransactionreq.getCommission())
						.postamount(updatetransactionreq.getPostamount())			
						.build()));
	}

	 

	

	//Pago de una tarjeta de credito desde otra cuenta bancaria no perteneciente al mismo banco
		@Override
		public Mono<Transaction> multibankTransPay(Transferpaymentrequest tpaymentrequest,  Mono<BankAccountDto> account, Mono<CreditDto> credit, WebClient accwebclient,  WebClient credwebclient){
			return  account.filter(acc->acc.getHeadline().contains(tpaymentrequest.getAccounttitular()))
					.switchIfEmpty(Mono.error(new Exception("Not same account holder - transferpayment")))
					.flatMap(acc-> 
					repository.countTransacByTitular(tpaymentrequest.getAccounttitular()).map(count ->
			    	  {   tpaymentrequest.setCommission(count>=Configtransaction.COMMISSION_FREE_TIMES?Configtransaction.COMMISSION_WITHDRAW_VALUE:0);  
			    	      return acc;
			    	  }) 
			         )
					.filter(acc-> acc.getAvailablebalance()-tpaymentrequest.getAmount()-tpaymentrequest.getCommission()>=0)
					        .switchIfEmpty(Mono.error(new Exception("Cant process the transaction - low account balance")))
					.then(credit)
					.filter(cred-> cred.getCredittype().equalsIgnoreCase("CRE4")||cred.getCredittype().equalsIgnoreCase("CRE3"))
		           			.switchIfEmpty(Mono.error(new Exception("Credit not credit card")))		
					.filter(cred-> cred.getHeadline().contains(tpaymentrequest.getCredittitular()))
					        .switchIfEmpty(Mono.error(new Exception("Not same credit holder - transferpayment")))
					.filter(cred -> (cred.getConsume()-tpaymentrequest.getAmount())>=0)
					        .switchIfEmpty(Mono.error(new Exception("Cant process the transaction - amount")))
					.flatMap(cre->{
	   			            cre.setConsume(cre.getConsume()-tpaymentrequest.getAmount());
						      return credwebclient.put().body(BodyInserters.fromValue(cre)).retrieve().bodyToMono(CreditDto.class) ;
					})
					.switchIfEmpty(Mono.error(new Exception("Cant process the transcation - credit")))
					.flatMap(cre->repository.save(Transaction.builder()
		                         .prodid(cre.getId())
		                   	     .prodtype(tpaymentrequest.getProdtype()) 
		                   	     .transtype("TRANSPAYMENT")
		                   	     .headline(tpaymentrequest.getCredittitular())
				                 .bank(cre.getBank())
		                   	     .amount(tpaymentrequest.getAmount())
		                   	     .commission(tpaymentrequest.getCommission())
		                   	     .postamount(cre.getConsume()-tpaymentrequest.getAmount()-tpaymentrequest.getCommission()) 
		                         .build()))
					.switchIfEmpty(Mono.error(new Exception("Cant process the transaction - creditransaction")))
					.flatMap(transaction-> {
						AtomicDouble amountss=new AtomicDouble();
						amountss.set(transaction.getAmount());
						return consumerepo.findByProductidAndPayedOrderByMonthAsc(transaction.getProdid(), false)  
						                  .map(consum ->{ 
						                	  if(amountss.doubleValue()>=consum.getNotpayedamount()){ 
						                		  amountss.set(amountss.doubleValue()-consum.getNotpayedamount());
						                		  consum.setNotpayedamount(0d);
						                		  consum.setPayed(true);
						                 	  }else{ 
						                	      consum.setNotpayedamount(consum.getNotpayedamount()-amountss.get());
						                		  amountss.set(0d); 
						                	  } 
						                	  return consum; 
						                   }).flatMap(consumerepo::save).then()
						                  .thenReturn(transaction); 
					  })
					.then(account)
					.flatMap(acc-> {
				    	  acc.setBalancetotal(acc.getAvailablebalance()- tpaymentrequest.getAmount()-tpaymentrequest.getCommission());
				    	  return accwebclient.put().body(BodyInserters.fromValue(acc)).retrieve().bodyToMono(BankAccountDto.class);
				    	  })
					.switchIfEmpty(Mono.error(new Exception("Cant process the transaction - account")))
					.flatMap(then-> repository.save(Transaction.builder()
		                    .prodid(then.getId())
		                    .prodtype(then.getAcctype())
		                    .transtype("TRANSWITHDRAW")
		                    .bank(then.getBankId())
		                    .headline(tpaymentrequest.getAccounttitular())
		                    .amount(tpaymentrequest.getAmount())
		                    .commission(tpaymentrequest.getCommission())
		                    .postamount(then.getAvailablebalance())
		                    .build()))
					 ;
		}

		@Override
		public Mono<Transaction> creditpaymentmultiBank(Creditpaymentmultibankrequest cpaymentmultibankrequest,
				Mono<BankAccountDto> accountpayment, Mono<CreditDto> creditpayment, WebClient accwebclient,
				WebClient credwebclient) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Mono<Boolean> checkforexpiredcredit(String titular) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Mono<TransactionResponse> depositatm(ATMTransactionDTO atmrequest, Mono<BankAccountDto> atmwc,
				WebClient webclient) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Mono<TransactionResponse> withdrawatm(ATMTransactionDTO atmrequest, Mono<BankAccountDto> atmwc,
				WebClient webclient) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
}
