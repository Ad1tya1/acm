# Access Module Management

Access Module Management System is a Java application that allows you to manage employee information, including their names and assigned modules.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- Create, Read, Update, and Delete employees.
- Assign modules to employees.
- Retrieve a list of all employees.
- Retrieve an individual employee by ID.
- Handle exceptions for missing employees and modules.

## Getting Started

Follow the instructions below to get the Employee Management System up and running on your local machine.

### Prerequisites

- Java Development Kit (JDK) 8 or later
- Maven
- Your favorite Integrated Development Environment (IDE)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Ad1tya1/acm.git
   ```

2. Open the project in your IDE.

3. Build the project using Maven:

   ```bash
   mvn clean install
   ```

4. Start the application:

   ```bash
   mvn spring-boot:run
   ```

The application should now be running on http://localhost:8080.

## Usage

We have three modules for now into our databse so please select from those only.
 ID, NAME
 1. Library
 2. Dashboard
 3. Reports

### Create an Employee

To create a new employee, make a POST request to the following endpoint:

```
POST /api/employees
```

Example Request Body:

```json
{
  "name": "Aditya",
  "moduleIds": [1, 2, 3]
}
```

### Get All Employees

To retrieve a list of all employees, make a GET request to the following endpoint:

```
GET /api/employees
```

### Get an Employee by ID

To retrieve an individual employee by their ID, make a GET request to the following endpoint:

```
GET /api/employees/{id}
```

### Update an Employee

To update an employee's information, make a PUT request to the following endpoint:

```
PUT /api/employees/{id}
```

Example Request Body:

```json
{
  "name": "Ajay",
  "moduleIds": [2, 3]
}
```

### Delete an Employee

To delete an employee by ID, make a DELETE request to the following endpoint:

```
DELETE /api/employees/{id}
```
