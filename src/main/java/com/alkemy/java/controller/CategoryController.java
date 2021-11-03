package com.alkemy.java.controller;

import com.alkemy.java.dto.CategoryListRespDto;
import com.alkemy.java.dto.CategoryRequestDto;
import com.alkemy.java.dto.CategoryResponseDto;
import com.alkemy.java.dto.PageDto;
import com.alkemy.java.exception.InvalidDataException;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.service.ICategoryService;
import com.alkemy.java.util.UtilPagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Api(value = "Categories controller")
@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    @Autowired
    private UtilPagination utilPagination;

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private MessageSource messageSource;

    @Value("success.deleted")
    private String messageDeleted;

    @Value("error.pagination")
    private String paginationError;

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 400, message = "Unauthorized Access"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Create a new category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody(required = true) CategoryRequestDto categoryRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidDataException(bindingResult);

        return new ResponseEntity<>(iCategoryService.createCategory(categoryRequest), HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 400, message = "Unauthorized Access"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Get All categories")
    @GetMapping("/")
    public ResponseEntity<List<CategoryListRespDto>> getAllCategories() {
        List<CategoryListRespDto> categories = iCategoryService.findAllCategories()
                .stream()
                .map(CategoryListRespDto::new)
                .collect(Collectors.toList());
        return categories.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(categories) : ResponseEntity.ok(categories);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 400, message = "Unauthorized Access"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Get all pageable categories")
    @GetMapping
    ResponseEntity<?> getCategoriesPageable(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable,
                                            @RequestParam(value = "page", defaultValue = "0") int page, HttpServletRequest request) {

        PageDto<CategoryListRespDto> r = iCategoryService.getPageableCategory(pageable, request);
        return ResponseEntity.status(HttpStatus.OK).body(r);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 400, message = "Unauthorized Access"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Update category by ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryResponseDto categoryResponseDto, @PathVariable(name = "id") long id) {
        try {
            return new ResponseEntity<>(iCategoryService.updateCategory(categoryResponseDto, id), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 400, message = "Unauthorized Access"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Get category by ID")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long categoryId) throws NotFoundException {

        return ResponseEntity.ok(iCategoryService.getCategoryById(categoryId));
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 400, message = "Unauthorized Access"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiOperation("Delete category by ID")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        iCategoryService.deleteCategory(id);
        return new ResponseEntity<>(messageSource.getMessage(messageDeleted, null, Locale.getDefault()), HttpStatus.OK);
    }
}