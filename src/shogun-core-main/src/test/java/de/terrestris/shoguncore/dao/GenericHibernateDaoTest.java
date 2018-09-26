package de.terrestris.shoguncore.dao;

import de.terrestris.shoguncore.model.Application;
import de.terrestris.shoguncore.model.Plugin;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * The most basic concrete class extending the {@link GenericHibernateDao}, will
 * be used to test the functionality that the abstract class provides. This
 * class will operate on the {@link Application} class, but any model-class
 * could have been picked.
 */
@Repository
class AppTestDao extends GenericHibernateDao<Application, Integer> {
    protected AppTestDao() {
        super(Application.class);
    }
}

/**
 * @author Nils Bühner
 */
@Repository
class PluginTestDao extends GenericHibernateDao<Plugin, Integer> {
    protected PluginTestDao() {
        super(Plugin.class);
    }
}

/**
 * This class will test the {@link GenericHibernateDao}. As
 * {@link GenericHibernateDao} is an abstract class, we cannot instantiate it.
 * Instead we will use the {@link AppTestDao} (as the most basic extension of
 * {@link GenericHibernateDao}) to test the logic contained in
 * {@link GenericHibernateDao}.
 *
 * @author Marc Jansen
 * @author Nils Bühner
 */
public class GenericHibernateDaoTest {

}
