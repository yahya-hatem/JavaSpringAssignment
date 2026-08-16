package com.library.controller;

import com.library.dto.FineResultDTO;
import com.library.dto.LibraryItemDTO;
import com.library.dto.LibraryReportDTO;
import com.library.dto.MemberDTO;
import com.library.model.*;
import com.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller for the Bayt Al Hekma Library Management System.
 * Exposes library operations via HTTP endpoints.
 */
@RestController
@RequestMapping("/api/library")
@CrossOrigin(origins = "*")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    // ==================== Catalogue Endpoints ====================

    /**
     * GET /api/library/catalogue
     * View all items in the catalogue
     */
    @GetMapping("/catalogue")
    public ResponseEntity<List<LibraryItemDTO>> viewCatalogue() {
        return ResponseEntity.ok(libraryService.viewCatalogue());
    }

    /**
     * GET /api/library/items/status/{status}
     * View items filtered by status
     */
    @GetMapping("/items/status/{status}")
    public ResponseEntity<List<LibraryItemDTO>> viewItemsByStatus(@PathVariable ItemStatus status) {
        return ResponseEntity.ok(libraryService.viewItemsByStatus(status));
    }

    /**
     * GET /api/library/items/{catalogueId}
     * Search for a specific item by catalogue ID
     */
    @GetMapping("/items/{catalogueId}")
    public ResponseEntity<LibraryItemDTO> searchItem(@PathVariable String catalogueId) {
        LibraryItem item = libraryService.findItemByCatalogueId(catalogueId);
        return ResponseEntity.ok(convertItemToDTO(item));
    }

    /**
     * POST /api/library/items/books
     * Register a new book
     */
    @PostMapping("/items/books")
    public ResponseEntity<?> registerBook(@RequestBody BookRegistrationRequest request) {
        Book book = new Book(
                request.catalogueId, request.title, request.author, request.pageCount,
                "Bayt Al Hekma", 10.0, 0
        );
        Book registered = (Book) libraryService.registerItem(book);
        return ResponseEntity.ok(createSuccessResponse("Book registered successfully", registered));
    }

    /**
     * POST /api/library/items/magazines
     * Register a new magazine
     */
    @PostMapping("/items/magazines")
    public ResponseEntity<?> registerMagazine(@RequestBody MagazineRegistrationRequest request) {
        Magazine magazine = new Magazine(
                request.catalogueId, request.title, request.issueNumber,
                "Bayt Al Hekma", 10.0, 0
        );
        Magazine registered = (Magazine) libraryService.registerItem(magazine);
        return ResponseEntity.ok(createSuccessResponse("Magazine registered successfully", registered));
    }

    /**
     * POST /api/library/items/dvds
     * Register a new DVD
     */
    @PostMapping("/items/dvds")
    public ResponseEntity<?> registerDVD(@RequestBody DVDRegistrationRequest request) {
        DVD dvd = new DVD(
                request.catalogueId, request.title, request.runtime,
                "Bayt Al Hekma", 10.0, 0
        );
        DVD registered = (DVD) libraryService.registerItem(dvd);
        return ResponseEntity.ok(createSuccessResponse("DVD registered successfully", registered));
    }

    // ==================== Member Endpoints ====================

    /**
     * GET /api/library/members
     * View all members
     */
    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> viewAllMembers() {
        return ResponseEntity.ok(libraryService.viewAllMembers());
    }

    /**
     * GET /api/library/members/{membershipId}
     * Search for a specific member
     */
    @GetMapping("/members/{membershipId}")
    public ResponseEntity<MemberDTO> searchMember(@PathVariable String membershipId) {
        Member member = libraryService.findMemberByMembershipId(membershipId);
        return ResponseEntity.ok(convertMemberToDTO(member));
    }

    /**
     * POST /api/library/members
     * Register a new member
     */
    @PostMapping("/members")
    public ResponseEntity<?> registerMember(@RequestBody MemberRegistrationRequest request) {
        Member member = new Member(request.membershipId, request.name, request.category);
        Member registered = libraryService.registerMember(member);
        return ResponseEntity.ok(createSuccessResponse("Member registered successfully", registered));
    }

    // ==================== Circulation Endpoints ====================

    /**
     * POST /api/library/borrow
     * Borrow an item
     */
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowItem(@RequestBody BorrowRequest request) {
        int loanPeriod = libraryService.borrowItem(request.catalogueId, request.membershipId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Item borrowed successfully");
        response.put("loanPeriodDays", loanPeriod);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/library/return
     * Return an item and process fines
     */
    @PostMapping("/return")
    public ResponseEntity<?> processReturn(@RequestBody ReturnRequest request) {
        FineResultDTO result = libraryService.processReturn(request.catalogueId, request.daysOverdue);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Item returned successfully");
        response.put("fineDetails", result);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/library/renew
     * Renew a loan
     */
    @PostMapping("/renew")
    public ResponseEntity<?> renewLoan(@RequestBody RenewRequest request) {
        int renewalsRemaining = libraryService.renewLoan(request.catalogueId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Loan renewed successfully");
        response.put("renewalsRemaining", renewalsRemaining);
        return ResponseEntity.ok(response);
    }

    // ==================== Item State Endpoints ====================

    /**
     * PUT /api/library/items/{catalogueId}/reserve
     * Mark an item as reserved
     */
    @PutMapping("/items/{catalogueId}/reserve")
    public ResponseEntity<?> reserveItem(@PathVariable String catalogueId) {
        libraryService.reserveItem(catalogueId);
        return ResponseEntity.ok(createSuccessResponse("Item reserved successfully", null));
    }

    /**
     * PUT /api/library/items/{catalogueId}/lost
     * Mark an item as lost
     */
    @PutMapping("/items/{catalogueId}/lost")
    public ResponseEntity<?> markItemLost(@PathVariable String catalogueId) {
        libraryService.markItemLost(catalogueId);
        return ResponseEntity.ok(createSuccessResponse("Item marked as lost", null));
    }

    /**
     * PUT /api/library/items/{catalogueId}/restore
     * Restore a reserved or lost item
     */
    @PutMapping("/items/{catalogueId}/restore")
    public ResponseEntity<?> restoreItem(@PathVariable String catalogueId) {
        libraryService.bringItemBack(catalogueId);
        return ResponseEntity.ok(createSuccessResponse("Item restored to available", null));
    }

    // ==================== Fine Payment Endpoints ====================

    /**
     * POST /api/library/payments
     * Process a fine payment
     */
    @PostMapping("/payments")
    public ResponseEntity<?> payFines(@RequestBody PaymentRequest request) {
        double newBalance = libraryService.payOutstandingFines(request.membershipId, request.amount);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Payment processed successfully");
        response.put("newBalance", newBalance);
        return ResponseEntity.ok(response);
    }

    // ==================== Reporting Endpoints ====================

    /**
     * GET /api/library/report
     * Generate library statistics report
     */
    @GetMapping("/report")
    public ResponseEntity<LibraryReportDTO> generateReport(
            @RequestParam(defaultValue = "5") int projectedOverduesDays) {
        return ResponseEntity.ok(libraryService.generateReport(projectedOverduesDays));
    }

    // ==================== Helper Methods ====================

    private LibraryItemDTO convertItemToDTO(LibraryItem item) {
        return new LibraryItemDTO(
                item.getCatalogueId(),
                item.getCategory(),
                item.getTitle(),
                item.getStatus(),
                item.getBorrowerName() != null ? item.getBorrowerName() : "-",
                item.getLoanPeriodDays(),
                item.calculateOverdueFine(1)
        );
    }

    private MemberDTO convertMemberToDTO(Member member) {
        return new MemberDTO(
                member.getMembershipId(),
                member.getName(),
                member.getCategory(),
                member.getBalanceOwed(),
                member.getItemsHeld()
        );
    }

    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }

    // ==================== Request DTOs ====================

    public static class BookRegistrationRequest {
        public String catalogueId;
        public String title;
        public String author;
        public int pageCount;
    }

    public static class MagazineRegistrationRequest {
        public String catalogueId;
        public String title;
        public int issueNumber;
    }

    public static class DVDRegistrationRequest {
        public String catalogueId;
        public String title;
        public int runtime;
    }

    public static class MemberRegistrationRequest {
        public String membershipId;
        public String name;
        public MembershipType category;
    }

    public static class BorrowRequest {
        public String catalogueId;
        public String membershipId;
    }

    public static class ReturnRequest {
        public String catalogueId;
        public int daysOverdue;
    }

    public static class RenewRequest {
        public String catalogueId;
    }

    public static class PaymentRequest {
        public String membershipId;
        public double amount;
    }
}
