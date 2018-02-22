package mila.niit.dao;

import mila.niit.model.BlogPost;
import mila.niit.model.BlogPostLikes;

public interface BlogPostLikesDao {
BlogPostLikes hasUserLikedPost(int postId,String email);
BlogPost updateLikes(int postId,String email);
}