package de.terrestris.shoguncore.service;

import de.terrestris.shogun2.dao.RoleDao;
import de.terrestris.shogun2.model.Role;

public class RoleServiceTest extends PermissionAwareCrudServiceTest<Role, RoleDao<Role>, RoleService<Role, RoleDao<Role>>> {

    /**
     * @throws Exception
     */
    public void setUpImplToTest() throws Exception {
        implToTest = new Role();
    }

    @Override
    protected RoleService<Role, RoleDao<Role>> getCrudService() {
        return new RoleService<Role, RoleDao<Role>>();
    }



    @SuppressWarnings("unchecked")
    @Override
    protected Class<RoleDao<Role>> getDaoClass() {
        return (Class<RoleDao<Role>>) new RoleDao<Role>().getClass();
    }

}
