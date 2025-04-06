# `Smart Task Management System`
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
### Clone the repository: 
### Backend:
