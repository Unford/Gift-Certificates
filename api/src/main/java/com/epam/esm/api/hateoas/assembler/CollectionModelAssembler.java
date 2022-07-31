package com.epam.esm.api.hateoas.assembler;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;
import java.util.List;

public interface CollectionModelAssembler<T extends RepresentationModel<T>> {

    List<Link> getCollectionLinks(Collection<T> collection,
                                  SimplePageRequest simplePage) throws ServiceException;

    default CollectionModel<T> toCollectionModel(Collection<T> collection, SimplePageRequest pageRequest)
            throws ServiceException {
        CollectionModel<T> collectionModel = CollectionModel.empty();
        if (collection != null && !collection.isEmpty()) {
            collectionModel = CollectionModel.of(collection, getCollectionLinks(collection, pageRequest));
        }
        return collectionModel;
    }

}
