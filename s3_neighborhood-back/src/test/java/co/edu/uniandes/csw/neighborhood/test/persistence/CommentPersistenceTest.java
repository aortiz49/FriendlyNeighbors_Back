/*
MIT License

Copyright (c) 2019 Universidad de los Andes - ISIS2603

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
package co.edu.uniandes.csw.neighborhood.test.persistence;

import co.edu.uniandes.csw.neighborhood.entities.NeighborhoodEntity;
import co.edu.uniandes.csw.neighborhood.entities.CommentEntity;
import co.edu.uniandes.csw.neighborhood.entities.ResidentProfileEntity;
import co.edu.uniandes.csw.neighborhood.persistence.CommentPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Persistence test for Comment
 *
 * @author aortiz49
 */
@RunWith(Arquillian.class)
public class CommentPersistenceTest {

    @Inject
    private CommentPersistence commentPersistence;

    @PersistenceContext
    private EntityManager em;

    @Inject
    UserTransaction utx;

    NeighborhoodEntity neighborhood;

    ResidentProfileEntity resident;

    private List<CommentEntity> data = new ArrayList<>();

    /**
     * @return Returns jar which Arquillian will deploy embedded in Payara. jar
     * contains classes, DB descriptor and beans.xml file for dependencies
     * injector resolution.
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(CommentEntity.class.getPackage())
                .addPackage(CommentPersistence.class.getPackage())
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
            em.joinTransaction();
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
        em.createQuery("delete from CommentEntity").executeUpdate();
    }

    /**
     * Inserts initial data for correct test operation
     */
    private void insertData() {

        PodamFactory factory = new PodamFactoryImpl();

        neighborhood = factory.manufacturePojo(NeighborhoodEntity.class);
        em.persist(neighborhood);

        resident = factory.manufacturePojo(ResidentProfileEntity.class);
        
        resident.setNeighborhood(neighborhood);
        em.persist(resident);
        

        for (int i = 0; i < 3; i++) {
            CommentEntity entity = factory.manufacturePojo(CommentEntity.class);
            entity.setAuthor(resident);

            em.persist(entity);
            data.add(entity);
        }
    }

    /**
     * Test for creating comment.
     */
    @Test
    public void createCommentTest() {
        PodamFactory factory = new PodamFactoryImpl();
        CommentEntity newEntity = factory.manufacturePojo(CommentEntity.class);
        CommentEntity result = commentPersistence.create(newEntity);

        Assert.assertNotNull(result);

        CommentEntity entity = em.find(CommentEntity.class, result.getId());

        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getDate(), newEntity.getDate());
        Assert.assertEquals(entity.getText(), newEntity.getText());

    }

    /**
     * Test for retrieving all residents from DB.
     */
    @Test
    public void findAllTest() {

        List<CommentEntity> list = commentPersistence.findAll(neighborhood.getId());
        Assert.assertEquals(data.size(), list.size());
        for (CommentEntity ent : list) {
            boolean found = false;
            for (CommentEntity entity : data) {
                if (ent.getId().equals(entity.getId())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    /**
     * Test for a query about comment.
     */
    @Test
    public void getResidentTest() {
        CommentEntity entity = data.get(0);
        CommentEntity newEntity = commentPersistence.find(entity.getId(), neighborhood.getId());
        Assert.assertNotNull(newEntity);
        Assert.assertEquals(entity.getDate(), newEntity.getDate());
        Assert.assertEquals(entity.getText(), newEntity.getText());
    }

    /**
     * Test for a query about a comment not belonging to a neighborhood.
     */
    @Test(expected = RuntimeException.class)
    public void getResidentTestNotBelonging() {
        CommentEntity entity = data.get(0);
        commentPersistence.find(entity.getId(), new Long(10000));
    }

    /**
     * Test for updating comment.
     */
    @Test
    public void updateResidentTest() {
        CommentEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        CommentEntity newEntity = factory.manufacturePojo(CommentEntity.class);

        newEntity.setId(entity.getId());
        newEntity.setAuthor(resident);

        commentPersistence.update(newEntity, neighborhood.getId());

       CommentEntity resp =  commentPersistence.find(entity.getId(), neighborhood.getId());

        Assert.assertNotNull(newEntity);
        Assert.assertEquals(resp.getDate(), resp.getDate());
        Assert.assertEquals(resp.getText(), resp.getText());
    }

    /**
     * Test for deleting comment.
     */
    @Test
    public void deleteResidentTest() {
        CommentEntity entity = data.get(0);
        commentPersistence.delete(entity.getId(), neighborhood.getId());
        CommentEntity deleted = em.find(CommentEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

}
