# PiggyMetrics - Financial Management System

## Overview
PiggyMetrics is a **personal finance management system** that helps users **track expenses**, manage **monthly budgets**, **repay debts**, 
and handle **savings efficiently**. This project provides **automated and manual debt repayment mechanisms**, **custom interest calculations**, and **financial warnings**.

##  Features
 **Two Budget Modes**  
- **Automatic Budget Update** → Salary-based budget resets monthly  
- **Manual Budget Update** → Users set budget manually  

 **Expense Tracking & Debt Management**  
- Expenses accumulate **daily**  
- **Debt increases** when expenses exceed budget & savings  
- Users can **repay debt manually or automatically**  
- Interest calculations applied **monthly**  

 **Savings & Debt Handling**  
- Leftover budget **moves to savings**  
- **Debt accumulates interest** if unpaid  
- Users can **toggle automatic interest payments**  

 **Financial Warnings & Alerts**  
-  **Debt Warning** when debt exceeds **50% of budget**  
-  **Missed Payment Alert** if debt is not repaid  
-  **Critical Alert** when debt is **greater than savings**

  ##  Technologies Used
- **Spring Boot**  (Backend framework)  
- **Hibernate / JPA**  (Database ORM)  
- **PostgreSQL**  (Database)  
- **Spring Scheduler**  (Automated debt & budget updates)
