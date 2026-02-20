package com.erp.backend.service;

import com.erp.backend.dto.ExpenseRequestDTO;
import com.erp.backend.dto.ExpenseResponseDTO;
import com.erp.backend.entity.Expense;
import com.erp.backend.exception.ResourceNotFoundException;
import com.erp.backend.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // ✅ Create Expense
    @Transactional
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO) {

        Expense expense = new Expense();
        expense.setDescription(requestDTO.getDescription());
        expense.setAmount(requestDTO.getAmount());
        expense.setCategory(requestDTO.getCategory());
        expense.setExpenseDate(requestDTO.getExpenseDate());

        Expense savedExpense = expenseRepository.save(expense);

        return mapToResponse(savedExpense);
    }

    // ✅ Get All Expenses
    public List<ExpenseResponseDTO> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get Expense By ID
    public ExpenseResponseDTO getExpenseById(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        return mapToResponse(expense);
    }

    // ✅ Delete Expense
    @Transactional
    public void deleteExpense(Long id) {

        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found");
        }

        expenseRepository.deleteById(id);
    }

    // 🔹 Mapping Method
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
    @Transactional
    public void uploadReceipt(Long id, MultipartFile file) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        try {
            String uploadDir = "uploads/receipts/";
            String originalFileName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalFileName;

            Path filePath = Paths.get(uploadDir, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            expense.setReceiptPath(filePath.toString());
            expenseRepository.save(expense);

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }
}