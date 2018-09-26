package de.terrestris.shoguncore.dao;

import org.springframework.stereotype.Repository;

import de.terrestris.shoguncore.model.User;

@Repository("userDao")
public class UserDao<E extends User> extends PersonDao<E> {

    /**
     * Public default constructor for this DAO.
     */
    @SuppressWarnings("unchecked")
    public UserDao() {
        super((Class<E>) User.class);
    }

    /**
     * Constructor that has to be called by subclasses.
     *
     * @param clazz
     */
    protected UserDao(Class<E> clazz) {
        super(clazz);
    }

    /**
     * @param accountName
     * @return
     */
    public E findByAccountName(String accountName) {
return null;
    }

    /**
     * @param email
     * @return
     */
    public E findByEmail(String email) {
        return null;
    }
}
