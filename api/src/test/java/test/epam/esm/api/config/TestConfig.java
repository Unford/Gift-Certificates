package test.epam.esm.api.config;

import com.epam.esm.core.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.core.service.impl.TagServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    public TagServiceImpl tagService(){
        return Mockito.mock(TagServiceImpl.class);
    }

    @Bean
    public GiftCertificateServiceImpl giftCertificateService(){
        return Mockito.mock(GiftCertificateServiceImpl.class);
    }


}
