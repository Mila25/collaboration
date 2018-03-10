package mila.niit.controller;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mila.niit.dao.BlogPostDao;
import mila.niit.dao.BlogPostLikesDao;
import mila.niit.dao.UserDAO;
import mila.niit.model.BlogComment;
import mila.niit.model.BlogPost;
import mila.niit.model.BlogPostLikes;
import mila.niit.model.ErrorClass;
import mila.niit.model.User;



@Controller
public class BlogPostController {
	@Autowired
	private BlogPostDao blogPostDao;
	@Autowired
	private UserDAO UserDAO;
	@Autowired
	private BlogPostLikesDao blogPostLikesDao;
	
	@RequestMapping(value="/addblogpost",method=RequestMethod.POST)
	public ResponseEntity<?> addBlogPost(@RequestBody BlogPost blogPost,HttpSession session){
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(4,"Unauthorized access please login");
					return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		blogPost.setPostedOn(new Date());
		User postedBy=UserDAO.getUser(email);
		blogPost.setPostedBy(postedBy);
		try {
			blogPostDao.addBlogPost(blogPost);
			return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
		}catch(Exception e) {
			ErrorClass error=new ErrorClass(7,"Unable to insert blogpost details");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@RequestMapping(value="/getblogs/{approved}",method=RequestMethod.GET)
	public ResponseEntity<?> getBlogs(@PathVariable boolean approved,HttpSession session){
		String email=(String)session.getAttribute("loginId");
		if(email==null){
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		if(!approved){
			User user=UserDAO.getUser(email);
			if(!user.getRole().equals("ADMIN")){
				ErrorClass error=new ErrorClass(4,"Access Denied..");
				return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
			}
		}
		List<BlogPost> blogs=blogPostDao.getBlogs(approved);
		return new ResponseEntity<List<BlogPost>>(blogs,HttpStatus.OK);
	}
	

	@RequestMapping(value="/getblog/{id}",method=RequestMethod.GET)
	public ResponseEntity<?> getBlogPost(@PathVariable int id,HttpSession session){
		String email=(String)session.getAttribute("loginId");
		if(email==null){
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED); //2nd callback function
		}
		User user=UserDAO.getUser(email);
		BlogPost blogPost=blogPostDao.getBlogById(id);
		if(!blogPost.isApproved())
			if(!user.getRole().equals("ADMIN")){
				ErrorClass error=new ErrorClass(4,"Access Denied..");
				return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
			}
			
		return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
	}
	

	@RequestMapping(value="/haspostliked/{id}",method=RequestMethod.GET)
	public ResponseEntity<?> hasUserLikedBlogPost(@PathVariable int id,HttpSession session){
		String email=(String)session.getAttribute("loginId");
		if(email==null){
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED); //2nd callback function
		}
		BlogPostLikes blogPostLikes=blogPostLikesDao.hasUserLikedPost(id, email);
		return new ResponseEntity<BlogPostLikes>(blogPostLikes,HttpStatus.OK);
		
	}

	@RequestMapping(value="/updatelikes/{id}",method=RequestMethod.PUT)
	public ResponseEntity<?> updateLikes(@PathVariable int id,HttpSession session){
		//id is 735
		String email=(String)session.getAttribute("loginId");
		if(email==null){
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		
		BlogPost blogPost=blogPostLikesDao.updateLikes(id, email);
		return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
	}

	@RequestMapping(value="/blogapproved/{id}",method=RequestMethod.PUT)
	public ResponseEntity<?> blogApproved(@PathVariable int id,HttpSession session){
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		User user=UserDAO.getUser(email);
		if(!user.getRole().equals("ADMIN")){
			ErrorClass error=new ErrorClass(4,"Access Denied..");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		blogPostDao.blogApproved(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/blogrejected/{id}/{rejectionReason}",method=RequestMethod.PUT)
	public ResponseEntity<?> blogRejected(@PathVariable int id,@PathVariable String rejectionReason,HttpSession session){
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		User user=UserDAO.getUser(email);
		if(!user.getRole().equals("ADMIN")){
			ErrorClass error=new ErrorClass(4,"Access Denied..");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		blogPostDao.blogRejected(id,rejectionReason);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/addblogcomment",method=RequestMethod.POST)
	
	public ResponseEntity<?> addBlogComment(@RequestBody BlogComment blogComment,HttpSession session){
		
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		}
		try {
			blogComment.setCommentedOn(new Date());
			User commentedBy=UserDAO.getUser(email);
			blogComment.setCommentedBy(commentedBy);
			
			
			blogPostDao.addBlogComment(blogComment);
			return new ResponseEntity<BlogComment> (blogComment,HttpStatus.OK);
		}catch(Exception e) {
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	@RequestMapping(value="/getblogcomments/{blogPostId}",method=RequestMethod.GET)
	public ResponseEntity<?> getBlogComments(@PathVariable int blogPostId,HttpSession session){
		
		String email=(String)session.getAttribute("loginId");
		if(email==null) {
			ErrorClass error=new ErrorClass(4,"Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClass>(error,HttpStatus.UNAUTHORIZED);
		
	}
		List<BlogComment> blogComments=blogPostDao.getAllBlogComments(blogPostId);
		return new ResponseEntity<List<BlogComment>>(blogComments,HttpStatus.OK);
		
	}
}