package test.epam.esm.core.service;

import com.epam.esm.core.dao.impl.TagDaoImpl;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class TagServiceTest {
    private final TagDaoImpl tagDao = Mockito.mock(TagDaoImpl.class);

    private TagServiceImpl tagService = new TagServiceImpl(tagDao);
    private List<Tag> tags;

    @BeforeEach
    public void setUp() {
        tags = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tags.add(new Tag(i, "tag_" + i));
        }
    }

    @Test
    void testTagServiceFindAll() {
        Mockito.when(tagDao.findAll()).thenReturn(tags);
        List<Tag> actual = tagService.findAll();
        Assertions.assertEquals(tags, actual);
        Mockito.verify(tagDao, Mockito.times(1)).findAll();
    }

    @Test
    void testTagServiceFindById() throws ServiceException {
        Mockito.when(tagDao.findById(Mockito.anyLong())).thenReturn(Optional.of(tags.get(0)));
        Tag actual = tagService.findById(1);
        Assertions.assertEquals(tags.get(0), actual);
        Mockito.verify(tagDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testTagServiceFindByIdThrowsException() throws ServiceException {
        Mockito.when(tagDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> tagService.findById(-1));
        Mockito.verify(tagDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testTagServiceDeleteById() throws ServiceException {
        Mockito.when(tagDao.deleteById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(tagDao.findById(Mockito.anyLong())).thenReturn(Optional.of(tags.get(1)));
        Mockito.when(tagDao.isAnyLinksToTag(Mockito.anyLong())).thenReturn(false);

        Assertions.assertTrue(tagService.deleteById(1));
        Mockito.verify(tagDao, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(tagDao, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(tagDao, Mockito.times(1)).isAnyLinksToTag(Mockito.anyLong());

    }

    @Test
    void testTagServiceDeleteByIdNotFound() throws ServiceException {
        Mockito.when(tagDao.deleteById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(tagDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(tagDao.isAnyLinksToTag(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(ServiceException.class, () -> tagService.deleteById(-1));
        Mockito.verify(tagDao, Mockito.times(0)).deleteById(Mockito.anyLong());
        Mockito.verify(tagDao, Mockito.times(0)).isAnyLinksToTag(Mockito.anyLong());
        Mockito.verify(tagDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testTagServiceDeleteByIdAnyLinks() throws ServiceException {
        Mockito.when(tagDao.deleteById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(tagDao.findById(Mockito.anyLong())).thenReturn(Optional.of(tags.get(0)));
        Mockito.when(tagDao.isAnyLinksToTag(Mockito.anyLong())).thenReturn(true);
        Assertions.assertThrows(ServiceException.class, () -> tagService.deleteById(3));
        Mockito.verify(tagDao, Mockito.times(0)).deleteById(Mockito.anyLong());
        Mockito.verify(tagDao, Mockito.times(1)).isAnyLinksToTag(Mockito.anyLong());
        Mockito.verify(tagDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testTagServiceUpdateException() throws ServiceException {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> tagService.update(tags.get(3)));
    }

    @Test
    void testTagServiceCreate() throws ServiceException {
        Tag expected = new Tag(5, "new tag");
        Mockito.when(tagDao.create(Mockito.any())).thenReturn(expected);
        Mockito.when(tagDao.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Tag actual = tagService.create(new Tag(0, "new tag"));
        Assertions.assertEquals(expected, actual);
        Mockito.verify(tagDao, Mockito.times(1)).create(Mockito.any());
        Mockito.verify(tagDao, Mockito.times(1)).findByName(Mockito.anyString());
    }

    @Test
    void testTagServiceAlreadyExist() throws ServiceException {
        Tag expected = new Tag(1, "tag_1");
        Mockito.when(tagDao.create(Mockito.any())).thenReturn(expected);
        Mockito.when(tagDao.findByName(Mockito.anyString())).thenReturn(Optional.of(expected));
        Assertions.assertThrows(ServiceException.class, () -> tagService.create(expected));
        Mockito.verify(tagDao, Mockito.times(0)).create(Mockito.any());
        Mockito.verify(tagDao, Mockito.times(1)).findByName(Mockito.anyString());
    }

}
