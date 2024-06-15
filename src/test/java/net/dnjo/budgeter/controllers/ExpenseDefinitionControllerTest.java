package net.dnjo.budgeter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dnjo.budgeter.dtos.expensedefinition.CreateExpenseDefinitionRequest;
import net.dnjo.budgeter.dtos.expensedefinition.ExpenseDefinitionResponse;
import net.dnjo.budgeter.dtos.expensedefinition.UpdateExpenseDefinitionRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.services.ExpenseDefinitionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseDefinitionController.class)
class ExpenseDefinitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseDefinitionService expenseDefinitionService;

    @Test
    public void createExpenseDefinition() throws Exception {
        var expenseDefinition = new ExpenseDefinitionResponse(
                1L,
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        when(expenseDefinitionService.createExpenseDefinition(any())).thenReturn(expenseDefinition);

        var request = new CreateExpenseDefinitionRequest(
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);

        mockMvc.perform(post("/expense-definitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.name").value("Rent"));
    }

    @Test
    public void getExpenseDefinitionById() throws Exception {
        var expenseDefinitionResponse = new ExpenseDefinitionResponse(
                1L,
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        when(expenseDefinitionService.findExpenseDefinitionById(1L)).thenReturn(Optional.of(expenseDefinitionResponse));

        mockMvc.perform(get("/expense-definitions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.name").value("Rent"));
    }

    @Test
    public void getExpenseDefinitionById_definitionNotFound_returns404() throws Exception {
        when(expenseDefinitionService.findExpenseDefinitionById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/expense-definitions/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllExpenseDefinitions() throws Exception {
        var expenseDefinitionResponse1 = new ExpenseDefinitionResponse(
                1L,
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        var expenseDefinitionResponse2 = new ExpenseDefinitionResponse(
                2L,
                1L,
                BigDecimal.valueOf(100),
                "Electricity",
                null);

        when(expenseDefinitionService.findAllExpenseDefinitions()).thenReturn(List.of(
                expenseDefinitionResponse1,
                expenseDefinitionResponse2));

        mockMvc.perform(get("/expense-definitions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].categoryId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Rent"))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].categoryId").value(1L))
                .andExpect(jsonPath("$[1].name").value("Electricity"))
                .andExpect(jsonPath("$[1].amount").value(100));
    }

    @Test
    public void updateExpenseDefinition() throws Exception {
        var expenseDefinitionResponse = new ExpenseDefinitionResponse(
                1L,
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        when(expenseDefinitionService.updateExpenseDefinition(eq(1L), any())).thenReturn(expenseDefinitionResponse);

        UpdateExpenseDefinitionRequest request = new UpdateExpenseDefinitionRequest(
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);

        mockMvc.perform(put("/expense-definitions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.name").value("Rent"));
    }

    @Test
    public void updateExpenseDefinition_definitionNotFound_returns404() throws Exception {
        when(expenseDefinitionService.updateExpenseDefinition(eq(1L), any()))
                .thenThrow(EntityNotFoundException.class);

        UpdateExpenseDefinitionRequest request = new UpdateExpenseDefinitionRequest(
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);

        mockMvc.perform(put("/expense-definitions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteExpenseDefinition() throws Exception {
        mockMvc.perform(delete("/expense-definitions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteExpenseDefinition_definitionNotFound_returns404() throws Exception {
        doThrow(EntityNotFoundException.class).when(expenseDefinitionService).deleteExpenseDefinitionById(1L);

        mockMvc.perform(delete("/expense-definitions/1"))
                .andExpect(status().isNotFound());
    }
}