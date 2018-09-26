package de.terrestris.shoguncore.service;

import de.terrestris.shoguncore.dao.AbstractUserTokenDao;
import de.terrestris.shoguncore.model.token.UserToken;

/**
 * Test for the {@link AbstractUserTokenService}.
 *
 * @author Nils Bühner
 */
public abstract class AbstractUserTokenServiceTest<E extends UserToken, D extends AbstractUserTokenDao<E>, S extends AbstractUserTokenService<E, D>>
    extends PermissionAwareCrudServiceTest<E, D, S> {

}
