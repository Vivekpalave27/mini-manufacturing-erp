package com.erp.backend.service;

import com.erp.backend.dto.ExpenseRequestDTO;
import com.erp.backend.dto.ExpenseResponseDTO;
import com.erp.backend.entity.Expense;
import com.erp.backend.exception.BusinessException;
import com.erp.backend.exception.ResourceNotFoundException;
import com.erp.backend.repository.ExpenseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // ==========================
    // CREATE EXPENSE
    // ==========================
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO) {

        Expense expense = new Expense();
        expense.setDescription(requestDTO.getDescription());
        expense.setAmount(requestDTO.getAmount());
        expense.setCategory(requestDTO.getCategory());
        expense.setExpenseDate(requestDTO.getExpenseDate());

        return mapToResponse(expenseRepository.save(expense));
    }

    // ==========================
    // GET ALL EXPENSES
    // ==========================
    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==========================
    // GET BY ID
    // ==========================
    @Transactional(readOnly = true)
    public ExpenseResponseDTO getExpenseById(Long id) {
        return mapToResponse(getExpenseEntity(id));
    }

    // ==========================
    // DELETE EXPENSE
    // ==========================
    public void deleteExpense(Long id) {
        expenseRepository.delete(getExpenseEntity(id));
    }

    // ==========================
    // UPLOAD RECEIPT
    // ==========================
    public void uploadReceipt(Long id, MultipartFile file) {

        Expense expense = getExpenseEntity(id);

        try {
            String uploadDir = "uploads/receipts/";
            String fileName = System.currentTimeMillis() +
                    "_" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            expense.setReceiptPath(filePath.toString());

        } catch (Exception e) {
            throw new BusinessException(
                    "File upload failed. Please try again.");
        }
    }

    // ==========================
    // PRIVATE HELPERS
    // ==========================
    private Expense getExpenseEntity(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Expense not found with id: " + id));
    }

    private ExpenseResponseDTO mapToResponse(Expense expense) {

        ExpenseResponseDTO response = new ExpenseResponseDTO();
        response.setId(expense.getId());
        response.setExpenseNumber(expense.getExpenseNumber());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setExpenseDate(expense.getExpenseDate());
        response.setReceiptPath(expense.getReceiptPath());
        response.setCreatedAt(expense.getCreatedAt());

        return response;
    }
}