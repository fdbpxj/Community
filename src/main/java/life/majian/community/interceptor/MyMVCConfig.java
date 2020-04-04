package life.majian.community.interceptor;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

public class MyMVCConfig extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/Path/**").addResourceLocations("file:/G:/Community/src/main/resources/static/");
        super.addResourceHandlers(registry);

    }
}
