package net.dnjo.budgeter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dnjo.budgeter.dtos.category.CategoryResponse;
import net.dnjo.budgeter.dtos.category.CreateCategoryRequest;
import net.dnjo.budgeter.dtos.category.UpdateCategoryRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void createCategory() throws Exception {
        var categoryResponse = new CategoryResponse(1L, "Shopping");
        when(categoryService.createCategory(any())).thenReturn(categoryResponse);

        var request = new CreateCategoryRequest("Shopping");

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Shopping"));
    }

    @Test
    public void getCategoryById() throws Exception {
        var categoryResponse = new CategoryResponse(1L, "Shopping");
        when(categoryService.findCategoryById(1L)).thenReturn(Optional.of(categoryResponse));

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Shopping"));
    }

    @Test
    public void getCategoryById_categoryNotFound_returns404() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllCategories() throws Exception {
        CategoryResponse categoryResponse1 = new CategoryResponse(1L, "Shopping");
        CategoryResponse categoryResponse2 = new CategoryResponse(2L, "Transportation");

        when(categoryService.findAllCategories()).thenReturn(List.of(categoryResponse1, categoryResponse2));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Shopping"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Transportation"));
    }

    @Test
    public void updateCategory() throws Exception {
        var categoryResponse = new CategoryResponse(1L, "Transportation");
        when(categoryService.updateCategory(eq(1L), any())).thenReturn(categoryResponse);

        var request = new UpdateCategoryRequest("Transportation");

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Transportation"));
    }

    @Test
    public void updateCategory_categoryNotFound_returns404() throws Exception {
        when(categoryService.updateCategory(eq(1L), any())).thenThrow(EntityNotFoundException.class);

        var request = new UpdateCategoryRequest("Transportation");

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCategory() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCategory_categoryNotFound_returns404() throws Exception {
        doThrow(EntityNotFoundException.class).when(categoryService).deleteCategoryById(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNotFound());
    }

}