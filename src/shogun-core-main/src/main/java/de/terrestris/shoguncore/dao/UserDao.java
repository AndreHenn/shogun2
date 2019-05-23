package de.terrestris.shoguncore.dao;

import de.terrestris.shoguncore.model.User;
import de.terrestris.shoguncore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDao<E extends User> extends PersonDao<E> {

    @Autowired
    private UserRepository userRepository;

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
        if (userRepository != null) {
            E user = (E) userRepository.findByAccountName(accountName);
            return user;
        }
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
