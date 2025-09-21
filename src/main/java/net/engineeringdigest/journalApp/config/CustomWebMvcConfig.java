package net.engineeringdigest.journalApp.config;

import net.engineeringdigest.journalApp.filter.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//RateLimiterInterCeptor will be used here to intercept
@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/journal/**"); // rate limit only these   .addPathPatterns("/journal/**", "/external/**");


        //Other examples- If you wanted to apply both rateLimitInterceptor and authInterceptor to /journal/**, but only authInterceptor to /admin/**, you could do:
//        registry.addInterceptor(rateLimitInterceptor)
//                .addPathPatterns("/journal/**");
//
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/journal/**", "/admin/**");

        //The order of interceptors matters: they execute in the order they are registered.
    }


}
