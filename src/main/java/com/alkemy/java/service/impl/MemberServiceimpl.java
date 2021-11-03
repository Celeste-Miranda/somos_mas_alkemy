package com.alkemy.java.service.impl;

import com.alkemy.java.dto.MemberDto;
import com.alkemy.java.dto.MemberRequestDto;
import com.alkemy.java.dto.MemberResponseDto;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.Member;
import com.alkemy.java.model.Testimonial;
import com.alkemy.java.repository.MemberRepository;
import com.alkemy.java.service.IMemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class MemberServiceimpl implements IMemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    @Value("error.member.name.repeated")
    private String errorPath;

    @Value("error.member.idNotFound")
    private String idNotFound;

    @Value("error.member.id.not.found")
    private String idNotFoundMessage;


    @Override
    public List<MemberResponseDto> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(member -> modelMapper.map(member, MemberResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public MemberResponseDto createMember(MemberRequestDto request) throws Exception {

        if (memberRepository.findByName(request.getName()) != null)
            throw new RuntimeException(messageSource.getMessage(errorPath, null, Locale.getDefault()));

        Member updatedMember =
                MemberRequestDto.dtoToMember(request);

        Member member = memberRepository.save(updatedMember);

        return MemberResponseDto.memberToDto(member);
    }

    @Override
    public MemberDto updateMember(MemberDto memberDto, Long id) throws Exception {
        Member memberToUpdate = memberRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException(messageSource.getMessage(idNotFound,null,Locale.getDefault())));
        memberToUpdate.setDescription(memberDto.getDescription());
        memberToUpdate.setFacebookUrl(memberDto.getFacebookUrl());
        memberToUpdate.setImage(memberDto.getImage());
        memberToUpdate.setInstagramUrl(memberDto.getInstagramUrl());
        memberToUpdate.setName(memberDto.getName());
        memberToUpdate.setLinkedinUrl(memberDto.getLinkedinUrl());
        memberToUpdate.setUpdateDate(new Date());
        return modelMapper.map(memberRepository.save(memberToUpdate),MemberDto.class);
    }

    public void deleteById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(messageSource.getMessage
                        (idNotFoundMessage, null, Locale.getDefault())));
        memberRepository.delete(member);
    }

    @Override
    public Page<MemberDto> getAllMembersPageable(Pageable page) {
        return memberRepository.findAll(page).map(memberPage -> modelMapper.map(memberPage, MemberDto.class));
    }
}
