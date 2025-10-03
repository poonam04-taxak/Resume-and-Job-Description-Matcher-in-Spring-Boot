# Resume and Job Description Matcher in Spring Boot

A Spring Boot application that parses job descriptions (JDs) and resumes, extracts relevant skills, and calculates a match percentage between them. Built to help HRs and recruiters quickly find the best candidates for a role.

---

## Features

- **Job Description Parsing**: Automatically extracts required and preferred skills from JD text.
- **Resume Parsing**: Extracts skills from resumes in PDF/DOCX format.
- **Skill Matching**: Compares candidate resumes with JDs to calculate an overall match percentage.
- **Skill Normalization**: Handles aliases and variations of skills (e.g., "JS" → "JavaScript", "RESTAPI" → "REST API").
- **REST APIs**: Easily integrate with other HR tools or front-end applications.
- **Persistence**: Stores JDs and resumes using Spring Data JPA with H2/MySQL/PostgreSQL support.

---

## Tech Stack

- **Java 24**  
- **Spring Boot 3.5.x**  
- **Spring Data JPA**  
- **Maven**  
- **H2 / MySQL **  
- **PDF & DOCX parsing** (Apache PDFBox, Apache POI)  
- **JUnit 5 & Mockito** for testing  

---

## Getting Started

### Prerequisites

- Java 24+
- Maven 3.8+
- IDE (IntelliJ IDEA recommended)

### Running the Project

1. Clone the repository:

```bash
git clone https://github.com/poonam04-taxak/Resume-and-Job-Description-Matcher-in-Spring-Boot.git
cd Resume-and-Job-Description-Matcher-in-Spring-Boot

2. Build the project:
mvn clean install

3. Run the application:
mvn spring-boot:run

4. The application will start at: http://localhost:8084

API Endpoints:

Job Description: POST /api/jd – Add a new JD
GET /api/jd – Get all JDs
GET /api/jd/{id} – Get JD by ID

Resume: POST /api/resume – Upload a new resume
GET /api/resume – Get all resumes
GET /api/resume/{id} – Get resume by ID

Matching: GET /api/match/{jdId} – Get all resumes matched to a JD with percentage

Example:
Saving a Job Description
POST /api/jd
{
  "title": "Java Developer",
  "content": "We are looking for a Java Developer with experience in Spring Boot, REST API, SQL, and Docker. Preferred: AWS, Kubernetes, React."
}
Matching Resumes
GET /api/match/1
Response:
[
  {
    "resumeId": 5,
    "candidateName": "Poonam Taxak",
    "overallMatchPercentage": 85
  }
]
Testing
Run all tests:
mvn test
Tests include: JobDescriptionServiceTest – Testing JD parsing & skill extraction

ResumeServiceTest – Testing resume parsing & skill extraction

MatchServiceTest – Testing matching logic

Contributing
1.Fork the repository
2.Create a new branch (git checkout -b feature/YourFeature)
3.Commit your changes (git commit -m 'Add feature')
4.Push to the branch (git push origin feature/YourFeature)
5.Open a Pull Request

License
This project is licensed under the MIT License.

Author
Poonam Taxak
