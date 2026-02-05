# Mini Manufacturing ERP

PPO-oriented Mini Manufacturing ERP project built using Spring Boot and React.

## Features
- Authentication & Role Management
- Sales & Purchase Orders
- Inventory via GRR
- Expense Tracking
- Dashboard & Reports

## Tech Stack
- Backend: Spring Boot, Java, JPA, MySQL
- Frontend: React, Tailwind CSS

## Status
Day 1: Project setup and architecture completed.

# Day-2: Database Design & ER Diagram

## Objective
Design and validate database schema for Mini Manufacturing ERP.

## Work Completed
- Created all required tables
- Defined primary and foreign keys
- Established 1:N relationships
- Designed ER Diagram using MySQL Workbench

## Tables Created
- roles
- users
- suppliers
- items
- purchase_orders
- purchase_order_items
- grr
- grr_items
- sales_orders
- sales_order_items
- expenses

## Relationships
1. roles → users (1:N)
2. suppliers → purchase_orders (1:N)
3. purchase_orders → purchase_order_items (1:N)
4. items → purchase_order_items (1:N)
5. purchase_orders → grr (1:N)
6. grr → grr_items (1:N)
7. items → grr_items (1:N)
8. sales_orders → sales_order_items (1:N)
9. items → sales_order_items (1:N)

## Tools Used
- MySQL Workbench
- MySQL
- GitHub

## Status
Day-2 completed.

