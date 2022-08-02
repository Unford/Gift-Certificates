package com.epam.esm.api.hateoas.assembler;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;
import java.util.List;

/**
 * This is an interface that defines the methods that are used to convert a collection of objects
 * into a collection model with pagination links.
 */
public interface CollectionModelAssembler<T extends RepresentationModel<T>> {

    /**
     * Given a collection of objects, return a list of links to the objects
     *
     * @param collection The collection of objects to be converted to links.
     * @param simplePage The page request object.
     * @return A list of links.
     */
    List<Link> getCollectionLinks(Collection<T> collection,
                                  SimplePageRequest simplePage) throws ServiceException;

    /**
     * > This function converts a collection of objects into a collection model with pagination links
     *
     * @param collection  The collection of entities to be converted to a CollectionModel.
     * @param pageRequest The page request object that contains the page number and page size.
     * @return A collection model with the collection and the links.
     */
    default CollectionModel<T> toCollectionModel(Collection<T> collection, SimplePageRequest pageRequest)
            throws ServiceException {
        CollectionModel<T> collectionModel = CollectionModel.empty();
        if (collection != null && !collection.isEmpty()) {
            collectionModel = CollectionModel.of(collection, getCollectionLinks(collection, pageRequest));
        }
        return collectionModel;
    }

}
