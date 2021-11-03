package com.alkemy.java.config;

import com.alkemy.java.repository.CommentRepository;
import com.alkemy.java.repository.NewsRepository;
import com.alkemy.java.repository.UserRepository;
import com.alkemy.java.service.ICommentService;
import com.alkemy.java.service.INewsService;
import com.alkemy.java.service.impl.UserDetailsServiceImpl;
import com.alkemy.java.util.JwtUtil;
import com.alkemy.java.util.UtilPagination;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Config {
    @Bean
    public JwtUtil jwt(){
        return Mockito.mock(JwtUtil.class);
    }
    @Bean
    public UtilPagination utilPagination() { return Mockito.mock(UtilPagination.class); }

    @Bean
    public UserDetailsServiceImpl userService(){
        return Mockito.mock(UserDetailsServiceImpl.class);
    }
    @Bean
    public UserRepository userRepository() { return Mockito.mock(UserRepository.class);}

    @Bean
    public NewsRepository newsRepository() { return Mockito.mock(NewsRepository.class);}
    @Bean
    public INewsService newsService() { return Mockito.mock(INewsService.class);}

    @Bean
    public CommentRepository commentRepository() { return Mockito.mock(CommentRepository.class);}
    @Bean
    public ICommentService commentService() { return Mockito.mock(ICommentService.class);}

}
