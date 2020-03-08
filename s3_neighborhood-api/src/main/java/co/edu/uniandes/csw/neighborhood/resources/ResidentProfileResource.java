/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.neighborhood.resources;

import co.edu.uniandes.csw.neighborhood.dtos.ResidentLoginDetailDTO;
import co.edu.uniandes.csw.neighborhood.dtos.ResidentProfileDTO;
import co.edu.uniandes.csw.neighborhood.dtos.ResidentProfileDetailDTO;
import co.edu.uniandes.csw.neighborhood.ejb.ResidentProfileLogic;
import co.edu.uniandes.csw.neighborhood.entities.ResidentProfileEntity;
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
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

/**
 * Class implementing resource "residents".
 *
 * @author albayons
 * @version 1.0
 */
@Path("residents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ResidentProfileResource {

    private static final Logger LOGGER = Logger.getLogger(ResidentProfileResource.class.getName());

    @Inject
    private ResidentProfileLogic residentLogic;

    /**
     * Creates a new resident with the information received in request, then an
     * identical object is returned with an id generated by DB
     *
     * @param resident {@link ResidentProfileDTO} - Resident to be persisted
     * @return JSON {@link ResidentProfileDTO} - Auto-generated resident to be
     * persisted
     */
    @POST
    public ResidentProfileDetailDTO createResident(ResidentProfileDetailDTO resident) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Creating resident from resource: input: {0}", resident);

        ResidentProfileDetailDTO residentDTO = new ResidentProfileDetailDTO(residentLogic.createResident(resident.toEntity()));
        
        

        LOGGER.log(Level.INFO, "Created resident from resource: output: {0}", residentDTO);
        return residentDTO;
    }

    /**
     * Looks for all residents on application and returns them.
     *
     * @return JSONArray {@link ResidentProfileDetailDTO} - All the residents on
     * application if found. Otherwise, an empty list.
     */
    @GET
    public List<ResidentProfileDetailDTO> getResidents() {
        LOGGER.info("Looking for all residents from resources: input: void");
        List<ResidentProfileDetailDTO> residents = listEntity2DTO(residentLogic.getResidents());
        LOGGER.log(Level.INFO, "Ended looking for all residents from resources: output: {0}", residents);
        return residents;
    }

    /**
     * Looks for the resident with id received in the URL y returns it.
     *
     * @param residentsId Id from wanted resident. Must be a sequence of digits.
     * @return JSON {@link ResidentProfileDetailDTO} - Wanted resident DTO
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Logic error if not found
     */
    @GET
    @Path("{residentsId: \\d+}")
    public ResidentProfileDetailDTO getResident(@PathParam("residentsId") Long residentsId) {
        LOGGER.log(Level.INFO, "Looking for  resident from resource: input: {0}", residentsId);
        ResidentProfileEntity residentEntity = residentLogic.getResident(residentsId);
        if (residentEntity == null) {
            throw new WebApplicationException("Resource /residents/" + residentsId + " does not exist.", 404);
        }
        ResidentProfileDetailDTO detailDTO = new ResidentProfileDetailDTO(residentEntity);
        LOGGER.log(Level.INFO, "Ended looking for resident from resource: output: {0}", detailDTO);
        return detailDTO;
    }

    /**
     * Updates resident with id from URL with the information contained in
     * request body.
     *
     * @param residentId ID from resident to be updated. Must be a sequence of
     * digits.
     * @param resident {@link ResidentProfileDetailDTO} Resident to be updated.
     * @return JSON {@link ResidentProfileDetailDTO} - Updated resident
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Logic error if not found
     */
    @PUT
    @Path("{residentsId: \\d+}")
    public ResidentProfileDetailDTO updateAuthor(@PathParam("residentsId") Long residentsId, ResidentProfileDetailDTO resident) throws BusinessLogicException {
        LOGGER.log(Level.INFO, "Updating resident from resource: input: authorsId: {0} , author: {1}", new Object[]{residentsId, resident});
        resident.setId(residentsId);
        if (residentLogic.getResident(residentsId) == null) {
                        throw new WebApplicationException("Resource /residents/" + residentsId + " does not exist.", 404);
        }
        ResidentProfileDetailDTO detailDTO = new ResidentProfileDetailDTO(residentLogic.updateResident(resident.toEntity()));
        LOGGER.log(Level.INFO, "Ended updating resident from resource: output: {0}", detailDTO);

        return detailDTO;
    }

    /**
     * Deletes the resident with the associated id received in URL
     *
     * @param residentsId id from resident to be deleted
     * @throws WebApplicationException {@link WebApplicationExceptionMapper}
     * Logic error if not found
     */
    @DELETE
    @Path("{residentsId: \\d+}")
    public void deleteResident(@PathParam("residentsId") Long residentsId) {
        LOGGER.log(Level.INFO, "Deleting resident from resource: input: {0}", residentsId);
        if (residentLogic.getResident(residentsId) == null) {
            throw new WebApplicationException("Resource /residents/" + residentsId + " does not exist.", 404);
        }
        residentLogic.deleteResident(residentsId);
        LOGGER.info("Resident deleted from resource: output: void");
    }

    /**
     * Converts an entity list to a DTO list for residents.
     *
     * @param entityList Resident entity list to be converted.
     * @return Resident DTO list
     */
    private List<ResidentProfileDetailDTO> listEntity2DTO(List<ResidentProfileEntity> entityList) {
        List<ResidentProfileDetailDTO> list = new ArrayList<>();
        for (ResidentProfileEntity entity : entityList) {
            list.add(new ResidentProfileDetailDTO(entity));
        }
        return list;
    }

    /**
     *
     * Connects /residents route with /membershipGroups route which are
     * dependent of resident resource, by redirecting to the service managing
     * the URL segment in charge of the members
     *
     * @param residentsId id from resident from which the resource is being
     * accessed
     * @return groups resource from the specified resident
     */
    @Path("{membersId: \\d+}/membershipGroups")
    public Class<MemberGroupResource> getMemberGroupResource(@PathParam("membersId") Long residentsId) {
        if (residentLogic.getResident(residentsId) == null) {
            throw new WebApplicationException("Resource /residents/" + residentsId + " does not exist.", 404);
        }
        return MemberGroupResource.class;
    }
    
        /**
     *
     * Connects /residents route with /favorsToHelp route which are
     * dependent of resident resource, by redirecting to the service managing
     * the URL segment in charge of the members
     *
     * @param residentsId id from resident from which the resource is being
     * accessed
     * @return groups resource from the specified resident
     */
    @Path("{helpersId: \\d+}/favorsToHelp")
    public Class<HelperFavorResource> getHelperFavorResource(@PathParam("helpersId") Long residentsId) {
        if (residentLogic.getResident(residentsId) == null) {
            throw new WebApplicationException("Resource /residents/" + residentsId + " does not exist.", 404);
        }
        return HelperFavorResource.class;
    }

    /**
     *
     * Connects /residents route with /favors route which are dependent of
     * resident resource, by redirecting to the service managing the URL segment
     * in charge of the members
     *
     * @param residentsId id from resident from which the resource is being
     * accessed
     * @return groups resource from the specified resident
     */
    @Path("{residentsId: \\d+}/favors")
    public Class<FavorResidentProfileResource> getFavorResidentProfileResource(@PathParam("residentsId") Long residentsId) {
        if (residentLogic.getResident(residentsId) == null) {
            throw new WebApplicationException("Resource /residents/" + residentsId + " does not exist.", 404);
        }
        return FavorResidentProfileResource.class;
    }

}
