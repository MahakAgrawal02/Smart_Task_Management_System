# `Smart Task Management System`
## Demo: 
https://github.com/user-attachments/assets/db3b0ab8-7747-48c5-9ac6-7efd40d99b5e

## Purpose
The Smart Task Management System aims to provide a secure, user-friendly web platform for efficient task creation, assignment, and tracking. It enhances productivity through features like role-based access, prioritization, deadlines, notifications, and search/filter options—ensuring better collaboration, accountability, and streamlined task management within organizations.

## Features: 
- Register and login: Admin can login admin account. Employees can register and login.
- Task Management: Admin can assign tasks to employees using CRUD operations. Tasks have title, description, due date, priority(LOW, HIGH, MEDIUM), status(COMPLETED, INPROGRESS, PENDING, CANCELLED) and name of employee to which task is assigned.
- Search and Filter: Tasks can be searched using keywords and it can be filtered based on priority and status.
- Notifications: Users get notifications of comments. Employees get notifications when assigned tasks.
- Logging and Error handling: Logging and monitoring for all operations done inside the application to track any error.
- Testing: Every service has its own unit testing using Junit.

## `Functionalities`:
### Employee:
- Register
- Login (need to be registered first)
- View task
- Comment
- Change status

## Admin:
- Login
- Create task
- View task
- Update task (Due date, priority, status, Assigned employee, title, description)
- Comment
- Track
- Delete

## `Technologies Used`: 
- Java 8
- Angular (version 16)
- MySQL
- Embedded Tomcat 9 server
- Maven
- Spring Boot

## `Installation`:
### MySQL: Database creation: 
![Screenshot 2025-04-06 173940](https://github.com/user-attachments/assets/152c534c-c0d6-4124-acc8-6577ae051f56)
### Clone the repository: https://github.com/MahakAgrawal02/Smart_Task_Management_System
### Backend:
- Step 1: Update below configuration fields in application.properties inside /resources folder with database name, username and password
![Screenshot 2025-04-06 174613](https://github.com/user-attachments/assets/90c8d368-4d47-40c3-9244-cf6c538466fa)
- Step 2: Run Backend as Spring Boot
### Frontend:
- Step 1: Run npm install
- Step 2: Run npm start
### Go to http://localhost:4200/
- You can acces admin using email as admin@test.com and password admin
- You can register and login employees with correct credentials
