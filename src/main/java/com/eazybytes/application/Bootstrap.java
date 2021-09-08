package com.eazybytes.application;

import com.eazybytes.model.*;
import com.eazybytes.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class Bootstrap implements CommandLineRunner {

    private CardsRepository cardsRepository;
    private UsersRepository usersRepository;
    private AuthoritiesRepository authoritiesRepository;
    private CustomerRepository customerRepository;
    private AccountsRepository accountsRepository;
    private NoticeRepository noticeRepository;
    private AccountTransactionsRepository accountTransactionsRepository;
    private LoanRepository loanRepository;

    public Bootstrap(CardsRepository cardsRepository, UsersRepository usersRepository, AuthoritiesRepository authoritiesRepository,
                     CustomerRepository customerRepository, AccountsRepository accountsRepository, NoticeRepository noticeRepository,
                     AccountTransactionsRepository accountTransactionsRepository,
                     LoanRepository loanRepository) {
        this.cardsRepository = cardsRepository;
        this.usersRepository = usersRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.customerRepository = customerRepository;
        this.accountsRepository = accountsRepository;
        this.noticeRepository = noticeRepository;
        this.accountTransactionsRepository = accountTransactionsRepository;
        this.loanRepository = loanRepository;
    }


    @Override
    public void run(String... args) {
        Notice notice = new Notice();
        notice.setNoticeSummary("Home Loan Interest rates reduced");
        notice.setNoticeDetails("Home loan interest rates are reduced as per the goverment guidelines. The updated rates will be effective immediately");
        notice.setNoticBegDt(new java.sql.Date(System.currentTimeMillis()));
        noticeRepository.save(notice);

        Authority authorityAdmin = new Authority();
        authorityAdmin.setName("ROLE_ADMIN");
        Authority authorityUser = new Authority();
        authorityUser.setName("ROLE_USER");
        Authority authorityRoot = new Authority();
        authorityRoot.setName("ROLE_ROOT");
        authoritiesRepository.save(authorityAdmin);
        authoritiesRepository.save(authorityUser);
        authoritiesRepository.save(authorityRoot);

        Customer customer = new Customer();
        customer.setName("Happy");
        customer.setEmail("jhabitey@gmail.com");
        customer.setMobileNumber("9876548337");
        customer.setPwd("12345");
        customer.setRole("admin");

        customer.getAuthorities().add(authorityAdmin);
        customer.getAuthorities().add(authorityUser);
//        customer.getAuthorities().add(authorityRoot);
        customer = customerRepository.save(customer);

        Loans loan = createLoan(customer.getId(), "Home", 100);
        loanRepository.save(loan);


        Accounts accounts = createAccount(186576453434L, "Savings", "123 Main Street, New York", customer.getId());
        accountsRepository.save(accounts);

        AccountTransactions accountTransactions = createAccountTransaction(186576453434L, customer.getId(), 30, "dea2112c-762b-46a6-8ade-68572ba27b98", "Coffee Shop", "Withdrawal");
        AccountTransactions accountTransactions2 = createAccountTransaction(186576453434L, customer.getId(), 50, "rea2112c-762b-46a6-8ade-68572ba27b98", "Some placce", "Withdrawal");
        accountTransactionsRepository.save(accountTransactions);
        accountTransactionsRepository.save(accountTransactions2);



        System.out.println("Inserting........");
        Cards cards = new Cards();
        cards.setAmountUsed(1000);
        cards.setCardNumber("happy@example.com");
        cards.setCustomerId(customer.getId());
        cardsRepository.save(cards);
    }

    private Loans createLoan(int customerId, String type, int totalLoan){
        Loans loans = new Loans();
        loans.setCustomerId(customerId);
        loans.setLoanType(type);
        loans.setTotalLoan(totalLoan);
        return loans;

    }

    private AccountTransactions createAccountTransaction(long accountNumber, int customerId, int closingBalance, String uuid, String transactionSummary, String transactionType) {
        AccountTransactions accountTransactions = new AccountTransactions();
        accountTransactions.setAccountNumber(accountNumber);
        accountTransactions.setCustomerId(customerId);
        accountTransactions.setClosingBalance(closingBalance);
        accountTransactions.setTransactionId(uuid);
        accountTransactions.setTransactionSummary(transactionSummary);
        accountTransactions.setTransactionType(transactionType);
        accountTransactions.setTransactionAmt(20);
        return accountTransactions;
    }

    private Accounts createAccount(long accountNumber, String type, String branchAddress, int customerId) {
        Accounts accounts = new Accounts();
        accounts.setAccountNumber(accountNumber);
        accounts.setAccountType(type);
        accounts.setBranchAddress(branchAddress);
        accounts.setCustomerId(customerId);
        return accounts;
    }
}
