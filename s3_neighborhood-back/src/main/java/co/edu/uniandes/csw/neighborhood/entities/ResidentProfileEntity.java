/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.neighborhood.entities;

import co.edu.uniandes.csw.neighborhood.podam.DateStrategy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStrategyValue;

/**
 * This class represents a resident in a neighborhood
 *
 * @author albayona
 */
@Entity
public class ResidentProfileEntity extends BaseEntity implements Serializable {

//===================================================
// Attributes
//===================================================  
    /**
     * Represents phone number of this resident
     */
    private String phoneNumber;

    /**
     * Represents email of this resident
     */
    private String email;

    /**
     * Represents the name of this resident
     */
    private String name;

    /**
     * Represents nickname of this resident
     */
    private String nickname;

    /**
     * The resident's address.
     */
    private String address;

    /**
     * Represents preferences of this resident
     */
    private String preferences;

    /**
     * Represents a link to a proof of residence file of this resident
     */
    private String proofOfResidence;

    private String profilePicture;

    private String muralPicture;

    private String livingSince;

    @Temporal(TemporalType.DATE)
    @PodamStrategyValue(DateStrategy.class)
    private Date birthDate;

    @Temporal(TemporalType.DATE)
    @PodamStrategyValue(DateStrategy.class)
    private Date joinDate;

//===================================================
// Relations
//===================================================
    @ElementCollection
    @CollectionTable(name = "residentAlbum")
    private List<String> album;

    /**
     * Events a resident is signed up for.
     */
    @PodamExclude
    @ManyToMany(mappedBy = "attendees")
    private List<EventEntity> attendedEvents = new ArrayList<>();

    /**
     * Favors a resident is signed up to complete.
     */
    @PodamExclude
    @ManyToMany(mappedBy = "candidates", fetch = FetchType.LAZY)
    private List<FavorEntity> favorsToHelp = new ArrayList();

    /**
     * Posts shared with this resident.
     */
    @PodamExclude
    @ManyToMany(mappedBy = "viewers", fetch = FetchType.LAZY)
    private List<PostEntity> postsToView = new ArrayList();

    /**
     * Represents favors requested by this resident
     */
    @PodamExclude
    @OneToMany(
            mappedBy = "author",
            fetch = javax.persistence.FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )

    private List<FavorEntity> favorsRequested = new ArrayList<>();

    /**
     * Represents services offered by this resident
     */
    @PodamExclude
    @OneToMany(
            mappedBy = "author",
            fetch = javax.persistence.FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<ServiceEntity> services = new ArrayList<>();

    /**
     * Represents notifications posted by this resident
     */
    @PodamExclude
    @OneToMany(
            mappedBy = "author",
            fetch = javax.persistence.FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<NotificationEntity> notifications = new ArrayList<>();

    /**
     * Represents posts made by this resident
     */
    @PodamExclude
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostEntity> posts = new ArrayList<>();

    /**
     * Represents events posted by this resident
     */
    @PodamExclude
    @OneToMany(
            mappedBy = "host",
            fetch = javax.persistence.FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<EventEntity> events = new ArrayList<>();

    /**
     * Represents events posted by this resident
     */
    @PodamExclude
    @OneToOne
    private ResidentLoginEntity login;

    /**
     * Represents groups this resident is part of
     */
    @PodamExclude
    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private List<GroupEntity> groups = new ArrayList<>();

    /**
     * Represents comments posted by this resident
     */
    @PodamExclude
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();

    /**
     * Represents businesses owned by this resident
     */
    @PodamExclude
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<BusinessEntity> businesses = new ArrayList<>();

    /**
     * Represents the neighborhood of this resident
     */
    @PodamExclude
    @ManyToOne
    private NeighborhoodEntity neighborhood;

    public String getPhoneNumber() {
        return phoneNumber;
    }

//===================================================
// Getters & Setters
//=================================================== 
    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the preferences
     */
    public String getPreferences() {
        return preferences;
    }

    /**
     * @param preferences the preferences to set
     */
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    /**
     * @return the proofOfResidence
     */
    public String getProofOfResidence() {
        return proofOfResidence;
    }

    /**
     * @param proofOfResidence the proofOfResidence to set
     */
    public void setProofOfResidence(String proofOfResidence) {
        this.proofOfResidence = proofOfResidence;
    }

    /**
     * @return the profilePicture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * @param profilePicture the profilePicture to set
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * @return the muralPicture
     */
    public String getMuralPicture() {
        return muralPicture;
    }

    /**
     * @param muralPicture the muralPicture to set
     */
    public void setMuralPicture(String muralPicture) {
        this.muralPicture = muralPicture;
    }

    /**
     * @return the livingSince
     */
    public String getLivingSince() {
        return livingSince;
    }

    /**
     * @param livingSince the livingSince to set
     */
    public void setLivingSince(String livingSince) {
        this.livingSince = livingSince;
    }

    /**
     * @return the birthDate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @return the joinDate
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * @param joinDate the joinDate to set
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * @return the attendedEvents
     */
    public List<EventEntity> getAttendedEvents() {
        return attendedEvents;
    }

    /**
     * @param attendedEvents the attendedEvents to set
     */
    public void setAttendedEvents(List<EventEntity> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    /**
     * @return the favorsToHelp
     */
    public List<FavorEntity> getFavorsToHelp() {
        return favorsToHelp;
    }

    /**
     * @param favorsToHelp the favorsToHelp to set
     */
    public void setFavorsToHelp(List<FavorEntity> favorsToHelp) {
        this.favorsToHelp = favorsToHelp;
    }

    /**
     * @return the postsToView
     */
    public List<PostEntity> getPostsToView() {
        return postsToView;
    }

    /**
     * @param postsToView the postsToView to set
     */
    public void setPostsToView(List<PostEntity> postsToView) {
        this.postsToView = postsToView;
    }

    /**
     * @return the favorsRequested
     */
    public List<FavorEntity> getFavorsRequested() {
        return favorsRequested;
    }

    /**
     * @param favorsRequested the favorsRequested to set
     */
    public void setFavorsRequested(List<FavorEntity> favorsRequested) {
        this.favorsRequested = favorsRequested;
    }

    /**
     * @return the services
     */
    public List<ServiceEntity> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<ServiceEntity> services) {
        this.services = services;
    }

    /**
     * @return the notifications
     */
    public List<NotificationEntity> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications the notifications to set
     */
    public void setNotifications(List<NotificationEntity> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the posts
     */
    public List<PostEntity> getPosts() {
        return posts;
    }

    /**
     * @param posts the posts to set
     */
    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
    }

    /**
     * @return the events
     */
    public List<EventEntity> getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */
    public void setEvents(List<EventEntity> events) {
        this.events = events;
    }

    /**
     * @return the login
     */
    public ResidentLoginEntity getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(ResidentLoginEntity login) {
        this.login = login;
    }

    /**
     * @return the groups
     */
    public List<GroupEntity> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<GroupEntity> groups) {
        this.groups = groups;
    }

    /**
     * @return the comments
     */
    public List<CommentEntity> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    /**
     * @return the businesses
     */
    public List<BusinessEntity> getBusinesses() {
        return businesses;
    }

    /**
     * @param businesses the businesses to set
     */
    public void setBusinesses(List<BusinessEntity> businesses) {
        this.businesses = businesses;
    }

    /**
     * @return the neighborhood
     */
    public NeighborhoodEntity getNeighborhood() {
        return neighborhood;
    }

    /**
     * @param neighborhood the neighborhood to set
     */
    public void setNeighborhood(NeighborhoodEntity neighborhood) {
        this.neighborhood = neighborhood;
    }

    public List<String> getAlbum() {
        return album;
    }

    public void setAlbum(List<String> album) {
        this.album = album;
    }

    
}
