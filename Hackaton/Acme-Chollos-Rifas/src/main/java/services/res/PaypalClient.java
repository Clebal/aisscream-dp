package services.res;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

public class PaypalClient {

	private String clientId = "AW1jnKlMWtcJ89S06Cb_3wvUC2EezkhFwKCm0oJWov6wExVxI1q6rKr1My3Hafc6s41rJc-yx-etXV1q";
	private String clientSecret = "EA68rBYMMp1OYW9Oc_IF4TIBC7_AND2M6j9baEb1-ijh7l_8qgXgZ1moX_rJjX2SKAa4wGsyCG3fp86_";
	
	public Map<String, Object> createPayment(String sum, int raffleId, int amountItem, HttpServletRequest request){
		
	    Map<String, Object> response = new HashMap<String, Object>();
  
	    Amount amount = new Amount();
	    amount.setCurrency("EUR");
	    amount.setTotal(sum);
	    
	    Transaction transaction = new Transaction();
	    transaction.setAmount(amount);
	    
	    List<Transaction> transactions = new ArrayList<Transaction>();
	    transactions.add(transaction);

	    Payer payer = new Payer();
	    payer.setPaymentMethod("paypal");

	    Payment payment = new Payment();
	    payment.setIntent("sale");
	    payment.setPayer(payer);
	    payment.setTransactions(transactions);
  
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int portNumber = request.getServerPort();
        String contextPath = request.getContextPath();
        
	    RedirectUrls redirectUrls = new RedirectUrls();
	    redirectUrls.setCancelUrl(scheme+"://"+serverName+":"+portNumber+contextPath+"/raffle/display.do?raffleId="+raffleId);
	    redirectUrls.setReturnUrl(scheme+"://"+serverName+":"+portNumber+contextPath+"/ticket/user/completepayment.do?raffleId="+raffleId+"&amount="+amountItem);
	    payment.setRedirectUrls(redirectUrls);
	    
	    Payment createdPayment;
	    
	    try {
	        String redirectUrl = "";
	        APIContext context = new APIContext(clientId, clientSecret, "sandbox");
	        createdPayment = payment.create(context);
	        if(createdPayment!=null){
	            List<Links> links = createdPayment.getLinks();
	            for (Links link:links) {
	                if(link.getRel().equals("approval_url")){
	                    redirectUrl = link.getHref();
	                    break;
	                }
	            }
	            response.put("status", "success");
	            response.put("redirect_url", redirectUrl);
	        }
	    } catch (PayPalRESTException e) {
	        System.out.println("Error happened during payment creation!");
	    }
	    return response;
	}
	
	public Map<String, Object> completePayment(final String paymentId, final String PayerId){
	    Map<String, Object> response = new HashMap<String, Object>();
	    Payment payment = new Payment();
	    payment.setId(paymentId);

	    PaymentExecution paymentExecution = new PaymentExecution();
	    paymentExecution.setPayerId(PayerId);
	    try {
	        APIContext context = new APIContext(clientId, clientSecret, "sandbox");
	        Payment createdPayment = payment.execute(context, paymentExecution);
	        if(createdPayment!=null){
	            response.put("status", "success");
	            response.put("payment", createdPayment);
	        }
	    } catch (PayPalRESTException e) {
	        System.err.println(e.getDetails());
	    }
	    return response;
	}
	
	
}
