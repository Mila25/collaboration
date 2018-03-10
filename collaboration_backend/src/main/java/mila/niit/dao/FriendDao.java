package mila.niit.dao;

import java.util.List;

import mila.niit.model.Friend;
import mila.niit.model.User;

public interface FriendDao {

	List<User> listOfSuggestedUsers(String email);
	void addFriendRequest(Friend friend);
	List<Friend> getAllPendingRequests(String email);
	void updateFriendRequest(Friend friend);
	List<User> listOfFriends(String email);
}
