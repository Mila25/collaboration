package mila.niit.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mila.niit.dao.JobDao;
import mila.niit.dao.UserDAO;
import mila.niit.model.ErrorClass;
import mila.niit.model.Job;
import mila.niit.model.User;

@RestController
public class JobController {
	@Autowired
	private JobDao jobDao;
	@Autowired
	private UserDAO userDAO;
	

	@RequestMapping(value="/addjob",method=RequestMethod.POST)
	
	public ResponseEntity<?> addJob(@RequestBody Job job,HttpSession session){
		
		String email=(String)session.getAttribute("loginId");
		
		if(email==null) {
				ErrorClass error=new ErrorClass(4,"Unauthorized access....please login");
				return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		User user=userDAO.getUser(email);
		if(!user.getRole().equals("ADMIN")) {
			ErrorClass error=new ErrorClass(5,"Access Denied");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);

		}
		try {
			job.setPostedOn(new Date());
			jobDao.addJob(job);
			return new ResponseEntity<Job>(job,HttpStatus.OK);
		}catch(Exception e)
		{
			ErrorClass error=new ErrorClass(6,"Unable to insert job details...fields are empty");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	
		}
		
			
	}

       @RequestMapping(value="/getalljobs",method=RequestMethod.GET)	
	   public ResponseEntity<?> getAllJobs(HttpSession session){
    	String email=(String)session.getAttribute("loginId");
		if(email==null){//not logged in [Authenticated]
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED); //login page
		}
		List<Job> jobs=jobDao.getAllJobs();
		return new ResponseEntity<List<Job>>(jobs,HttpStatus.OK);//success
	}
       

       @RequestMapping(value="/getjob/{id}",method=RequestMethod.GET)
       public ResponseEntity<?> getJob(@PathVariable int id, HttpSession session){
       	String email=(String)session.getAttribute("loginId");
   		if(email==null){//not logged in [Authenticated]
   			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
   			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED); //login page
   		}
   		Job job=jobDao.getJob(id);
   		return new ResponseEntity<Job>(job,HttpStatus.OK);
	
	
       }

}