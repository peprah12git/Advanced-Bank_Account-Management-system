# Advanced Bank Account Management System

A comprehensive Java-based banking application that demonstrates modern software engineering principles, object-oriented design patterns, and advanced data structures. This system manages customer accounts, transactions, and financial operations with full persistence and concurrent transaction support.

---

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Core Components](#core-components)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Usage Guide](#usage-guide)
- [Learning Objectives](#learning-objectives)
- [Testing](#testing)
- [Architecture](#architecture)
- [Exception Handling](#exception-handling)
- [File Persistence](#file-persistence)
- [Contributing](#contributing)

---

## Features

### Account Management
- **Multiple Account Types**: Savings and Checking accounts with different rules and interest rates
- **Account Creation**: Create new accounts with automatic account number generation
- **Account Viewing**: View all accounts with detailed information and balance summary
- **Account Status Tracking**: Monitor account status (Active/Inactive)

### Customer Management
- **Customer Types**: Support for Regular and Premium customers
- **Premium Benefits**: Special perks for premium account holders
- **Customer Profiles**: Store comprehensive customer information (name, age, contact, address)
- **Customer Queries**: Search and filter customers by various criteria

### Transaction Management
- **Deposit Operations**: Add funds to accounts with validation
- **Withdrawal Operations**: Remove funds with balance verification
- **Transfer Operations**: Move funds between accounts
- **Transaction History**: Complete transaction audit trail with timestamps
- **Transaction Filtering**: View transactions by account, type, or date range

### Advanced Features
- **Concurrent Transactions**: Simulate and handle multiple simultaneous transactions
- **Data Persistence**: Automatic save/load from file system (CSV format)
- **Account Statements**: Generate detailed account statements with transaction history
- **Balance Calculations**: Automatic balance tracking across all accounts
- **Interest Calculation**: Auto-calculate interest for savings accounts

### Business Rules Enforcement
- **Overdraft Protection**: Prevent withdrawals exceeding account balance
- **Overdraft Limits**: Control maximum overdraft amounts for checking accounts
- **Minimum Balances**: Enforce minimum balance requirements
- **Input Validation**: Comprehensive validation of all user inputs

---

## Project Structure

```
Advanced-Bank_Account-Management-system/
├── src/
│   ├── main/
│   │   └── Main.java                          # Application entry point
│   ├── models/
│   │   ├── Account.java                       # Abstract account class
│   │   ├── SavingsAccount.java                # Savings account implementation
│   │   ├── CheckingAccount.java               # Checking account implementation
│   │   ├── Customer.java                      # Abstract customer class
│   │   ├── RegularCustomer.java               # Regular customer type
│   │   ├── PremiumCustomer.java               # Premium customer type
│   │   └── Transaction.java                   # Transaction records
│   ├── services/
│   │   ├── AccountManager.java                # Account operations service
│   │   ├── CustomerManager.java               # Customer operations service
│   │   ├── TransactionManager.java            # Transaction operations service
│   │   └── FilePersistence/
│   │       ├── AccountFilePersistenceService.java
│   │       └── TransactionFilePersistence.java
│   ├── exceptions/
│   │   ├── AccountNotFoundException.java
│   │   ├── InsufficientFundsException.java
│   │   ├── InvalidAmountException.java
│   │   ├── InvalidInputException.java
│   │   ├── OverdraftLimitExceededException.java
│   │   └── ViewAccountException.java
│   ├── utils/
│   │   ├── ConsoleInputReader.java            # User input handling
│   │   ├── ConsoleTablePrinter.java           # Table formatting output
│   │   ├── ValidationUtils.java               # Input validation utilities
│   │   └── ConcurrencyUtils2.java             # Concurrent transaction simulation
│   ├── data/
│   │   ├── accounts.txt                       # Persistent account data
│   │   └── transactions.txt                   # Persistent transaction data
│   └── Test/
│       ├── accounts/
│       │   ├── AccountTest.java
│       │   ├── CheckingAccountTest.java
│       │   ├── SavingsAccountTest.java
│       │   └── AccountManagerTest.java
│       ├── customers/
│       │   ├── RegularCustomerTest.java
│       │   └── PremiumCustomerTest.java
│       └── Transactions/
│           ├── TransactionTest.java
│           └── TransactionManagerTest.java
├── resource-files/
│   ├── stylesheet.css
│   ├── jquery-ui.min.css
│   └── fonts/
├── legal/
│   └── LICENSE
└── README.md
```

---

## Core Components

### 1. **Account Models** (`src/models/`)
- **Account (Abstract)**: Base class for all account types
  - Properties: accountNumber, customer, balance, status
  - Methods: deposit(), withdraw(), getBalance()
  
- **SavingsAccount**: Low-risk account with interest
  - Interest Rate: Applied periodically
  - Withdrawal Limits: May apply
  
- **CheckingAccount**: Daily transaction account
  - Overdraft Limit: Allows limited negative balance
  - Transaction Fees: May apply per transaction

### 2. **Customer Models** (`src/models/`)
- **Customer (Abstract)**: Base customer class
  - Properties: customerId, name, age, contact, address
  
- **RegularCustomer**: Standard customer with basic account types
- **PremiumCustomer**: Enhanced customer with special privileges

### 3. **Service Layer** (`src/services/`)
- **AccountManager**: Handles all account operations
  - CRUD operations for accounts
  - Balance calculations using Streams API
  - Fast O(1) account lookups using HashMap
  
- **CustomerManager**: Manages customer information
  - Customer creation and updates
  - Customer search and filtering
  
- **TransactionManager**: Manages all transactions
  - Transaction recording and retrieval
  - Transaction filtering by account and type
  - Deposit/withdrawal processing
  
- **FilePersistence Services**: Handles data persistence
  - Account persistence to CSV
  - Transaction persistence to CSV
  - Automatic data loading on startup

### 4. **Exception Handling** (`src/exceptions/`)
Custom exceptions for precise error handling:
- `AccountNotFoundException`: Account doesn't exist
- `InsufficientFundsException`: Insufficient balance for withdrawal
- `InvalidAmountException`: Invalid transaction amount
- `InvalidInputException`: Invalid user input
- `OverdraftLimitExceededException`: Overdraft limit exceeded
- `ViewAccountException`: Error viewing account details

---

## Technology Stack

| Technology | Purpose |
|-----------|---------|
| **Java 11+** | Core language |
| **Collections Framework** | ArrayList, HashMap for efficient storage |
| **Streams API** | Functional programming and data processing |
| **Lambda Expressions** | Functional interfaces and callbacks |
| **File I/O (NIO)** | File reading/writing with Paths and Files |
| **CSV Format** | Data persistence |
| **JUnit 5** | Unit and integration testing |
| **Custom Utilities** | Console I/O, validation, formatting |

---

## Getting Started

### Prerequisites
- Java 11 or higher
- JDK installed and configured in PATH
- IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)

### Installation

1. **Clone/Download the repository**
   ```bash
   git clone <repository-url>
   cd Advanced-Bank_Account-Management-system
   ```

2. **Compile the project**
   ```bash
   javac -d bin src/**/*.java
   ```

3. **Run the application**
   ```bash
   java -cp bin main.Main
   ```

---

## Usage Guide

### Main Menu Options

When you run the application, you'll see the main menu with the following options:

```
+-----------+
| MAIN MENU |
+-----------+
1. Create Account       - Create a new customer account
2. View Accounts        - Display all accounts and summary
3. View Customers       - Display all customer information
4. Process Transaction  - Perform deposits, withdrawals, transfers
5. View All Transactions- View complete transaction history
6. Generate Account Statement - Get detailed account statement
7. Run Tests            - Execute unit and integration tests
8. Simulate Concurrent Transactions - Test multi-threaded operations
9. Exit                 - Save and exit the application
```

### Example Workflow

#### Creating an Account
1. Select option **1** from main menu
2. Choose customer type (Regular/Premium)
3. Enter customer details (name, age, contact, address)
4. Select account type (Savings/Checking)
5. Enter initial balance
6. Account created with auto-generated number

#### Processing a Transaction
1. Select option **4** from main menu
2. Choose transaction type (Deposit/Withdrawal/Transfer)
3. Enter account number
4. Enter amount
5. Transaction processed and recorded

#### Viewing Account Statement
1. Select option **6** from main menu
2. Enter account number
3. View detailed statement with all transactions

---

## Learning Objectives

This project demonstrates mastery of the following Java concepts:

### 1. **Collections Framework**
- **ArrayList**: Maintains ordered account and transaction records
- **HashMap**: Provides O(1) account lookup by account number
- Example: `accountManager.findAccountByNumber(number)` uses HashMap.get()

### 2. **Streams API & Lambda Expressions**
- Stream operations for filtering and transforming data
- Lambda expressions for functional programming
- Example: Calculate total balance across all accounts
  ```java
  double totalBalance = accounts.stream()
      .mapToDouble(Account::getBalance)
      .sum();
  ```

### 3. **File I/O (NIO)**
- Java NIO Paths and Files API for persistence
- Stream-based file reading with automatic resource management
- Example: Load accounts from CSV with Streams
  ```java
  List<Account> accounts = Files.lines(filePath)
      .filter(line -> !line.trim().isEmpty())
      .map(this::csvToAccount)
      .collect(Collectors.toList());
  ```

### 4. **Object-Oriented Design**
- Abstract classes (Account, Customer)
- Inheritance and polymorphism
- Interface implementation (Transactable)
- Encapsulation and access modifiers

### 5. **Exception Handling**
- Custom exception classes
- Checked vs unchecked exceptions
- Try-catch-finally blocks
- Exception propagation and handling

### 6. **Concurrency**
- Multi-threaded transaction simulation
- Thread safety considerations
- Concurrent modification handling

---

## Testing

The project includes comprehensive unit and integration tests covering:

### Test Coverage
- **Account Tests**: Account creation, deposit, withdrawal, balance validation
- **CheckingAccount Tests**: Overdraft functionality
- **SavingsAccount Tests**: Interest calculation
- **Customer Tests**: Customer creation and properties
- **AccountManager Tests**: CRUD operations, balance calculations
- **TransactionManager Tests**: Transaction processing and filtering

### Running Tests

**Option 1: Through Main Menu**
- Run the application and select option **7** from the main menu

**Option 2: Direct Execution**
```bash
java -cp bin utils.CustomTestRunner
```

**Option 3: IDE Test Runner**
- Right-click on test files and select "Run Tests"

---

## Architecture

### Design Patterns Used

1. **MVC Pattern**
   - Models: Account, Customer, Transaction
   - Services: Business logic layer (AccountManager, TransactionManager)
   - Main: Presentation layer (console UI)

2. **Service Layer Pattern**
   - Separation of concerns
   - Business logic isolated from UI

3. **Persistence Layer Pattern**
   - FilePersistence classes handle all data access
   - Abstraction from domain models

4. **Factory Pattern**
   - Account creation (SavingsAccount vs CheckingAccount)
   - Customer creation (RegularCustomer vs PremiumCustomer)

### Data Flow

```
User Input → Main → ConsoleInputReader
    ↓
ConsoleInputReader → Service Layer (AccountManager, TransactionManager)
    ↓
Service Layer → Models (Account, Customer, Transaction)
    ↓
Models → FilePersistence (for saving)
    ↓
File System (accounts.txt, transactions.txt)
```

---

## Exception Handling

All operations are wrapped with appropriate exception handling:

```java
try {
    account.withdraw(amount);
    transactionManager.recordTransaction(transaction);
} catch (InsufficientFundsException e) {
    System.out.println("Error: " + e.getMessage());
} catch (InvalidAmountException e) {
    System.out.println("Error: " + e.getMessage());
} finally {
    // Cleanup or logging
}
```

---

## File Persistence

### Data Storage Format

**accounts.txt** (CSV format):
```
AccountNumber,CustomerID,CustomerName,Balance,AccountType,Status
ACC001,CUS0,John Doe,5000.0,SAVINGS,Active
ACC002,CUS1,Jane Smith,3500.0,CHECKING,Active
```

**transactions.txt** (CSV format):
```
TransactionID,AccountNumber,Type,Amount,Timestamp,Description
TXN001,ACC001,DEPOSIT,1000.0,2024-01-15 10:30:45,Initial Deposit
TXN002,ACC001,WITHDRAWAL,500.0,2024-01-15 11:15:22,ATM Withdrawal
```

### Auto-Save Features
- Data is automatically loaded on application startup
- Data is saved on every operation (transaction, account creation)
- Final save occurs on application exit

---

## Contributing

This is an educational project. For improvements or bug fixes:

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit for review

---

## Learning Resources

- Java Collections Documentation
- Java Streams API Guide
- Java NIO Files and Paths
- Exception Handling Best Practices
- Object-Oriented Design Principles

---

## License

This project is licensed under the terms specified in the [LICENSE](legal/LICENSE) file.

---

**Author:** Emmanuel Mensah Peprah
