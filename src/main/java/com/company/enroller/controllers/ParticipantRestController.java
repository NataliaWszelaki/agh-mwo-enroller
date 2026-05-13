package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(
            @RequestParam(value = "key", defaultValue = "") String key,
            @RequestParam(value = "sortBy", defaultValue = "") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {

		Collection<Participant> participants = participantService.getAll(key, sortBy, sortOrder);

		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("login") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        Participant foundParticipant = participantService.findByLogin(participant.getLogin());
        if (foundParticipant != null) {
            return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin()
                    + " already exists.", HttpStatus.CONFLICT);
        }
        participantService.addParticipant(participant);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("login") String login) {
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundParticipant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        participantService.deleteParticipant(login);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@PathVariable("login") String login, @RequestBody Participant participant)  {
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundParticipant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        participantService.updateParticipant(participant);
        return new ResponseEntity<>("Participant has been updated " + foundParticipant, HttpStatus.OK);
    }
}
