# Bank Account Management System

This repository contains a Java-based Bank Account Management System with separate feature branches for exceptions handling, testing, and the main application entry point.

---

##  Branches & Key Features

### 1. `feature/exceptions`

* Implements **custom and centralized exception handling**
* Handles invalid operations gracefully
* Enforces business rules using exceptions
* Structured `try-catch` in application flow
* Exceptions Package:

```
src/exceptions/
 ├── AccountNotFoundException.java
 ├── InsufficientBalanceException.java
 ├── InvalidAmountException.java
 ├── CustomerNotFoundException.java
 └── TransactionException.java
```

* Usage:

```bash
git switch feature/exceptions
```

### 2. `feature/testing`

* Implements **unit and integration testing**
* Validates core logic in AccountManager, TransactionManager, and CustomerManager
* Uses **JUnit 5** framework
* Test Structure:

```
src/test/
 ├── AccountManagerTest.java
 ├── TransactionManagerTest.java
 └── CustomerManagerTest.java
```

* Usage:

```bash
git switch feature/testing
```

Run tests with IDE or Maven/Gradle.

### 3. Main Application (`Main.java`)

* Entry point of the system
* Initializes managers and orchestrates user interactions
* Provides **menu-driven interface** for banking operations
* Ensures exception-safe execution
* Usage:

```bash
javac Main.java
java Main
```

Follow the menu to create accounts, deposit, withdraw, transfer, and view models details.

---

##  Status

* Exceptions feature:  Complete
* Testing feature:  Complete
* Main application:  Core functionality implemented

---

**Author:** Emmanuel Mensah Peprah
