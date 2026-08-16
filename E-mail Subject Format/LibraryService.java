package com.library.service;

import com.library.dto.FineResultDTO;
import com.library.dto.LibraryItemDTO;
import com.library.dto.MemberDTO;
import com.library.exception.*;
import com.library.model.*;
import com.library.repository.LibraryItemRepository;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Library Service encapsulates all business logic for the Bayt Al Hekma Library.
 * It handles circulation and fines operations as described in the program specification.
 */
@Service
@Transactional
public class LibraryService {
    @Autowired
    private LibraryItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final String LIBRARY_NAME = "Bayt Al Hekma";
    private static final double ADMINISTRATIVE_CHARGE = 10.0;

    // ==================== Catalogue Operations ====================

    /**
     * Registers a new library item.
     * Refuses if the catalogue ID is already taken.
     *
     * @param item the LibraryItem to register
     * @return the registered item
     * @throws InvalidOperationException if the ID is duplicate
     */
    @Transactional
    public LibraryItem registerItem(LibraryItem item) {
        if (itemRepository.findByCatalogueId(item.getCatalogueId()).isPresent()) {
            throw new InvalidOperationException(
                    "Item with catalogue ID '" + item.getCatalogueId() + "' already exists");
        }
        return itemRepository.save(item);
    }

    /**
     * Retrieves an item by its catalogue ID.
     * Returns empty optional if not found (never crashes).
     */
    public LibraryItem findItemByCatalogueId(String catalogueId) {
        return itemRepository.findByCatalogueId(catalogueId)
                .orElseThrow(() -> new ItemNotFoundException(catalogueId));
    }

    /**
     * Returns all items in the catalogue.
     */
    public List<LibraryItemDTO> viewCatalogue() {
        return itemRepository.findAll().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns all items filtered by status.
     */
    public List<LibraryItemDTO> viewItemsByStatus(ItemStatus status) {
        return itemRepository.findByStatus(status).stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());
    }

    // ==================== Member Operations ====================

    /**
     * Registers a new library member.
     * Refuses if the membership ID is already taken.
     *
     * @param member the Member to register
     * @return the registered member
     * @throws InvalidOperationException if the ID is duplicate
     */
    @Transactional
    public Member registerMember(Member member) {
        if (memberRepository.findByMembershipId(member.getMembershipId()).isPresent()) {
            throw new InvalidOperationException(
                    "Member with ID '" + member.getMembershipId() + "' already exists");
        }
        return memberRepository.save(member);
    }

    /**
     * Retrieves a member by their membership ID.
     */
    public Member findMemberByMembershipId(String membershipId) {
        return memberRepository.findByMembershipId(membershipId)
                .orElseThrow(() -> new MemberNotFoundException(membershipId));
    }

    /**
     * Returns all members with their details.
     */
    public List<MemberDTO> viewAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::convertMemberToDTO)
                .collect(Collectors.toList());
    }

    // ==================== Lending Operations ====================

    /**
     * Lends an item to a member.
     * Refuses if:
     * - Either item or member is missing
     * - Item is not available
     * - Member is not eligible to borrow
     *
     * @param catalogueId the ID of the item to borrow
     * @param membershipId the ID of the member borrowing
     * @return the loan period in days
     * @throws ItemNotFoundException if item doesn't exist
     * @throws MemberNotFoundException if member doesn't exist
     * @throws InvalidOperationException if the operation cannot be completed
     */
    @Transactional
    public int borrowItem(String catalogueId, String membershipId) {
        LibraryItem item = findItemByCatalogueId(catalogueId);
        Member member = findMemberByMembershipId(membershipId);

        if (item.getStatus() != ItemStatus.AVAILABLE) {
            throw new InvalidOperationException(
                    "Item '" + catalogueId + "' is not available (status: " + item.getStatus() + ")");
        }

        if (!member.isEligibleToBorrow()) {
            if (member.getItemsHeld() >= 3) {
                throw new InvalidOperationException(
                        "Member " + membershipId + " already holds 3 items (maximum)");
            } else {
                throw new InvalidOperationException(
                        "Member " + membershipId + " owes " + member.getBalanceOwed() + 
                        " EGP (exceeds 100 EGP limit)");
            }
        }

        item.lendTo(member.getName());
        member.recordBorrowing();

        itemRepository.save(item);
        memberRepository.save(member);

        return item.getLoanPeriodDays();
    }

    // ==================== Return and Fine Operations ====================

    /**
     * Processes the return of an item.
     * Calculates fine, applies waiver, adds administrative charge, and charges member.
     * 
     * Rule enforcement:
     * - Item must be on loan
     * - Days overdue must not be negative
     * - Member must be identified before item is freed
     * - Waiver applies to base fine only, never to administrative charge
     *
     * @param catalogueId the ID of the item being returned
     * @param daysOverdue the number of days overdue (0 means on time)
     * @return FineResultDTO with the breakdown
     * @throws ItemNotFoundException if item doesn't exist
     * @throws InvalidOperationException if operation cannot be completed
     */
    @Transactional
    public FineResultDTO processReturn(String catalogueId, int daysOverdue) {
        if (daysOverdue < 0) {
            throw new InvalidOperationException("Days overdue cannot be negative");
        }

        LibraryItem item = findItemByCatalogueId(catalogueId);

        if (item.getStatus() != ItemStatus.ON_LOAN) {
            throw new InvalidOperationException(
                    "Item '" + catalogueId + "' is not currently on loan");
        }

        // CRITICAL: Identify member before freeing the item
        String membershipId = item.getBorrowerName();
        Member member = findMemberByMembershipId(membershipId);

        // Calculate fine in fixed order
        double baseFine = daysOverdue == 0 ? 0.0 : item.calculateOverdueFine(daysOverdue);
        double waiverAmount = member.getCategory().calculateWaiver(baseFine);
        double fineAfterWaiver = baseFine - waiverAmount;
        
        // Administrative charge is never waived and not charged if on time
        double administrativeCharge = daysOverdue == 0 ? 0.0 : ADMINISTRATIVE_CHARGE;
        double totalCharged = fineAfterWaiver + administrativeCharge;

        // Charge the member
        if (!member.chargeFine(totalCharged)) {
            throw new InvalidOperationException("Failed to charge fine to member");
        }

        // Record the return
        if (!member.recordReturn()) {
            throw new InvalidOperationException("Failed to record return");
        }

        // Free the item (must be after member is identified)
        item.takeBack();

        // Persist changes
        memberRepository.save(member);
        itemRepository.save(item);

        return new FineResultDTO(
                catalogueId, membershipId, daysOverdue,
                baseFine, waiverAmount, administrativeCharge,
                totalCharged, member.getBalanceOwed()
        );
    }

    // ==================== Renewal Operations ====================

    /**
     * Attempts to renew a loan.
     * Checks whether the item type supports renewal using the Renewable interface.
     * This is the only place in the program where an object's type is examined at runtime,
     * and it asks "can this object be renewed?" not "is this a book or magazine?"
     *
     * @param catalogueId the ID of the item to renew
     * @return the number of renewals remaining, or -1 if renewal not possible
     * @throws ItemNotFoundException if item doesn't exist
     * @throws InvalidOperationException if renewal cannot be completed
     */
    @Transactional
    public int renewLoan(String catalogueId) {
        LibraryItem item = findItemByCatalogueId(catalogueId);

        // Check if this item type supports renewal
        if (!(item instanceof Renewable)) {
            throw new InvalidOperationException(
                    "Items of type '" + item.getCategory() + "' cannot be renewed");
        }

        Renewable renewableItem = (Renewable) item;

        if (item.getStatus() != ItemStatus.ON_LOAN) {
            throw new InvalidOperationException(
                    "Item '" + catalogueId + "' is not currently on loan");
        }

        if (!renewableItem.renewLoan()) {
            throw new InvalidOperationException(
                    "Item '" + catalogueId + "' has reached its renewal limit");
        }

        itemRepository.save(item);
        return renewableItem.getRenewalsRemaining();
    }

    // ==================== Item State Operations ====================

    /**
     * Marks an item as reserved.
     */
    @Transactional
    public void reserveItem(String catalogueId) {
        LibraryItem item = findItemByCatalogueId(catalogueId);
        if (!item.markReserved()) {
            throw new InvalidOperationException(
                    "Item '" + catalogueId + "' cannot be reserved (not available)");
        }
        itemRepository.save(item);
    }

    /**
     * Marks an item as lost.
     */
    @Transactional
    public void markItemLost(String catalogueId) {
        LibraryItem item = findItemByCatalogueId(catalogueId);
        item.markLost();
        itemRepository.save(item);
    }

    /**
     * Brings a reserved or lost item back to available status.
     */
    @Transactional
    public void bringItemBack(String catalogueId) {
        LibraryItem item = findItemByCatalogueId(catalogueId);
        item.bringBack();
        itemRepository.save(item);
    }

    // ==================== Fine Payment Operations ====================

    /**
     * Processes a payment from a member against their outstanding balance.
     *
     * @param membershipId the ID of the member
     * @param amount the payment amount
     * @return the new balance owed
     * @throws MemberNotFoundException if member doesn't exist
     * @throws InvalidOperationException if payment is invalid
     */
    @Transactional
    public double payOutstandingFines(String membershipId, double amount) {
        Member member = findMemberByMembershipId(membershipId);

        if (!member.acceptPayment(amount)) {
            if (amount <= 0) {
                throw new InvalidOperationException("Payment amount must be positive");
            } else {
                throw new InvalidOperationException(
                        "Payment amount exceeds balance owed (" + member.getBalanceOwed() + " EGP)");
            }
        }

        memberRepository.save(member);
        return member.getBalanceOwed();
    }

    // ==================== Reporting Operations ====================

    /**
     * Returns library statistics and calculations.
     */
    public LibraryReportDTO generateReport(int projectedOverduesDays) {
        long totalItems = itemRepository.count();
        long itemsOnLoan = itemRepository.countByStatus(ItemStatus.ON_LOAN);
        double loanRate = totalItems > 0 ? (itemsOnLoan * 100.0) / totalItems : 0.0;

        double totalOutstanding = memberRepository.findAll().stream()
                .mapToDouble(Member::getBalanceOwed)
                .sum();

        double projectedFines = itemRepository.findByStatus(ItemStatus.ON_LOAN).stream()
                .mapToDouble(item -> item.calculateOverdueFine(projectedOverduesDays))
                .sum();

        return new LibraryReportDTO(
                (int) totalItems,
                (int) itemsOnLoan,
                loanRate,
                totalOutstanding,
                projectedFines
        );
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
}
