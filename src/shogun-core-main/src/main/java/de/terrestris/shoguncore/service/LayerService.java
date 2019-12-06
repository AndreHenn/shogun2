package de.terrestris.shoguncore.service;

import de.terrestris.shoguncore.dao.LayerDao;
import de.terrestris.shoguncore.dao.MapDao;
import de.terrestris.shoguncore.model.layer.Layer;
import de.terrestris.shoguncore.model.module.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service class for the {@link Layer} model.
 *
 * @author Nils Bühner
 * @see PermissionAwareCrudService
 */
@Service("layerService")
public class LayerService<E extends Layer, D extends LayerDao<E>>
    extends PermissionAwareCrudService<E, D> {

    /**
     *
     */
    @Autowired
    @Qualifier("mapService")
    MapService<Map, MapDao<Map>> mapService;

    /**
     * Default constructor, which calls the type-constructor
     */
    @SuppressWarnings("unchecked")
    public LayerService() {
        this((Class<E>) Layer.class);
    }

    /**
     * Constructor that sets the concrete entity class for the service.
     * Subclasses MUST call this constructor.
     */
    protected LayerService(Class<E> entityClass) {
        super(entityClass);
    }

    /**
     * We have to use {@link Qualifier} to define the correct dao here.
     * Otherwise, spring can not decide which dao has to be autowired here
     * as there are multiple candidates.
     */
    @Override
    @Autowired
    @Qualifier("layerDao")
    public void setDao(D dao) {
        this.dao = dao;
    }

    /**
     *
     */
    @Override
    @PreAuthorize("hasRole(@configHolder.getSuperAdminRoleName()) or hasPermission(#layer, 'DELETE')")
    public void delete(E layer) {
        // get all maps that contain the layer
        Set<Map> maps = mapService.findMapsWithLayer(layer);

        logger.info("Found " + maps.size() + " maps with layer " + layer);

        // remove the layer from these maps
        for (Map map : maps) {
            map.getMapLayers().remove(layer);
            mapService.saveOrUpdate(map);

            logger.info("Removed layer from map");
        }

        // finally remove the layer
        super.delete(layer);
    }

}
