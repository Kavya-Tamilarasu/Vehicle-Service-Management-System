# Car Service Management System
### Group Project – Feature Documentation

---

## Member 1 – User Management

**Overview:** Handle registration, login, and basic profile management for customers and admins.

**Data Fields:** User ID, Name, Email, Password (hashed), Phone, Role (Admin/Customer), Date Registered

**CRUD Operations:**
| Operation | Action |
|---|---|
| Create | Register a new user |
| Read | View user profile / list all users (admin) |
| Update | Edit profile details, change password |
| Delete | Remove a user account |

**Features:**
- Register & login with email and password
- Role-based access (Admin sees all users, Customer sees own profile)
- Edit profile info
- Admin can delete accounts

---

## Member 2 – Car Management

**Overview:** Manage the cars registered under customers in the system.

**Data Fields:** Car ID, Owner (User ID), Make, Model, Year, License Plate, Color, Mileage

**CRUD Operations:**
| Operation | Action |
|---|---|
| Create | Add a new car |
| Read | View car details / list all cars |
| Update | Edit car info or update mileage |
| Delete | Remove a car record |

**Features:**
- Add a car with full details
- View all cars (admin) or own cars (customer)
- Update car mileage and details
- Delete a car from the system

---

## Member 3 – Service & Maintenance Records

**Overview:** Log and track service jobs done on cars.

**Data Fields:** Record ID, Car ID, Service Type, Description, Date, Cost, Status (Pending/Completed)

**CRUD Operations:**
| Operation | Action |
|---|---|
| Create | Add a new service record |
| Read | View service history for a car |
| Update | Edit record details or mark as completed |
| Delete | Remove a service record |

**Features:**
- Log a service entry for a car
- View full service history
- Update service status (Pending → Completed)
- Delete incorrect records

---

## Member 4 – Service Reminder & Notification Management

**Overview:** Set and manage reminders for upcoming or recurring services.

**Data Fields:** Reminder ID, Car ID, Reminder Type (e.g., Oil Change), Due Date, Notes, Status (Active/Dismissed)

**CRUD Operations:**
| Operation | Action |
|---|---|
| Create | Add a new reminder |
| Read | View all reminders / upcoming reminders |
| Update | Edit reminder date or mark as dismissed |
| Delete | Remove a reminder |

**Features:**
- Create reminders for specific cars
- View upcoming and past reminders
- Mark reminders as dismissed/done
- Highlight overdue reminders

---

## Member 5 – Feedback & Review Management

**Overview:** Allow customers to leave reviews for services received.

**Data Fields:** Review ID, User ID, Service Record ID, Rating (1–5), Comment, Date Submitted

**CRUD Operations:**
| Operation | Action |
|---|---|
| Create | Submit a review |
| Read | View all reviews / reviews per service |
| Update | Edit own review |
| Delete | Remove a review (admin or owner) |

**Features:**
- Submit a star rating and comment after a service
- View all reviews (admin) or own reviews (customer)
- Edit or delete a submitted review
- Display average rating

---

## Member 6 – Spare Parts & Inventory Management

**Overview:** Track spare parts stocked by the service center.

**Data Fields:** Part ID, Part Name, Category, Brand, Quantity in Stock, Minimum Stock Level, Unit Price, Supplier, Date Added

**CRUD Operations:**
| Operation | Action |
|---|---|
| Create | Add a new spare part |
| Read | View all parts / single part details |
| Update | Edit part info or restock quantity |
| Delete | Remove a part from inventory |

**Features:**
- Add and manage spare parts
- View full inventory list
- Update stock quantities
- Low stock warning when quantity falls below minimum level
