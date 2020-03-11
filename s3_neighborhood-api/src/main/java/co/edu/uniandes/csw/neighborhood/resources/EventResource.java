/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.neighborhood.resources;

import co.edu.uniandes.csw.neighborhood.dtos.EventDTO;
import co.edu.uniandes.csw.neighborhood.dtos.EventDetailDTO;
import co.edu.uniandes.csw.neighborhood.ejb.EventLogic;
import co.edu.uniandes.csw.neighborhood.entities.EventEntity;
import co.edu.uniandes.csw.neighborhood.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

/**
 * Class implementing resource "events".
 *
 * @author K.romero
 
 */
@Path("events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class EventResource {

    private static final Logger LOGGER = Logger.getLogger(EventResource.class.getName());

    @Inject
    private EventLogic eventLogic;

    /**
     * Creates a new event with the information received in request, then an
     * identical object is returned with an id generated by DB
     *
     * @param event {@link EventDTO} - Resident to be persisted
     * @return JSON {@link EventDTO} - Auto-generated event to be persisted
     */
    @POST
    public EventDTO createResident(EventDTO event) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Creating event from resource: input: {0}", event);

        EventDTO eventDTO = new EventDTO(eventLogic.createEvent(event.toEntity()));

        LOGGER.log(Level.INFO, "Created event from resource: output: {0}", eventDTO);
        return eventDTO;
    }

    /**
     * Looks for all events on application and returns them.
     *
     * @return JSONArray {@link EventDetailDTO} - All the events on application
     * if found. Otherwise, an empty list.
     */
    @GET
    public List<EventDetailDTO> getResidents() {
        LOGGER.info("Looking for all events from resources: input: void");
        List<EventDetailDTO> events = listEntity2DTO(eventLogic.getAllEvents());
        LOGGER.log(Level.INFO, "Ended looking for all events from resources: output: {0}", events);
        return events;
    }

    /**
     * Looks for the event with id received in the URL y returns it.
     *
     * @param eventsId Id from wanted event. Must be a sequence of digits.
     * @return JSON {@link EventDetailDTO} - Wanted event DTO
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Logic error if not found
     */
    @GET
    @Path("{eventsId: \\d+}")
    public EventDetailDTO getResident(@PathParam("eventsId") Long eventsId) {
        LOGGER.log(Level.INFO, "Looking for  event from resource: input: {0}", eventsId);
        EventEntity eventEntity = eventLogic.getEvent(eventsId);
        if (eventEntity == null) {
            throw new WebApplicationException("Resource /events/" + eventsId + " does not exist.", 404);
        }
        EventDetailDTO detailDTO = new EventDetailDTO(eventEntity);
        LOGGER.log(Level.INFO, "Ended looking for event from resource: output: {0}", detailDTO);
        return detailDTO;
    }

    /**
     * Updates event with id from URL with the information contained in request
     * body.
     *
     * @param eventId ID from event to be updated. Must be a sequence of digits.
     * @param event {@link EventDetailDTO} Resident to be updated.
     * @return JSON {@link EventDetailDTO} - Updated event
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Logic error if not found
     */
    @PUT
    @Path("{eventsId: \\d+}")
    public EventDetailDTO updateAuthor(@PathParam("eventsId") Long eventsId, EventDetailDTO event) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Updating event from resource: input: authorsId: {0} , author: {1}", new Object[]{eventsId, event});
        event.setTitle(eventsId+"");
        if (eventLogic.getEvent(eventsId) == null) {
             throw new WebApplicationException("Resource /events/" + eventsId + " does not exist.", 404);
        }
        EventDetailDTO detailDTO = new EventDetailDTO(eventLogic.updateEvent(eventsId,event.toEntity()));
        LOGGER.log(Level.INFO, "Ended updating event from resource: output: {0}", detailDTO);

        return detailDTO;
    }

    /**
     * Deletes the event with the associated id received in URL
     *
     * @param eventsId id from event to be deleted
     * @throws WebApplicationException {@link WebApplicationExceptionMapper}
     * Logic error if not found
     */
    @DELETE
    @Path("{eventsId: \\d+}")
    public void deleteResident(@PathParam("eventsId") Long eventsId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Deleting event from resource: input: {0}", eventsId);
        if (eventLogic.getEvent(eventsId) == null) {
            throw new WebApplicationException("Resource /events/" + eventsId + " does not exist.", 404);
        }
        eventLogic.deleteEvent(eventsId);
        LOGGER.info("Resident deleted from resource: output: void");
    }

    /**
     *
     * Connects /events route with /members route which are dependent of event
     * resource, by redirecting to the service managing the URL segment in
     * charge of the members
     *
     * @param eventId id from event from which the resource is being accessed
     * @return members resource from the specified event
     */

    @Path("{eventsId: \\d+}/members")
    public Class<EventResource> getEventMemberResource(@PathParam("eventsId") Long eventsId) {
        if (eventLogic.getEvent(eventsId) == null) {
            throw new WebApplicationException("Resource /events/" + eventsId + " does not exist.", 404);
        }
        return EventResource.class;
    }

    /**
     * Converts an entity list to a DTO list for events.
     *
     * @param entityList Resident entity list to be converted.
     * @return Resident DTO list
     */
    private List<EventDetailDTO> listEntity2DTO(List<EventEntity> entityList) {
        List<EventDetailDTO> list = new ArrayList<>();
        for (EventEntity entity : entityList) {
            list.add(new EventDetailDTO(entity));
        }
        return list;
    }
}
