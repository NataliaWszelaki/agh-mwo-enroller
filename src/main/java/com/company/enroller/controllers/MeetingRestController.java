package com.company.enroller.controllers;

import java.util.Collection;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings(
            @RequestParam(value = "key", defaultValue = "") String key,
            @RequestParam(value = "sortBy", defaultValue = "") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
        Collection<Meeting> meetings = meetingService.getAll(key, sortBy, sortOrder);
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if(meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
        meetingService.addMeeting(meeting);
        return new ResponseEntity<>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id){
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meetingService.deleteMeeting(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meeting.setId(id);
        meetingService.updateMeeting(meeting);
        return new ResponseEntity<>("Meeting has been updated " + meeting.getTitle(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if(meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<Participant> participants = meeting.getParticipants();
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @RequestBody Participant participant) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Participant foundParticipant = participantService.findByLogin(participant.getLogin());
        if (foundParticipant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meeting.addParticipant(foundParticipant);
        meetingService.updateMeeting(meeting);
        return new ResponseEntity<>("Participant " + foundParticipant.getLogin()
                + " has been added to the meeting: " + meeting.getTitle(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipantFromMeeting(@PathVariable("id") long id, @PathVariable("login") String login) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundParticipant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meeting.removeParticipant(foundParticipant);
        meetingService.updateMeeting(meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
