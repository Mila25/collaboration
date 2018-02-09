package mila.niit.dao;
import mila.niit.model.User;

public interface UserDAO {
	
	 void registration(User user);
	 boolean isEmailValid(String email);
	 User login(User user);
	

}
