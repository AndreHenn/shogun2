package de.terrestris.shoguncore.service;

import de.terrestris.shoguncore.dao.GenericHibernateDao;
import de.terrestris.shoguncore.helper.IdHelper;
import de.terrestris.shoguncore.model.PersistentObject;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Abstract (parent) test for the {@link AbstractCrudService}.
 *
 * @author Nils Bühner
 */
public abstract class AbstractCrudServiceTest<E extends PersistentObject, D extends GenericHibernateDao<E, Integer>, S extends AbstractCrudService<E, D>> {

    /**
     * Object that holds concrete implementations of {@link PersistentObject}
     * for the tests.
     */
    protected E implToTest = null;

    protected D dao;

    @InjectMocks
    protected S crudService;

    @Before
    public void setUp() {
        // see here why we are mocking this way:
        // http://stackoverflow.com/a/24302622
        dao = mock(getDaoClass());
        this.crudService = getCrudService();
        // Process mock annotations
        MockitoAnnotations.initMocks(this);
    }

    /**
     * This method has to be implemented by subclasses.
     *
     * @throws Exception
     */
    @Before
    public abstract void setUpImplToTest() throws Exception;

    /**
     * This method has to be implemented by subclasses to return a concrete
     * implementation of the tested service.
     *
     * @return
     */
    protected abstract S getCrudService();

    /**
     * This method has to be implemented by subclasses to return the concrete
     * class of the dao.
     *
     * @return
     */
    protected abstract Class<D> getDaoClass();


    @After
    public void tearDownAfterEachTest() throws Exception {
        implToTest = null;
    }

    /**
     * Test whether the saveOrUpdate method <i>saves</i> a
     * {@link PersistentObject}, which initially has no id value.
     */
    @SuppressWarnings("unchecked")
    //@Test
    public void saveOrUpdate_shouldSave() {

        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation)
                throws NoSuchFieldException, SecurityException,
                IllegalArgumentException, IllegalAccessException {
                E po = (E) invocation.getArguments()[0];

                // set id like the dao does
                IdHelper.setIdOnPersistentObject(po, ""+1);

                return null;
            }
        }).when(dao).saveOrUpdate(implToTest);

        // id has to be NULL before the service method is called
        assertNull(implToTest.getId());

        crudService.saveOrUpdate(implToTest);

        // id must not be NULL after the service method is called
        assertNotNull(implToTest.getId());
//        assertTrue(implToTest.getId() > 0);

        // be sure that dao method has been executed exactly once
        verify(dao, times(1)).saveOrUpdate(implToTest);
    }

    /**
     * Test whether
     * {@link AbstractCrudService#saveOrUpdate(PersistentObject)}
     * <i>updates</i> the modified value of a {@link PersistentObject}.
     *
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InterruptedException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void saveOrUpdate_shouldUpdate() throws NoSuchFieldException,
        SecurityException, IllegalArgumentException,
        IllegalAccessException, InterruptedException {

    }

    /**
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    //@Test
    public void findById_shouldFindExistingId() throws NoSuchFieldException,
        IllegalAccessException {

        Integer existingId = 17;

        IdHelper.setIdOnPersistentObject(implToTest, ""+existingId);

        // mock dao behavior
        //doReturn(implToTest).when(dao).findById(existingId);

        // actually test
        //E result = crudService.findById(existingId);

       // assertNotNull(result);
        //assertEquals(existingId, result.getId());

        // be sure that dao method has been executed exactly once
        //verify(dao, times(1)).findById(existingId);
    }

    /**
     *
     */
   // @Test
    public void saveOrUpdate_shouldReturnNullForNonExistingId() {

        Integer nonExistingId = 42;

        // mock behaviour of dao, which is called in the service
       // doReturn(null).when(dao).findById(nonExistingId);

        // actually test
     //   E result = crudService.findById(nonExistingId);

      //  assertNull(result);

        // be sure that dao method has been executed exactly once
     //   verify(dao, times(1)).findById(nonExistingId);
    }

    /**
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @SuppressWarnings({"unchecked"})
    @Test
    public void findAll_shouldFindAll() throws NoSuchFieldException,
        IllegalAccessException, InstantiationException,
        InvocationTargetException, NoSuchMethodException {

        final Integer id1 = 17;
        final Integer id2 = 42;
        List<E> persistedObjectList = new ArrayList<E>();

        E obj1 = implToTest;
        E obj2 = (E) BeanUtils.cloneBean(obj1);

        IdHelper.setIdOnPersistentObject(obj1, ""+id1);
        IdHelper.setIdOnPersistentObject(obj2, ""+id2);

        persistedObjectList.add((E) obj1);
        persistedObjectList.add((E) obj2);
    }

    /**
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    @Test
    public void delete_shouldDelete() throws NoSuchFieldException,
        IllegalAccessException {

        Set<E> persistedObjects = new HashSet<E>();
        persistedObjects.add(implToTest);

        doNothing().when(dao).delete(implToTest);

        // actually test
        crudService.delete(implToTest);

        // be sure that dao.delete() has been executed exactly once
        verify(dao, times(1)).delete(implToTest);

        // maybe this test can be enhanced...
    }



}
