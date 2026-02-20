package com.erp.backend.controller;

import com.erp.backend.dto.ExpenseRequestDTO;
import com.erp.backend.dto.ExpenseResponseDTO;
import com.erp.backend.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // ✅ Create Expense (ADMIN only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @Valid @RequestBody ExpenseRequestDTO requestDTO) {

        return ResponseEntity.ok(expenseService.createExpense(requestDTO));
    }

    // ✅ Get All Expenses (ADMIN + STAFF)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    // ✅ Get Expense By ID (ADMIN + STAFF)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    // ✅ Delete Expense (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {

        expenseService.deleteExpense(id);

        return ResponseEntity.ok("Expense deleted successfully.");
    }
    @PostMapping("/{id}/receipt")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadReceipt(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        expenseService.uploadReceipt(id, file);
        return ResponseEntity.ok("Receipt uploaded successfully.");
    }
}