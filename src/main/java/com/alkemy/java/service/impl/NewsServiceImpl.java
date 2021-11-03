
package com.alkemy.java.service.impl;

import com.alkemy.java.dto.NewsDto;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.News;
import com.alkemy.java.repository.NewsRepository;
import com.alkemy.java.service.INewsService;
import java.util.Date;
import java.util.Locale;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.alkemy.java.dto.NewsRequestDto;
import com.alkemy.java.dto.NewsResponseDto;
import com.alkemy.java.exception.BadRequestException;
import com.alkemy.java.exception.ConflictException;
import com.alkemy.java.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Service
public class NewsServiceImpl implements INewsService{

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ModelMapper modelMapper;

    @Value ("error.news.service.dont.exist")
     String messageDontExist;
   

    @Override
    public void deleteNews(Long id) {
        News news = findById(id);
        
        if (news == null) {
            throw new ResourceNotFoundException(messageSource.getMessage(messageDontExist, null, Locale.getDefault()));
        }
        
        newsRepository.deleteById(id);
    }

    @Override
    public NewsResponseDto createNews(NewsRequestDto newsDto) {

        if(newsRepository.findByName(newsDto.getName()) != null)
            throw new ConflictException(messageSource.getMessage("error.news.already.exist", null, Locale.getDefault()));

        if (!categoryRepository.existsById((long) newsDto.getCategoryId()))
            throw new BadRequestException(messageSource.getMessage("error.category.doesnt.exist", null, Locale.getDefault()));

        News newsToSave = NewsRequestDto.dtoToNews(newsDto);

        newsToSave.setCreateDate(new Date());
        newsToSave.setUpdateDate(new Date());
        newsToSave.setDeleted(false);

        newsToSave.setCategory(categoryRepository.findById((long) newsDto.getCategoryId()).get());

        News news = newsRepository.save(newsToSave);

        return NewsResponseDto.newsToDto(news);
    }

    @Override
    public NewsDto updateNews(Long id, NewsDto newsDto) {
        News news = newsRepository.findById(id).orElseThrow( () ->
                new ResourceNotFoundException(messageSource.getMessage(messageDontExist, null, Locale.getDefault())));

        if (newsDto.getCategory() != null)
            news.setCategory(newsDto.getCategory());
        if (newsDto.getContent() != null)
            news.setContent(newsDto.getContent());
        if (newsDto.getName() != null)
            news.setName(newsDto.getName());
        if (newsDto.getImage() != null)
            news.setImage(newsDto.getImage());
        news.setUpdateDate(new Date());

        news = newsRepository.save(news);
        return modelMapper.map(news, NewsDto.class);
    }

    private News findById(Long id){
       return newsRepository.findById(id).orElse(null);
    }

    @Override
    public NewsResponseDto findNewsById(Long id) throws ResourceNotFoundException {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(messageDontExist, null, Locale.getDefault())));
        return NewsResponseDto.newsToDto(news);
    }

    @Override
    public Page<NewsResponseDto> getNews(Pageable page) {
        
        return newsRepository.findAll(page).map(d -> modelMapper.map(d,NewsResponseDto.class));
        
    }
    
    

}
