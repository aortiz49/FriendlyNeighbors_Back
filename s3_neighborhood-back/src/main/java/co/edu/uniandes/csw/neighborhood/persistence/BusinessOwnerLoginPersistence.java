/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.neighborhood.persistence;

import co.edu.uniandes.csw.neighborhood.entities.BusinessOwnerLoginEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author aortiz49
 */
@Stateless
public class BusinessOwnerLoginPersistence {
    
    private static final Logger LOGGER = Logger.getLogger(BusinessOwnerLoginPersistence.class.getName());

    @PersistenceContext(unitName = "neighborhoodPU" )
    protected EntityManager em;
    
     /**
     * Creates a login within DB
     *
     * @param le login object to be created in DB
     * @return returns the created entity with an id given by DB.
     */
    public BusinessOwnerLoginEntity create(BusinessOwnerLoginEntity le){
        
        LOGGER.log(Level.INFO, "Creating a new Login");

        em.persist(le);
        LOGGER.log(Level.INFO, "Login created");
        return le;
    }
    
    /**
     * Returns all logins from DB.
     *
     * @return a list with all logins found in DB.
     */
    public List<BusinessOwnerLoginEntity> findAll() {
        LOGGER.log(Level.INFO, "Querying for all logins");
        
        TypedQuery query = em.createQuery("select u from BusinessOwnerLoginEntity u", BusinessOwnerLoginEntity.class);
       
        return query.getResultList();
    }
    /**
     * Looks for a login with the id given by argument
     *
     * @param loginId: id from login to be found.
     * @return a login.
     */
     public BusinessOwnerLoginEntity find(Long loginId) {
        LOGGER.log(Level.INFO, "Querying for login with id={0}", loginId);
       
        
        return em.find(BusinessOwnerLoginEntity.class, loginId);
    }
     
    /**
     * Updates a login with the modified login given by argument.
     *
     * @param le: the modified login. Por
     * @return the updated login
     */
    public BusinessOwnerLoginEntity update(BusinessOwnerLoginEntity le) {
        LOGGER.log(Level.INFO, "Updating login with id={0}", le.getId());
        return em.merge(le);
    }
    
    /**
     * Deletes from DB a login with the id given by argument
     *
     * @param loginId: id from login to be deleted.
     */
    public void delete(Long loginId) {

        LOGGER.log(Level.INFO, "Deleting login with id={0}", loginId);
        BusinessOwnerLoginEntity loginEntity = em.find(BusinessOwnerLoginEntity.class, loginId);
        em.remove(loginEntity);
    }
}
