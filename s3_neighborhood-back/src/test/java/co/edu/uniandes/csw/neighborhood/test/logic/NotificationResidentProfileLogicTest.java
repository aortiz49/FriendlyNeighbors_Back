/*
MIT License

Copyright (c) 2017 Universidad de los Andes - ISIS2603

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
package co.edu.uniandes.csw.neighborhood.test.logic;

import co.edu.uniandes.csw.neighborhood.ejb.NotificationLogic;
import co.edu.uniandes.csw.neighborhood.ejb.ResidentProfileLogic;
import co.edu.uniandes.csw.neighborhood.ejb.NotificationResidentProfileLogic;
import co.edu.uniandes.csw.neighborhood.entities.NotificationEntity;
import co.edu.uniandes.csw.neighborhood.entities.NeighborhoodEntity;
import co.edu.uniandes.csw.neighborhood.entities.ResidentProfileEntity;
import co.edu.uniandes.csw.neighborhood.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.neighborhood.persistence.ResidentProfilePersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.junit.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * @author albayona
 */
@RunWith(Arquillian.class)
public class NotificationResidentProfileLogicTest {

    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private NotificationResidentProfileLogic residentNotificationLogic;

    @Inject
    private NotificationLogic postLogic;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private NeighborhoodEntity neighborhood;

    private List<ResidentProfileEntity> residentData = new ArrayList<ResidentProfileEntity>();

    private List<NotificationEntity> postsData = new ArrayList();

    /**
     * @return Returns jar which Arquillian will deploy embedded in Payara. jar
     * contains classes, DB descriptor and beans.xml file for dependencies
     * injector resolution.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(ResidentProfileEntity.class.getPackage())
                .addPackage(ResidentProfileLogic.class.getPackage())
                .addPackage(ResidentProfilePersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    /**
     * Initial test configuration.
     */
    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Clears tables involved in tests
     */
    private void clearData() {
        em.createQuery("delete from NotificationEntity").executeUpdate();
        em.createQuery("delete from ResidentProfileEntity").executeUpdate();
    }

    /**
     * Inserts initial data for correct test operation
     */
    private void insertData() throws BusinessLogicException {
        neighborhood = factory.manufacturePojo(NeighborhoodEntity.class);
        em.persist(neighborhood);

        for (int i = 0; i < 3; i++) {
            ResidentProfileEntity entity = factory.manufacturePojo(ResidentProfileEntity.class);

            entity.setNeighborhood(neighborhood);
            em.persist(entity);
            residentData.add(entity);
        }

        for (int i = 0; i < 3; i++) {
            NotificationEntity post = factory.manufacturePojo(NotificationEntity.class);

            if (i == 0) {
                postLogic.createNotification(post, residentData.get(0).getId(), neighborhood.getId());
            } else {
                postLogic.createNotification(post, residentData.get(i).getId(), neighborhood.getId());
            }

            postsData.add(post);
        }

    }

    /**
     * Test for getting  collection of post entities associated with 
     * resident
     */
    @Test
    public void getNotificationsTest() {
        List<NotificationEntity> list = residentNotificationLogic.getNotifications(residentData.get(0).getId(), neighborhood.getId());

        Assert.assertEquals(1, list.size());
    }

    /**
     * Test for getting  post entity associated with  resident
     *
     * @throws BusinessLogicException
     */
    @Test
    public void getNotificationTest() throws BusinessLogicException {
        ResidentProfileEntity entity = residentData.get(0);
        NotificationEntity postEntity = postsData.get(0);
        NotificationEntity response = residentNotificationLogic.getNotification(entity.getId(), postEntity.getId(), neighborhood.getId());

        Assert.assertEquals(postEntity.getId(), response.getId());
        Assert.assertEquals(postEntity.getDescription(), response.getDescription());

    }

    /**
     * Test for getting  post from non-author user
     *
     * @throws BusinessLogicException
     */
    @Test(expected = BusinessLogicException.class)
    public void getNonRealatedNotificationTest() throws BusinessLogicException {
        ResidentProfileEntity entity = residentData.get(0);
        NotificationEntity postEntity = postsData.get(1);
        residentNotificationLogic.getNotification(entity.getId(), postEntity.getId(), neighborhood.getId());
    }

    /**
     * Test for replacing posts associated with  resident
     *
     * @throws BusinessLogicException
     */
    @Test

    public void replaceNotificationsTest() throws BusinessLogicException {
        List<NotificationEntity> newCollection = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NotificationEntity entity = factory.manufacturePojo(NotificationEntity.class);

            postLogic.createNotification(entity, residentData.get(0).getId(), neighborhood.getId());

            newCollection.add(entity);
        }
        residentNotificationLogic.replaceNotifications(residentData.get(0).getId(), newCollection, neighborhood.getId());
        List<NotificationEntity> posts = residentNotificationLogic.getNotifications(residentData.get(0).getId(), neighborhood.getId());
        for (NotificationEntity newE : newCollection) {
            Assert.assertTrue(posts.contains(newE));
        }
    }

    /**
     * Test for removing  post from resident
     *
     */
    @Test
    public void removeNotificationTest() throws BusinessLogicException {

        residentNotificationLogic.removeNotification(residentData.get(0).getId(), postsData.get(0).getId(), neighborhood.getId());

        Assert.assertTrue(residentNotificationLogic.getNotifications(residentData.get(0).getId(), neighborhood.getId()).isEmpty());
    }

}
