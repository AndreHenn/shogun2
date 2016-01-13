package de.terrestris.shogun2.dao;

import org.springframework.stereotype.Repository;

import de.terrestris.shogun2.model.token.Token;

/**
 * As the {@link Token} class is abstract, this class will also be abstract.
 * There will also be NO {@link Repository} annotation here.
 * 
 * @author Nils Bühner
 *
 * @param <E>
 */
public abstract class AbstractTokenDao<E extends Token> extends
		GenericHibernateDao<E, Integer> {

	/**
	 * Constructor that has to be called by subclasses.
	 * 
	 * @param clazz
	 */
	protected AbstractTokenDao(Class<E> clazz) {
		super(clazz);
	}

}
