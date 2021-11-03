package com.alkemy.java.service;

import com.alkemy.java.dto.MemberDto;
import com.alkemy.java.dto.MemberRequestDto;
import com.alkemy.java.dto.MemberResponseDto;
import com.alkemy.java.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMemberService {

    List<MemberResponseDto> getAllMembers();

    MemberResponseDto createMember(MemberRequestDto org) throws Exception;

    void deleteById(Long id);


    MemberDto updateMember(MemberDto memberDto, Long id) throws Exception;

    Page<MemberDto> getAllMembersPageable(Pageable page);
}
