package com.alkemy.java.service;

import com.alkemy.java.dto.NewsRequestDto;
import com.alkemy.java.dto.NewsResponseDto;

import com.alkemy.java.dto.NewsDto;
import com.alkemy.java.exception.ResourceNotFoundException;

public interface INewsService {

    NewsResponseDto createNews(NewsRequestDto news);

    void deleteNews(Long id);

    NewsDto updateNews(Long id, NewsDto newsDto);

    NewsResponseDto findNewsById(Long id) throws ResourceNotFoundException;
}
