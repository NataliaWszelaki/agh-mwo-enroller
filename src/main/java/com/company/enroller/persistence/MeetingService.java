package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import org.springframework.web.bind.annotation.RequestParam;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Meeting findById(long id) {
        return (Meeting) connector.getSession().get(Meeting.class, id);
    }

    public void addMeeting(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(meeting);
        transaction.commit();
    }

    public void deleteMeeting(long id) {
        Meeting deletedMeeting = findById(id);
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(deletedMeeting);
        transaction.commit();
    }


    public void updateMeeting(Meeting meeting) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(meeting);
        transaction.commit();
    }
}
