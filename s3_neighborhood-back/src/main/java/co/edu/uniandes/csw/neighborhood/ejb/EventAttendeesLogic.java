/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.neighborhood.ejb;

import co.edu.uniandes.csw.neighborhood.entities.EventEntity;
import co.edu.uniandes.csw.neighborhood.entities.ResidentProfileEntity;
import co.edu.uniandes.csw.neighborhood.exceptions.BusinessLogicException;

import co.edu.uniandes.csw.neighborhood.persistence.ResidentProfilePersistence;
import co.edu.uniandes.csw.neighborhood.persistence.EventPersistence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author albayona
 */
@Stateless
public class EventAttendeesLogic {

    private static final Logger LOGGER = Logger.getLogger(ResidentProfileLogic.class.getName());

    @Inject
    private ResidentProfilePersistence attendeePersistence;

    @Inject
    private EventPersistence eventPersistence;

    /**
     * Associates attendee with event
     *
     * @param neighId parent neighborhood
     * @param eventId id from event entity
     * @param attendeeId id from attendee
     * @return associated attendee
     */
    public ResidentProfileEntity associateAttendeeToEvent(Long neighId, Long eventId, Long attendeeId) {
        LOGGER.log(Level.INFO, "Initiating association between attendee with id {0} and  event with id {1}, from neighbothood {2}", new Object[]{attendeeId, eventId, neighId});
        EventEntity eventEntity = eventPersistence.find(neighId, eventId);
        ResidentProfileEntity attendeeEntity = attendeePersistence.find(attendeeId, neighId);

        eventEntity.getAttendees().add(attendeeEntity);

        LOGGER.log(Level.INFO, "Association created between attendee with id {0} and event with id {1}, from neighbothood {2}", new Object[]{attendeeId, eventId, neighId});
        return attendeePersistence.find(attendeeId, neighId);
    }

    /**
     * Gets a collection of attendee entities associated with event
     *
     * @param neighId parent neighborhood
     * @param eventId id from event entity
     * @return collection of attendee entities associated with event
     */
    public List<ResidentProfileEntity> getAttendees(Long neighId, Long eventId) {
        LOGGER.log(Level.INFO, "Gets all attendees belonging to event with id = {0} from neighborhood {1}", new Object[]{eventId, neighId});
        return eventPersistence.find(neighId, eventId).getAttendees();
    }

    /**
     * Gets attendee associated with event
     *
     * @param neighId parent neighborhood
     * @param eventId id from event
     * @param attendeeId id from associated entity
     * @return associated attendee
     * @throws BusinessLogicException If attendee is not associated
     */
    public ResidentProfileEntity getAttendee(Long neighId, Long eventId, Long attendeeId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Initiating query about attendee with id {0} from event with id {1}, from neighbothood {2}", new Object[]{attendeeId, eventId, neighId});

        List<ResidentProfileEntity> attendees = eventPersistence.find(neighId, eventId).getAttendees();
        ResidentProfileEntity attendeeResidentProfiles = attendeePersistence.find(attendeeId, neighId);

        int index = attendees.indexOf(attendeeResidentProfiles);

        LOGGER.log(Level.INFO, "Finish query about attendee with id {0} from event with id {1}, from neighbothood {2}", new Object[]{attendeeId, eventId, neighId});

        if (index >= 0) {
            return attendees.get(index);

        }
        throw new BusinessLogicException("There is no association between attendee and event");
    }

    /**
     * Replaces attendees associated with event
     *
     * @param neighId parent neighborhood
     * @param eventId id from event
     * @param attendees collection of attendee to associate with event
     * @return A new collection associated to event
     */
    public List<ResidentProfileEntity> replaceAttendees(Long neighId, Long eventId, List<ResidentProfileEntity> attendees) {
        LOGGER.log(Level.INFO, "Trying to replace attendees related to event with id {0} from neighborhood {1}", new Object[]{eventId, neighId});
        EventEntity eventEntity = eventPersistence.find(neighId, eventId);
        List<ResidentProfileEntity> attendeeList = attendeePersistence.findAll(neighId);
        for (ResidentProfileEntity attendee : attendeeList) {
            if (attendees.contains(attendee)) {
                if (!attendee.getAttendedEvents().contains(eventEntity)) {
                    attendee.getAttendedEvents().add(eventEntity);
                }
            } else {
                attendee.getAttendedEvents().remove(eventEntity);
            }
        }
        eventEntity.setAttendees(attendees);
        LOGGER.log(Level.INFO, "Ended trying to replace attendees related to event with id {0} from neighborhood {1}", new Object[]{eventId, neighId});
        return eventEntity.getAttendees();
    }

    /**
     * Unlinks attendee from event
     *
     * @param neighId parent neighborhood
     * @param eventId Id from event
     * @param attendeeId Id from attendee
     */
    public void removeAttendee(Long neighId, Long eventId, Long attendeeId) {
        LOGGER.log(Level.INFO, "Deleting association between attendee with id {0} and  event with id {1}, from neighbothood {2}", new Object[]{attendeeId, eventId, neighId});
        ResidentProfileEntity attendeeEntity = attendeePersistence.find(attendeeId, neighId);
        EventEntity eventEntity = eventPersistence.find(neighId, eventId);
        eventEntity.getAttendees().remove(attendeeEntity);
        LOGGER.log(Level.INFO, "Association deleted between attendee with id {0} and  event with id {1}, from neighbothood {2}", new Object[]{attendeeId, eventId, neighId});
    }

}
