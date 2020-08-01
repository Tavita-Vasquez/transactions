package ws.rest.springcloud.service.impl;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.mstransact.config.Configtransaction; 

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
import ws.rest.springcloud.repository.ITransactionRepository;


@Service
public class TransactionServiceImpl implements ITransactionService {

	@Autowired
	private ITransactionRepository repository;

	/*
	 * @Override public Mono<Transaction> moneywithdraw(AccwithdrawRequest
	 * mwithdrawrequest, Mono<BankAccountDto> account, WebClient accwebclient) {
	 * return account.filter(acc->
	 * acc.getHeadline().contains(mwithdrawrequest.getTitular()))
	 * .switchIfEmpty(Mono.error(new Exception("HeadLine not found")))
	 * .flatMap(acc->
	 * repository.countTransacByTitular((mwithdrawrequest.getTitular()).map(count ->
	 * {
	 * mwithdrawrequest.setCommission(count>=Configtransaction.COMMISSION_FREE_TIMES
	 * ?Configtransaction.COMMISSION_WITHDRAW_VALUE:0); return acc; }) )
	 * .filter(acc->
	 * acc.getBalance()-mwithdrawrequest.getAmount()-mwithdrawrequest.getCommission(
	 * )>=0) .switchIfEmpty(Mono.error(new Exception("Dont have enought money")))
	 * .flatMap(refresh-> { refresh.setBalance(refresh.getBalance()-
	 * mwithdrawrequest.getAmount()-mwithdrawrequest.getCommission()); return
	 * webclient.put().body(BodyInserters.fromValue(refresh)).retrieve().bodyToMono(
	 * AccountDto.class); }) .switchIfEmpty(Mono.error(new
	 * Exception("Error update account"))) .flatMap(then->
	 * repository.save(Transaction.builder() .prodid(then.getId())
	 * .prodtype(then.getAcctype()) .transtype("WITHDRAW")
	 * .titular(mwithdrawrequest.getTitular()) .amount(mwithdrawrequest.getAmount())
	 * .commission(mwithdrawrequest.getCommission()) .postamount(then.getBalance())
	 * .build())); }
	 */

	
	/*
	 * public Mono<Transaction> moneydeposit(AccdepositRequest mdepositrequest,
	 * Mono<BankAccountDto> account,WebClient accwebclient) { return
	 * account.filter(acc->acc.getHeadline().contains(mdepositrequest.getTitular()))
	 * .switchIfEmpty(Mono.error(new Throwable("Headline not found")))
	 * .flatMap(acc-> repository.countTransacByTitular(mdepositrequest.getTitular())
	 * .flatMap(count -> {
	 * mdepositrequest.setCommission(count>=Configtransaction.COMMISSION_FREE_TIMES?
	 * Configtransaction.COMMISSION_DEPOSIT_VALUE:0);
	 * acc.setAvailablebalance(acc.getAvailablebalance()+mdepositrequest.getAmount()
	 * -mdepositrequest.getCommission()); return
	 * webclient.put().body(BodyInserters.fromValue(acc)).retrieve().bodyToMono(
	 * BankAccountDto.class); }) ) .switchIfEmpty(Mono.error(new
	 * Exception("Error refresh account"))) .flatMap(then->
	 * repository.save(Transaction.builder() .prodid(then.getId())
	 * .prodtype(mdepositrequest.getProdtype()) .transtype("DEPOSIT")
	 * .titular(mdepositrequest.getTitular()) .amount(mdepositrequest.getAmount())
	 * .commission(mdepositrequest.getCommission()) .postamount(then.getSaldo())
	 * .build())); }
	 */
	@Override
	public Mono<Transaction> creditpayment(Creditpaymentrequest cpaymentrequest, Mono<CreditDto> credit,
			WebClient credwebclient) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Transaction> creditconsume(Creditconsumerequest cpaymentrequest, Mono<CreditDto> credit,
			WebClient credwebclient) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Transaction> transferpayment(Transferpaymentrequest tpaymentrequest, Mono<BankAccountDto> account,
			Mono<CreditDto> credit, WebClient accwebclient, WebClient credwebclient) {
		// TODO Auto-generated method stub
		return null;
	}

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

	@Override
	public Flux<Transaction> findtransaction() {
		return repository.findAll();
	}

	@Override
	public Flux<Transaction> findclienttransaction(String idHeadLine, LocalDate dateIni, LocalDate dateEnd) {
		// TODO Autreposio-generated method stub
		return repository.findByTitularAndTransactdateBetween(idHeadLine, dateIni, dateEnd)
				.switchIfEmpty(Mono.error(new Exception("Not process transaction findclienttransaction..")));
	}

	@Override
	public Mono<Transaction> findtransactionbyid(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id)
				.switchIfEmpty(Mono.error(new Exception("No process transaction by Id findtransactionbyid..")));
	}

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
						.idHeadLine(updatetransactionreq.getTitular())
						.transactdate(updatetransactionreq.getTransactdate())
						.amount(updatetransactionreq.getAmount())
						.commission(updatetransactionreq.getCommission())
						.postamount(updatetransactionreq.getPostamount())			
						.build()));
	}

	 

	@Override
	public Mono<Boolean> checkforexpiredcredit(String titular) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Transaction> moneywithdraw(AccwithdrawRequest mwithdrawrequest, Mono<BankAccountDto> account,
			WebClient accwebclient) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Transaction> moneydeposit(AccdepositRequest mdepositrequest, Mono<BankAccountDto> account,
			WebClient accwebclient) {
		// TODO Auto-generated method stub
		return null;
	}

 
	
	
	
}
