package mila.niit.dao;

import mila.niit.model.ProfilePicture;

public interface ProfilePictureDao {
void uploadProfilePicture(ProfilePicture profilePicture);
ProfilePicture getProfilePicture(String email);
}