package com.app.wizard;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.async.DeferredResult;

import com.app.model.Customer;
import com.app.validator.CustomerValidator;
import com.github.javafaker.Faker;
//https://agung-setiawan.com/spring-mvc-membuat-form-wizard/#
@Controller
@RequestMapping("wizard")
@SessionAttributes("customer")
public class WizardController {
	 @Autowired
	    CustomerValidator validator;
	 private final Queue<CustomDeferedResult> responseBodyQueue = new ConcurrentLinkedQueue<>();

	 
	@RequestMapping(value = "form-0")
    public String wizard0(Model model){
         return "wizard0";
    }
	
	
	@RequestMapping(value = "form-1")
    public CustomDeferedResult wizard1(Model model){
		CustomDeferedResult output = new CustomDeferedResult(model,"wizard1");
		   responseBodyQueue.add(output);
		
		/* ForkJoinPool.commonPool().submit(() -> {
		      
		        try {
		            Thread.sleep(6000);
		        } catch (InterruptedException e) {
		        }
		        model.addAttribute("customer", new Customer());
		        output.setResult("wizard1");
		    });
		*/
		 
		 
      
		 //  model =   output.getModel();
		 
		 return output;
		 
         
    }
	
	
	
	
	@RequestMapping(value = "form-2",method = RequestMethod.POST)
    public String wizard2(Model model,@ModelAttribute("customer") Customer customer, BindingResult result){
		validator.validatePage1(customer, result);
		if(result.hasErrors()){
            return "wizard1";
        }
		// model.addAttribute("customer", new Customer());
        return "wizard2";
    }
	
	@Scheduled(fixedRate = 5000)
	   public void processQueues() {
	   for (CustomDeferedResult result : responseBodyQueue) {
	        
		 
		   	String view = result.getView();
		   	HashMap<String,Object> param = new HashMap<String, Object>();
			Faker faker = new Faker();
	    	String name = faker.name().fullName();
		   Customer customer = new Customer();
		   customer.setName(name);
	    	result.getModel().addAttribute("customer", customer);
		   
	    	
		    result.setResult("wizard1");
			 
	           responseBodyQueue.remove(result);
	       }
	   }
    
}
