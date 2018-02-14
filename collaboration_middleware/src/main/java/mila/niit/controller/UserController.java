package mila.niit.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mila.niit.dao.UserDAO;
import mila.niit.model.ErrorClass;
import mila.niit.model.User;

@RestController
public class UserController {
	@Autowired
	private UserDAO userDAO;	
public UserController(){
	System.out.println("UserController INSTANTIATED");
}
@RequestMapping(value="/register",method=RequestMethod.POST)
public ResponseEntity<?> registration(@RequestBody User user){
	if(!userDAO.isEmailValid(user.getEmail())){
		ErrorClass error=new ErrorClass(2,"Email Id already exists.. please enter different email address");
		return new ResponseEntity<ErrorClass>(error, HttpStatus.CONFLICT);//409 //2nd callback func(error)
	}
	try{
	userDAO.registration(user);
	return new ResponseEntity<User>(user,HttpStatus.CREATED);//1st callback fun (success)
	}catch(Exception e){
		ErrorClass error=new ErrorClass(1,"Unable to register user details "+e.getMessage());
		return new ResponseEntity<ErrorClass>(error,HttpStatus.INTERNAL_SERVER_ERROR);//2nd callback func
	}
}
@RequestMapping(value="/login",method=RequestMethod.POST)
//{'email':'john.s@abc.com','password':'123'}  - i/p
public ResponseEntity<?> login(@RequestBody User user){
	User validUser=userDAO.login(user);
	if(validUser==null)//invalid credentials, error
	{
		ErrorClass error=new ErrorClass(3,"Invalid email id/password.. please enter valid credentials");
		return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);//401
	}
	else{//valid credentials, success
		return new ResponseEntity<User>(validUser,HttpStatus.OK);
	}
}

}
