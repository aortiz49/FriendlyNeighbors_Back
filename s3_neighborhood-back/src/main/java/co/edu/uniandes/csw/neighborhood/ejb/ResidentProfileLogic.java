/*
MIT License

Copyright (c) 2020 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package co.edu.uniandes.csw.neighborhood.ejb;

//===================================================
// Imports
//===================================================
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

import co.edu.uniandes.csw.neighborhood.persistence.*;
import co.edu.uniandes.csw.neighborhood.entities.ResidentProfileEntity;
import co.edu.uniandes.csw.neighborhood.entities.NeighborhoodEntity;
import co.edu.uniandes.csw.neighborhood.entities.PostEntity;
import co.edu.uniandes.csw.neighborhood.entities.ResidentLoginEntity;
import co.edu.uniandes.csw.neighborhood.exceptions.BusinessLogicException;

/**
 * Class the implements the connection with the businessPersistence for the Business entity.
 *
 * @author albayona
 * @version 2.0 (Added resident login so that it's mandatory to have before createing a resident)
 */
@Stateless
public class ResidentProfileLogic {

    private static final Logger LOGGER = Logger.getLogger(ResidentProfileLogic.class.getName());

    @Inject
    private ResidentProfilePersistence persistence;

    @Inject
    private NeighborhoodPersistence neighborhoodPersistence;

    @Inject
    private ResidentLoginPersistence loginPersistence;

    /**
     * Creates a resident in persistence
     *
     * @param residentEntity Entity representing resident to create
     * @param neighId Parent neighborhood
     * @return Persisted entity representing resident
     * @throws BusinessLogicException If a business rule si not met
     */
    public ResidentProfileEntity createResident(ResidentProfileEntity residentEntity, Long neighId) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Creation process for resident has started");

        verifyBusinessRules(residentEntity);

        NeighborhoodEntity persistedNeighborhood = neighborhoodPersistence.find(neighId);

        if (persistedNeighborhood == null) {
            throw new BusinessLogicException("The neighborhood must exist");
        }

        ResidentLoginEntity persistedLogin = loginPersistence.find(neighId, residentEntity.getLogin().getId());
        
        if (persistedLogin == null) {
            throw new BusinessLogicException("The resident must have a login!");
        }
        
        if(persistedLogin.getResident()!=null){
            throw new BusinessLogicException("The login already has a resident");
        }
        //1b. E-mail cannot be duplicated 
        if (persistence.findByEmail(residentEntity.getEmail())  != null) {
            throw new BusinessLogicException("E-mail provided already in use: " + residentEntity.getEmail() + "\"");
        }
        

        residentEntity.setNeighborhood(persistedNeighborhood);
        persistedLogin.setResident(residentEntity);
        residentEntity.setLogin(persistedLogin);

        persistence.create(residentEntity);

        LOGGER.log(Level.INFO, "Creation process for resident eneded");

        return residentEntity;
    }

    private void verifyBusinessRules(ResidentProfileEntity residentEntity) throws BusinessLogicException {
       
        //1a. E-mail cannot be null 
        if (residentEntity.getEmail() == null) {
            throw new BusinessLogicException("An e-mail has to be specified");
        }

        // 2. Proof of residence must not be null.
        if (residentEntity.getProofOfResidence() == null) {
            throw new BusinessLogicException("A proof of residence has to be specified");
        }

        //3. The user must indicate his name.
        if (residentEntity.getName() == null) {
            throw new BusinessLogicException("A name has to be specified");
        }

        //4. The user must provide a mobile number
        if (residentEntity.getPhoneNumber() == null) {
            throw new BusinessLogicException("A phone has to be specified");
        }

    }

    /**
     * Deletes a resident from neighborhood
     *
     * @param neighID parent neighborhood
     * @param id : id from resident to delete
     */
    public void deleteResident(Long id, Long neighID) {

        LOGGER.log(Level.INFO, "Starting deleting process for resident with id = {0}", id);
        persistence.delete(id, neighID);
        LOGGER.log(Level.INFO, "Ended deleting process for resident with id = {0}", id);
    }

    /**
     * Gets all residents from neighborhood
     *
     * @param neighID parent neighborhood
     * @return A list with ll residents from neighborhood
     */
    public List<ResidentProfileEntity> getResidents(Long neighID) {

        LOGGER.log(Level.INFO, "Starting querying process for all residents");
        List<ResidentProfileEntity> residents = persistence.findAll(neighID);
        LOGGER.log(Level.INFO, "Ended querying process for all residents");
        return residents;
    }

    /**
     * Returns a resident from neighborhood
     *
     * @param neighID parent neighborhood
     * @param id: id from resident to find from neighborhood
     * @return the entity of the wanted resident
     */
    public ResidentProfileEntity getResident(Long id, Long neighID) {
        LOGGER.log(Level.INFO, "Starting querying process for resident with id {0}", id);
        ResidentProfileEntity resident = persistence.find(id, neighID);
        LOGGER.log(Level.INFO, "Ended querying process for  resident with id {0}", id);
        return resident;
    }

    /**
     * Returns a resident by email
     *
     * @param email : email from wanted resident
     * @return the entity of the wanted resident
     */
    public ResidentProfileEntity getResidentByEmail(String email) {
        LOGGER.log(Level.INFO, "Starting querying process for resident with email ", email);
        ResidentProfileEntity resident = persistence.findByEmail(email);
        LOGGER.log(Level.INFO, "Ended querying process for  resident with id {0}", email);
        return resident;
    }

    /**
     * Updates a resident from neighborhood
     *
     * @param neighID parent neighborhood
     * @param residentEntity to be updated
     * @return the entity with the updated resident
     * @throws BusinessLogicException exception
     */
    public ResidentProfileEntity updateResident(ResidentProfileEntity residentEntity, Long neighID) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Starting update process for resident with id {0}", residentEntity.getId());

        ResidentProfileEntity original = persistence.find(residentEntity.getId(), neighID);

        //1a. E-mail cannot be null 
        if (residentEntity.getEmail() == null) {
            throw new BusinessLogicException("An e-mail has to be specified");
        }

        if (!original.getEmail().equals(residentEntity.getEmail())) {

            //1b. E-mail cannot be duplicated 
            if (persistence.findByEmail(residentEntity.getEmail()) != null) {
                throw new BusinessLogicException("E-mail provided already in use: " + residentEntity.getEmail() + "\"");
            }
        }

        verifyBusinessRules(residentEntity);

        NeighborhoodEntity neighborhood = neighborhoodPersistence.find(neighID);

        residentEntity.setNeighborhood(neighborhood);

        ResidentProfileEntity modified = persistence.update(residentEntity, neighID);
        LOGGER.log(Level.INFO, "Ended update process for resident with id {0}", residentEntity.getId());
        return modified;
    }

    
    
    public ResidentProfileEntity associatePictureToResident(Long residentId, Long neighId, String pic) throws BusinessLogicException {
        ResidentProfileEntity entity = persistence.find(residentId, neighId);

        entity.getAlbum().add(pic);
        return persistence.find(residentId, neighId);
    }
    
}
