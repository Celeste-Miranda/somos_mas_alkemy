package com.alkemy.java.controller;

import com.alkemy.java.dto.MemberDto;
import com.alkemy.java.dto.MemberRequestDto;
import com.alkemy.java.dto.MemberResponseDto;
import com.alkemy.java.dto.PageDto;
import com.alkemy.java.service.IMemberService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.alkemy.java.util.UtilPagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import java.util.Map;


@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private IMemberService memberService;

    @Autowired
    private UtilPagination utilPagination;

    @Value("success.get")
    private String successGet;

    @Value("success.deleted")
    private String successfullyDeleted;

    @Value("error.pagination")
    private String paginationError;

    @Autowired
    private MessageSource messageSource;

    @GetMapping()
    @ApiOperation("Bring all the members.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllMembers(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable,
                                           @RequestParam(value = "page", defaultValue = "0") int page, HttpServletRequest request){

        Page<MemberDto> members = memberService.getAllMembersPageable(pageable);
        Map<String, String> links = utilPagination.linksPagination(request, members);

        if (page >= members.getTotalPages())
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage(paginationError, null, Locale.getDefault()));

        PageDto<MemberDto> response = new PageDto<>();
        response.setContent(members.getContent());
        response.setLinks(links);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Add a new Member.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> createMembers(@Valid @RequestBody MemberRequestDto request){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ApiOperation(value = "Update an existing Member.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> updateMember(@Valid @RequestBody MemberDto memberDto,@PathVariable(name = "id") Long id){
       try {
           return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMember(memberDto, id));
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete an existing Member.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 404, message = "Member not found."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> deleteMemberById(@PathVariable Long id) {
        try {
            memberService.deleteById(id);

            return new ResponseEntity<>(messageSource.getMessage
                    (successfullyDeleted, null, Locale.getDefault()),
                    HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
