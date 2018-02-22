/**
 * BlogService
 */
app.factory('BlogService',function($http){
	var blog={}
		var BASE_URL="http://localhost:8093/collaboration_middleware"
			
			blog.addBlog=function(blog){
			return $http.post(BASE_URL + "/addblogpost",blog)
		}
		blog.getBlogsWaitingForApproval=function(){
			return $http.get(BASE_URL + "/getblogs/"+false)
		}
		
		blog.getApprovedBlogs=function(){
			return $http.get(BASE_URL + "/getblogs/"+true)
		}
		
		blog.getBlog=function(id){
			return $http.get(BASE_URL + "/getblog/"+id)
		}
		

	    blog.hasUserLikedPost=function(id){
	    	return $http.get(BASE_URL + "/haspostliked/"+id);
	    }
	    
	    blog.updateLikes=function(id){
	    	return $http.put(BASE_URL + "/updatelikes/"+id);
	    }
	    blog.blogApproved=function(id){
	    	return $http.put(BASE_URL + "/blogapproved/"+id);
	    }
	    blog.blogRejected=function(id,rejectionReason){
	    	return $http.put(BASE_URL + '/blogrejected/'+id + "/"+rejectionReason);
	    }


		return blog;
})