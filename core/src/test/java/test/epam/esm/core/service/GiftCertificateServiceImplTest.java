package test.epam.esm.core.service;

import com.epam.esm.core.config.MapperConfig;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Order;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.model.dto.request.CertificatePageRequest;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.core.service.impl.GiftCertificateServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GiftCertificateServiceImplTest {
    private final GiftCertificateServiceImpl service;
    private final TagRepositoryImpl tagRepository;
    private final GiftCertificateRepositoryImpl certificateRepository;

    private List<GiftCertificate> certificateList;
    private List<GiftCertificateDto> certificateDtoList;


    public GiftCertificateServiceImplTest() {
        this.tagRepository = Mockito.mock(TagRepositoryImpl.class);
        this.certificateRepository = Mockito.mock(GiftCertificateRepositoryImpl.class);
        ModelMapper modelMapper = new MapperConfig().modelMapper();
        this.service = new GiftCertificateServiceImpl(certificateRepository, tagRepository, modelMapper);
    }

    @BeforeEach
    public void setUp() {
        certificateDtoList = new ArrayList<>();
        certificateList = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            GiftCertificate giftCertificate = new GiftCertificate.GiftCertificateBuilder()
                    .id(i)
                    .createDate(LocalDateTime.now().minusHours(i))
                    .lastUpdateDate(LocalDateTime.now().minusMinutes(i))
                    .price(BigDecimal.valueOf(i))
                    .description("description_" + i)
                    .name("name_" + i)
                    .duration(Long.valueOf(i).intValue())
                    .tags(Collections.singleton(new Tag(6 - i, "tag_name_" + i)))
                    .build();

            GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
            giftCertificateDto.setId(giftCertificate.getId());
            giftCertificateDto.setName(giftCertificate.getName());
            giftCertificateDto.setDescription(giftCertificate.getDescription());
            giftCertificateDto.setDuration(giftCertificate.getDuration());
            giftCertificateDto.setPrice(giftCertificate.getPrice());
            giftCertificateDto.setCreateDate(giftCertificate.getCreateDate());
            giftCertificateDto.setLastUpdateDate(giftCertificate.getLastUpdateDate());
            giftCertificateDto.setTags(giftCertificate.getTags()
                    .stream()
                    .map(tag -> new TagDto(tag.getId(), tag.getName()))
                    .collect(Collectors.toSet()));

            certificateList.add(giftCertificate);
            certificateDtoList.add(giftCertificateDto);
        }
    }

    @Test
    void givenNewCertificate_whenCreate_thenReturn() throws ServiceException {
        GiftCertificateDto expected = certificateDtoList.get(2);
        GiftCertificate giftCertificate = certificateList.get(2);
        Tag tag = new Tag(expected.getId(), "tag_" + expected.getId());
        TagDto tagDto = new TagDto(expected.getId(), "tag_" + expected.getId());
        giftCertificate.setTags(Collections.singleton(tag));
        expected.setTags(Collections.singleton(tagDto));

        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(tag));
        Mockito.when(certificateRepository.create(Mockito.any())).thenReturn(giftCertificate);
        GiftCertificateDto actual = service.create(expected);

        assertEquals(expected, actual);
        Mockito.verify(tagRepository, Mockito.times(1)).findByName(Mockito.anyString());
        Mockito.verify(certificateRepository, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void givenNewCertificateNewTag_whenCreate_thenReturn() throws ServiceException {
        GiftCertificateDto expected = certificateDtoList.get(2);
        GiftCertificate giftCertificate = certificateList.get(2);

        Tag tag = new Tag(null, "tag_name_" + expected.getId());
        TagDto tagDto = new TagDto(null, "tag_name_" + expected.getId());
        expected.setTags(Collections.singleton(tagDto));
        giftCertificate.setTags(Collections.singleton(tag));

        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(certificateRepository.create(Mockito.any())).thenReturn(giftCertificate);
        GiftCertificateDto actual = service.create(expected);

        assertEquals(expected, actual);
        Mockito.verify(tagRepository, Mockito.times(1)).findByName(Mockito.anyString());
        Mockito.verify(certificateRepository, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void givenCertificates_whenFindAll_thenReturn() {
        Mockito.when(certificateRepository.findAll(Mockito.any())).thenReturn(certificateList);

        List<GiftCertificateDto> actual = service.findAll(SimplePageRequest.of(1, 10));
        Assertions.assertThat(actual).containsExactlyElementsOf(certificateDtoList);

        Mockito.verify(certificateRepository, Mockito.times(1)).findAll(Mockito.any());
    }

    @Test
    void givenCertificate_whenFindById_thenReturn() throws ServiceException {
        GiftCertificateDto expected = certificateDtoList.get(1);
        GiftCertificate certificate = certificateList.get(1);
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(certificate));
        GiftCertificateDto actual = service.findById(1L);
        assertEquals(expected, actual);
        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenCertificateNotExist_whenFindById_thenNotFound() throws ServiceException {
        long id = 1L;

        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        ServiceException exception = assertThrows(ServiceException.class, () -> service.findById(id));

        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals(Long.toString(id), exception.getMessage());

        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenCertificate_whenUpdate_thenReturn() throws ServiceException {
        GiftCertificateDto expected = certificateDtoList.get(2);
        GiftCertificate giftCertificate = certificateList.get(2);
        Tag tag = new Tag(expected.getId(), "tag_" + expected.getId());
        TagDto tagDto = new TagDto(expected.getId(), "tag_" + expected.getId());
        giftCertificate.setTags(Collections.singleton(tag));
        expected.setTags(Collections.singleton(tagDto));

        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(giftCertificate));
        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(tag));
        Mockito.when(certificateRepository.update(Mockito.any())).thenReturn(giftCertificate);

        GiftCertificateDto actual = service.update(expected);

        assertEquals(expected, actual);

        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(tagRepository, Mockito.times(1)).findByName(Mockito.anyString());
        Mockito.verify(certificateRepository, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    void givenCertificateNotExist_whenUpdate_thenNotFound() throws ServiceException {
        GiftCertificateDto certificateDto = certificateDtoList.get(2);

        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(certificateRepository.update(Mockito.any())).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.update(certificateDto));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals(Long.toString(certificateDto.getId()), exception.getMessage());

        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(tagRepository, Mockito.times(0)).findByName(Mockito.anyString());
        Mockito.verify(certificateRepository, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenCertificate_whenDeleteById_thenOk() throws ServiceException {
        GiftCertificate certificate = certificateList.get(2);
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(certificate));
        Mockito.doNothing().when(certificateRepository).deleteById(Mockito.anyLong());
        service.deleteById(certificate.getId());
        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(certificateRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenCertificateNotExist_whenDeleteById_thenNotFound() throws ServiceException {
        long id = 222L;
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.doNothing().when(certificateRepository).deleteById(Mockito.anyLong());

        ServiceException exception = assertThrows(ServiceException.class, () -> service.deleteById(id));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals(Long.toString(id), exception.getMessage());

        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(certificateRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenCertificateHasOrders_whenDeleteById_thenConflict() throws ServiceException {
        GiftCertificate certificate = certificateList.get(2);
        certificate.setOrders(Collections.singleton(new Order()));
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(certificate));
        Mockito.doNothing().when(certificateRepository).deleteById(Mockito.anyLong());

        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.deleteById(certificate.getId()));
        assertEquals(CustomErrorCode.CONFLICT_DELETE, exception.getErrorCode());
        assertEquals(Long.toString(certificate.getId()), exception.getMessage());

        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(certificateRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }

    @Test
    void givenCertificates_whenFindAllByParameters_thenReturn() {
        Mockito.when(certificateRepository.findAll(Mockito.any(), Mockito.any())).thenReturn(certificateList);
        CertificatePageRequest pageRequest = new CertificatePageRequest();
        List<GiftCertificateDto> actual = service.findAllByParameters(pageRequest);
        Assertions.assertThat(actual).containsExactlyElementsOf(certificateDtoList);
        Mockito.verify(certificateRepository, Mockito.times(1))
                .findAll(Mockito.any(), Mockito.any());
    }
}