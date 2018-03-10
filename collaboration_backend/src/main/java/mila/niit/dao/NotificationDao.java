package mila.niit.dao;

import java.util.List;

import mila.niit.model.Notification;

public interface NotificationDao {
List<Notification> getAllNotification(String email);//login id
void updateViewedStatus(int notificationId);
Notification getNotification(int id);
}