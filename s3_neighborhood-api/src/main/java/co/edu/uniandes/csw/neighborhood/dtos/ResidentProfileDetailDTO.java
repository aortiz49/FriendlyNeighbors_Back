/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.neighborhood.dtos;

import co.edu.uniandes.csw.neighborhood.entities.*;
import co.edu.uniandes.csw.neighborhood.entities.ResidentProfileEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * This class represent a resident in a neighborhood
 *
 * @author albayona
 */
public class ResidentProfileDetailDTO extends ResidentProfileDTO implements Serializable {

//===================================================
// Attributes
//===================================================  
    /**
     * Events a resident is signed up for.
     */
    private List<EventDTO> attendedEvents;

    /**
     * Favors a resident is signed up to complete.
     */
    private List<FavorDTO> favorsToHelp;

    /**
     * Posts shared with this resident.
     */
    private List<PostDTO> postsToView;

    /**
     * Represents favors requested by this resident
     */
    private List<FavorDTO> favorsRequested;

    /**
     * Represents services offered by this resident
     */
    private List<ServiceDTO> services;

    /**
     * Represents notifications posted by this resident
     */
    private List<NotificationDTO> notifications;

    /**
     * Represents posts made by this resident
     */
    private List<PostDTO> posts;

    /**
     * Represents events posted by this resident
     */
    private List<EventDTO> events;

    /**
     * Represents groups this resident is part of
     */
    private List<GroupDTO> groups;

    /**
     * Represents businesses owned by this resident
     */
    private List<BusinessDTO> businesses;
    /**
     * Represents comments posted by this resident
     */
    private List<CommentDTO> comments;

    private List<String> album;

    public ResidentProfileDetailDTO() {
        super();
    }

    /**
     * Creates a detailed resident DTO from resident entity, including its
     * relations.
     *
     * @param residentEntity from which new group DTO will be created
     *
     */
    public ResidentProfileDetailDTO(ResidentProfileEntity residentEntity) {
        super(residentEntity);
        if (residentEntity != null) {

            attendedEvents = new ArrayList<>();
            favorsToHelp = new ArrayList<>();
            postsToView = new ArrayList<>();
            favorsRequested = new ArrayList<>();
            services = new ArrayList<>();
            notifications = new ArrayList<>();
            posts = new ArrayList<>();
            events = new ArrayList<>();
            groups = new ArrayList<>();
            comments = new ArrayList<>();
            album = new ArrayList<>();

            for (EventEntity entityEvent : residentEntity.getAttendedEvents()) {
                attendedEvents.add(new EventDTO(entityEvent));
            }

            for (FavorEntity entityFavor : residentEntity.getFavorsToHelp()) {
                favorsToHelp.add(new FavorDTO(entityFavor));
            }

            for (PostEntity entityPost : residentEntity.getPostsToView()) {
                postsToView.add(new PostDTO(entityPost));
            }

            for (FavorEntity entityFavor : residentEntity.getFavorsRequested()) {
                favorsRequested.add(new FavorDTO(entityFavor));
            }

            for (ServiceEntity entityService : residentEntity.getServices()) {
                services.add(new ServiceDTO(entityService));
            }

            for (NotificationEntity entityNotification : residentEntity.getNotifications()) {
                notifications.add(new NotificationDTO(entityNotification));
            }

            for (PostEntity entityPost : residentEntity.getPosts()) {
                posts.add(new PostDTO(entityPost));
            }
            for (EventEntity entityEvent : residentEntity.getEvents()) {
                events.add(new EventDTO(entityEvent));
            }

            for (GroupEntity entityGroup : residentEntity.getGroups()) {
                groups.add(new GroupDTO(entityGroup));
            }

            for (BusinessEntity businessEntity : residentEntity.getBusinesses()) {
                businesses.add(new BusinessDTO(businessEntity));
            }

            album = residentEntity.getAlbum();

        }
    }

    /**
     * Converts a detailed resident DTO to a resident entity.
     *
     * @return new resident entity
     *
     */
    public ResidentProfileEntity toEntity() {
        ResidentProfileEntity residentEntity = super.toEntity();

        if (getAttendedEvents() != null) {
            List<EventEntity> events = new ArrayList<>();
            for (EventDTO dtoEvent : getAttendedEvents()) {
                events.add(dtoEvent.toEntity());
            }
            residentEntity.setAttendedEvents(events);
        }

        if (getFavorsToHelp() != null) {
            List<FavorEntity> favors = new ArrayList<>();
            for (FavorDTO dtoFavor : getFavorsToHelp()) {
                favors.add(dtoFavor.toEntity());
            }
            residentEntity.setFavorsToHelp(favors);
        }

        if (getPostsToView() != null) {
            List<PostEntity> posts = new ArrayList<>();
            for (PostDTO dtoPost : getPostsToView()) {
                posts.add(dtoPost.toEntity());
            }
            residentEntity.setPostsToView(posts);
        }

        if (getFavorsRequested() != null) {
            List<FavorEntity> favors = new ArrayList<>();
            for (FavorDTO dtoFavor : getFavorsRequested()) {
                favors.add(dtoFavor.toEntity());
            }
            residentEntity.setFavorsRequested(favors);
        }

        if (getServices() != null) {
            List<ServiceEntity> servicesE = new ArrayList<>();
            for (ServiceDTO dtoService : getServices()) {
                servicesE.add(dtoService.toEntity());
            }
            residentEntity.setServices(servicesE);
        }

        if (getNotifications() != null) {
            List<NotificationEntity> notificationsE = new ArrayList<>();
            for (NotificationDTO dtoNotification : getNotifications()) {
                notificationsE.add(dtoNotification.toEntity());
            }
            residentEntity.setNotifications(notificationsE);
        }

        if (getPosts() != null) {
            List<PostEntity> postsE = new ArrayList<>();
            for (PostDTO dtoPost : getPosts()) {
                postsE.add(dtoPost.toEntity());
            }
            residentEntity.setPostsToView(postsE);
        }

        if (getGroups() != null) {
            List<GroupEntity> groupsE = new ArrayList<>();
            for (GroupDTO dtoGroup : getGroups()) {
                groupsE.add(dtoGroup.toEntity());
            }
            residentEntity.setGroups(groupsE);
        }

        if (getBusinesses() != null) {
            List<BusinessEntity> businessEntities = new ArrayList<>();
            for (BusinessDTO businessDTO : getBusinesses()) {
                businessEntities.add(businessDTO.toEntity());
            }
            residentEntity.setBusinesses(businessEntities);
        }

        residentEntity.setAlbum(getAlbum());

        return residentEntity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * @return the attendedEvents
     */
    public List<EventDTO> getAttendedEvents() {
        return attendedEvents;
    }

    /**
     * @param attendedEvents the attendedEvents to set
     */
    public void setAttendedEvents(List<EventDTO> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    /**
     * @return the favorsToHelp
     */
    public List<FavorDTO> getFavorsToHelp() {
        return favorsToHelp;
    }

    /**
     * @param favorsToHelp the favorsToHelp to set
     */
    public void setFavorsToHelp(List<FavorDTO> favorsToHelp) {
        this.favorsToHelp = favorsToHelp;
    }

    /**
     * @return the postsToView
     */
    public List<PostDTO> getPostsToView() {
        return postsToView;
    }

    /**
     * @param postsToView the postsToView to set
     */
    public void setPostsToView(List<PostDTO> postsToView) {
        this.postsToView = postsToView;
    }

    /**
     * @return the favorsRequested
     */
    public List<FavorDTO> getFavorsRequested() {
        return favorsRequested;
    }

    /**
     * @param favorsRequested the favorsRequested to set
     */
    public void setFavorsRequested(List<FavorDTO> favorsRequested) {
        this.favorsRequested = favorsRequested;
    }

    /**
     * @return the services
     */
    public List<ServiceDTO> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<ServiceDTO> services) {
        this.services = services;
    }

    /**
     * @return the notifications
     */
    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications the notifications to set
     */
    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the posts
     */
    public List<PostDTO> getPosts() {
        return posts;
    }

    /**
     * @param posts the posts to set
     */
    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }

    /**
     * @return the events
     */
    public List<EventDTO> getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */
    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    /**
     * @return the groups
     */
    public List<GroupDTO> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }

    /**
     * @return the businesses
     */
    public List<BusinessDTO> getBusinesses() {
        return businesses;
    }

    /**
     * @param businesses the businesses to set
     */
    public void setBusinesses(List<BusinessDTO> businesses) {
        this.businesses = businesses;
    }

    /**
     * @return the comments
     */
    public List<CommentDTO> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<String> getAlbum() {
        return album;
    }

    public void setAlbum(List<String> album) {
        this.album = album;
    }

}
