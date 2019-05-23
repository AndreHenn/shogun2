package de.terrestris.shoguncore.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.terrestris.shoguncore.dao.GenericHibernateDao;
import de.terrestris.shoguncore.dao.PermissionCollectionDao;
import de.terrestris.shoguncore.model.PersistentObject;
import de.terrestris.shoguncore.model.User;
import de.terrestris.shoguncore.model.UserGroup;
import de.terrestris.shoguncore.model.security.Permission;
import de.terrestris.shoguncore.model.security.PermissionCollection;

/**
 * Abstract (parent) test for the
 * {@link PermissionAwareCrudServiceTest}.
 *
 * @author Nils Bühner
 */
public abstract class PermissionAwareCrudServiceTest<E extends PersistentObject, D extends GenericHibernateDao<E, Integer>, S extends PermissionAwareCrudService<E, D>>
    extends AbstractCrudServiceTest<E, D, S> {


}
