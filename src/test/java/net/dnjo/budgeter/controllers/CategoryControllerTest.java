package net.dnjo.budgeter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dnjo.budgeter.dtos.CategoryResponse;
import net.dnjo.budgeter.dtos.CreateCategoryRequest;
import net.dnjo.budgeter.dtos.UpdateCategoryRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
    public void testCreateCategory() throws Exception {
        var categoryResponse = new CategoryResponse(1L, "Shopping");
        when(categoryService.createCategory(any())).thenReturn(categoryResponse);

        var request = new CreateCategoryRequest("Shopping");

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Shopping"));
    }

    @Test
    public void testGetCategoryById() throws Exception {
        var categoryResponse = new CategoryResponse(1L, "Shopping");
        when(categoryService.findCategoryById(1L)).thenReturn(Optional.of(categoryResponse));

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Shopping"));
    }

    @Test
    public void testGetCategoryById_categoryNotFound_returns404() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateCategory() throws Exception {
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
    public void testUpdateCategory_categoryNotFound_returns404() throws Exception {
        when(categoryService.updateCategory(eq(1L), any())).thenThrow(EntityNotFoundException.class);

        var request = new UpdateCategoryRequest("Transportation");

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCategory_categoryNotFound_returns404() throws Exception {
        doThrow(EntityNotFoundException.class).when(categoryService).deleteCategoryById(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNotFound());
    }

}