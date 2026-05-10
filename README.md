# Gym Membership Management System Technical Task

<p align="center">
<img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java" />
<img src="https://img.shields.io/badge/Maven-4.0.0-C71A36?style=for-the-badge&logo=apachemaven" alt="Maven" />
<img src="https://img.shields.io/badge/Spring_Boot-4.0.6-green?style=for-the-badge&logo=springboot" alt="Spring Boot" />
<img src="https://img.shields.io/badge/H2-Database-orange?style=for-the-badge&logo=h2" alt="H2 Database" />

</p>

**Gym Membership Management System Technical Task** – REST API for managing gym memberships, including gyms, membership plans and members.

---

## 📑 Table of Contents

* [Tech Stack](#-tech-stack)
* [How to Run the Project](#-how-to-run-the-project)
* [REST	API	endpoints](#-rerst-api-endpoints)
* [Project Structure](#-project-structure)

---

## 🏗 Tech Stack

* **Java:** 21
* **Spring Boot:** 4.0.6
* **Apache-Maven:** 4.0.0
* **Database:** H2

---

## 🚀 How to Run the Project

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/rakets/Sii-test-task.git
    ```
2.  **Go to the project folder:**
    ```bash
    cd Sii-test-task
    ```
3.  **Build the project:**
    ```bash
    mvn clean install
    ```
6.  **Run the project:**
    ```bash
    mvn spring-boot:run
    ```
    Server will be available at `http://localhost:8080`.

---

## 🚀 REST	API	endpoints

Whole endpoints documentation you can find in project folder -> **Sii-test-task.postman_collection.json**

1. **Create a new gym:**
    ```bash
    http://localhost:8080/api/gyms/new
    ```
2. **List all gyms:**
    ```bash
    http://localhost:8080/api/gyms/all-gyms
    ```
3. **Create	a new membership plan for a given gym:**
    ```bash
    for example, we try to create new plan for gym with ID = 1
    ```
    ```bash
    http://localhost:8080/api/1/membership-plan/new
    ```
4. **List all membership plans for a given gym:**
     ```bash
    for example, we try to get all plans for gym with ID = 1
    ```
    ```bash
    http://localhost:8080/api/1/membership-plan/all-plans
    ```
5. **Register a new	member to a given membership plan (validate	capacity):**
     ```bash
    for example, we try to register new member for gym with ID = 1 and plan with ID = 1
    ```
    ```bash
    http://localhost:8080/api/member/1/1/new
    ```
6. **List all members - include the	plan name, gym name and status:**
    ```bash
    http://localhost:8080/api/member/all
    ```
7. **Cancel a membership:**
    <p>for example, we cancel membership plan for member with ID = 1</p>
    ```bash
    http://localhost:8080/api/member/cancel-membership/1
    ```
8. **Return the	revenue	report:**
    ```bash
    http://localhost:8080/api/gyms/report
    ```

---

## 📂 Project Structure

