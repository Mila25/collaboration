package mila.niit.dao;

import java.util.List;

import mila.niit.model.Job;

public interface JobDao {
void addJob(Job job);
List<Job> getAllJobs();
Job getJob(int id);
}