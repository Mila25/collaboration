package mila.niit.dao;

import java.util.List;

import mila.niit.model.BlogPost;



public interface BlogPostDao {
	
	void addBlogPost(BlogPost blogPost);
	List<BlogPost> getBlogs(boolean approved);
	BlogPost getBlogById(int id);
	void blogApproved(int id);
	void blogRejected(int id,String rejectionReason);
}