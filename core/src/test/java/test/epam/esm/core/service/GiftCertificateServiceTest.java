package test.epam.esm.core.service;

import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.core.dao.impl.TagDaoImpl;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.service.GiftCertificateService;
import com.epam.esm.core.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

class GiftCertificateServiceTest {
    private GiftCertificateDao certificateDao = Mockito.mock(GiftCertificateDaoImpl.class);
    private TagDao tagDao = Mockito.mock(TagDaoImpl.class);

    private GiftCertificateService service = new GiftCertificateServiceImpl(certificateDao, tagDao);

    private List<GiftCertificate> giftCertificates;

    @BeforeEach
    public void setUp() {
        giftCertificates = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            LocalDateTime time = LocalDateTime.now().plusHours(i);
            Set<Tag> tagSet = new HashSet<>();
            for (int j = 1; j < i; j++) {
                tagSet.add(new Tag(j, "tag_" + j));
            }
            GiftCertificate certificate = new GiftCertificate.GiftCertificateBuilder()
                    .id(i)
                    .name("gift_certificate_" + i)
                    .description("description_" + i)
                    .price(BigDecimal.valueOf(i))
                    .duration(i)
                    .createDate(time)
                    .lastUpdateDate(time)
                    .tags(tagSet)
                    .build();
            giftCertificates.add(certificate);
        }
    }

    @Test
    void testCertificateServiceFindAll() {
        Mockito.when(certificateDao.findAll()).thenReturn(giftCertificates);
        List<GiftCertificate> actual = service.findAll();
        Assertions.assertEquals(giftCertificates, actual);
        Mockito.verify(certificateDao, Mockito.times(1)).findAll();
    }

    @Test
    void testCertificateServiceFindById() throws ServiceException {
        Mockito.when(certificateDao.findById(Mockito.anyLong())).thenReturn(Optional.of(giftCertificates.get(0)));
        GiftCertificate actual = service.findById(1);
        Assertions.assertEquals(giftCertificates.get(0), actual);
        Mockito.verify(certificateDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testCertificateServiceFindByIdThrowsException() throws ServiceException {
        Mockito.when(certificateDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> service.findById(-1));
        Mockito.verify(certificateDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testCertificateServiceDeleteById() throws ServiceException {
        Mockito.when(certificateDao.deleteById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(certificateDao.findById(Mockito.anyLong())).thenReturn(Optional.of(giftCertificates.get(1)));
        Assertions.assertTrue(service.deleteById(1));
        Mockito.verify(certificateDao, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(certificateDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testCertificateServiceDeleteByIdNotFound() throws ServiceException {
        Mockito.when(certificateDao.deleteById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(certificateDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> service.deleteById(-1));
        Mockito.verify(certificateDao, Mockito.times(0)).deleteById(Mockito.anyLong());
        Mockito.verify(certificateDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void testCertificateServiceCreate() throws ServiceException {
        GiftCertificate expected = new GiftCertificate.GiftCertificateBuilder()
                .name("new certificate")
                .description("new description")
                .duration(1)
                .price(BigDecimal.ONE)
                .build();
        Mockito.when(certificateDao.create(Mockito.any())).thenReturn(expected);
        GiftCertificate actual = service.create(expected);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(certificateDao, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void testCertificateServiceCreateWithTags() throws ServiceException {
        Set<Tag> tagSet = giftCertificates.get(2).getTags();
        GiftCertificate expected = new GiftCertificate.GiftCertificateBuilder()
                .name("new certificate")
                .description("new description")
                .duration(1)
                .price(BigDecimal.ONE)
                .tags(tagSet)
                .build();
        Mockito.when(certificateDao.create(Mockito.any())).thenReturn(expected);
        Mockito.when(tagDao.findByName(Mockito.anyString()))
                .thenReturn(tagSet.stream().findFirst())
                .thenReturn(Optional.of(tagSet.iterator().next()));
        Mockito.when(certificateDao.isManyToManyLinkExist(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(true);
        GiftCertificate actual = service.create(expected);

        Assertions.assertEquals(expected, actual);
        Mockito.verify(certificateDao, Mockito.times(1)).create(Mockito.any());
        Mockito.verify(tagDao, Mockito.times(2)).findByName(Mockito.anyString());
        Mockito.verify(certificateDao, Mockito.times(2))
                .isManyToManyLinkExist(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void testCertificateServiceUpdate() throws ServiceException {
        Set<Tag> tagSet = giftCertificates.get(2).getTags();
        GiftCertificate expected = new GiftCertificate.GiftCertificateBuilder()
                .name("update certificate")
                .description("update description")
                .duration(2)
                .price(BigDecimal.ZERO)
                .tags(tagSet)
                .build();

        Mockito.when(certificateDao.findById(Mockito.anyLong())).thenReturn(Optional.of(giftCertificates.get(1)));
        Mockito.when(tagDao.findByName(Mockito.anyString()))
                .thenReturn(tagSet.stream().findFirst())
                .thenReturn(Optional.of(tagSet.iterator().next()));
        Mockito.when(certificateDao.isManyToManyLinkExist(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(certificateDao.update(Mockito.any())).thenReturn(Optional.of(expected));

        GiftCertificate actual = service.update(expected);

        Assertions.assertEquals(expected, actual);
        Mockito.verify(certificateDao, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(certificateDao, Mockito.times(1)).update(Mockito.any());
        Mockito.verify(tagDao, Mockito.times(2)).findByName(Mockito.anyString());
        Mockito.verify(certificateDao, Mockito.times(2))
                .isManyToManyLinkExist(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void testCertificateServiceThrowsException() {
        Mockito.when(certificateDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> service.update(giftCertificates.get(1)));
    }
}
