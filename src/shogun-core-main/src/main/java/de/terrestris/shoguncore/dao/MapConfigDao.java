package de.terrestris.shoguncore.dao;

import de.terrestris.shoguncore.model.map.MapConfig;
import org.springframework.stereotype.Repository;

@Repository("mapConfigDao")
public class MapConfigDao<E extends MapConfig> extends
    GenericHibernateDao<E, Integer> {

    /**
     * Public default constructor for this DAO.
     */
    @SuppressWarnings("unchecked")
    public MapConfigDao() {
        super((Class<E>) MapConfig.class);
    }

    /**
     * Constructor that has to be called by subclasses.
     *
     * @param clazz
     */
    protected MapConfigDao(Class<E> clazz) {
        super(clazz);
    }

}
