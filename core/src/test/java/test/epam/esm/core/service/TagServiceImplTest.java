package test.epam.esm.core.service;

import com.epam.esm.core.config.MapperConfig;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.core.service.impl.TagServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


class TagServiceImplTest {
    private final TagServiceImpl tagService;
    private final TagRepositoryImpl tagRepository;
    private List<Tag> expectedTagList;
    private List<TagDto> expectedTagDtoList;


    public TagServiceImplTest() {
        this.tagRepository = Mockito.mock(TagRepositoryImpl.class);
        ModelMapper modelMapper = new MapperConfig().modelMapper();
        this.tagService = new TagServiceImpl(tagRepository, modelMapper);
    }

    @BeforeEach
    public void setUp() {
        expectedTagList = new ArrayList<>();
        expectedTagDtoList = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            expectedTagList.add(new Tag(i, "tag_" + i));
            expectedTagDtoList.add(new TagDto(i, "tag_" + i));
        }
    }

    @Test
    void givenNotExistTag_whenCreate_thenReturn() throws ServiceException {
        TagDto expected = new TagDto(5L, "new tag");
        Tag expectedTag = new Tag(expected.getId(), expected.getName());

        Mockito.when(tagRepository.create(any())).thenReturn(expectedTag);
        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());

        TagDto actual = tagService.create(new TagDto(null, expected.getName()));
        assertEquals(expected, actual);
        Mockito.verify(tagRepository, Mockito.times(1)).create(any());
        Mockito.verify(tagRepository, Mockito.times(1)).findByName(Mockito.anyString());
    }

    @Test
    void givenTag_whenCreate_thenAlreadyExist() throws ServiceException {
        Tag expected = expectedTagList.get(0);
        TagDto newTag = new TagDto(null, expected.getName());

        Mockito.when(tagRepository.create(any())).thenReturn(expected);
        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(expected));
        ServiceException exception = assertThrows(ServiceException.class, () -> tagService.create(newTag));
        assertEquals(CustomErrorCode.RESOURCE_ALREADY_EXIST, exception.getErrorCode());
        Mockito.verify(tagRepository, Mockito.times(0)).create(any());
        Mockito.verify(tagRepository, Mockito.times(1)).findByName(Mockito.anyString());
    }

    @Test
    void givenTags_whenFindAll_thenReturn() {
        Mockito.when(tagRepository.findAll(any())).thenReturn(expectedTagList);
        List<TagDto> actual = tagService.findAll(SimplePageRequest.of(1, 10));
        Assertions.assertThat(actual).containsExactlyElementsOf(expectedTagDtoList);
        Mockito.verify(tagRepository, Mockito.times(1)).findAll(any());
    }

    @Test
    void givenTag_whenFindById_thenReturn() throws ServiceException {
        TagDto expected = expectedTagDtoList.get(1);
        Tag expectedTag = expectedTagList.get(1);
        Mockito.when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedTag));
        TagDto actual = tagService.findById(1L);
        assertEquals(expected, actual);
        Mockito.verify(tagRepository, Mockito.times(1)).findById(Mockito.anyLong());

    }

    @Test
    void givenTagNotExist_whenFindById_thenNotFound() throws ServiceException {
        Mockito.when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        ServiceException exception = assertThrows(ServiceException.class, () -> tagService.findById(-1));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        Mockito.verify(tagRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenTag_whenDeleteById_thenOk() throws ServiceException {
        Tag tag = expectedTagList.get(2);
        Mockito.when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(tag));
        Mockito.doNothing().when(tagRepository).deleteById(Mockito.anyLong());
        tagService.deleteById(2L);
        Mockito.verify(tagRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(tagRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenTagNotExist_whenDeleteById_thenNotFound() throws ServiceException {
        Mockito.when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.doNothing().when(tagRepository).deleteById(Mockito.anyLong());

        ServiceException exception = assertThrows(ServiceException.class, () -> tagService.deleteById(1));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());

        Mockito.verify(tagRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(tagRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenTagHasCertificates_whenDeleteById_thenConflict() throws ServiceException {
        Tag tag = expectedTagList.get(2);
        tag.setGiftCertificates(Collections.singleton(new GiftCertificate()));

        Mockito.when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(tag));
        Mockito.doNothing().when(tagRepository).deleteById(Mockito.anyLong());

        ServiceException exception = assertThrows(ServiceException.class, () -> tagService.deleteById(1));
        assertEquals(CustomErrorCode.CONFLICT_DELETE, exception.getErrorCode());

        Mockito.verify(tagRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(tagRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenUnsupportedOperation_whenUpdate_thenThrow() {
        TagDto tagDto = expectedTagDtoList.get(0);
        Mockito.when(tagRepository.update(Mockito.any())).thenReturn(null);
        assertThrows(UnsupportedOperationException.class, () -> tagService.update(tagDto));
        Mockito.verify(tagRepository, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenTag_whenFindTheMostWidelyUsedTag_thenReturn() throws ServiceException {
        TagDto expectedTagDto = expectedTagDtoList.get(1);
        Tag expected = expectedTagList.get(1);

        Mockito.when(tagRepository.findTheMostWidelyUsedTag()).thenReturn(Optional.ofNullable(expected));
        TagDto actual = tagService.findTheMostWidelyUsedTag();
        assertEquals(expectedTagDto, actual);
        Mockito.verify(tagRepository, Mockito.times(1)).findTheMostWidelyUsedTag();
    }
}