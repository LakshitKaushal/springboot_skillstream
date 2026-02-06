# Setup Instructions for SkillStream Spring Boot Application

## Issues Fixed
1. ✅ Added explicit Hibernate dialect (`org.hibernate.dialect.PostgreSQLDialect`) to `application.properties`

## Prerequisites
- Java 17 or higher (you have Java 23 installed ✅)
- PostgreSQL installed and running (PostgreSQL is running on port 5432 ✅)
- Maven (included via mvnw wrapper ✅)

## Database Setup

The application requires a PostgreSQL database named `SkillStream_springboot`. 

### Option 1: Using pgAdmin or PostgreSQL GUI
1. Open pgAdmin or your PostgreSQL GUI tool
2. Connect to your PostgreSQL server (localhost:5432)
3. Create a new database named: `SkillStream_springboot`
4. Make sure the user `postgres` has access to this database

### Option 2: Using Command Line (if psql is in your PATH)
```bash
# Set password (Windows PowerShell)
$env:PGPASSWORD="Lakshit@123"

# Create database
psql -U postgres -h localhost -c "CREATE DATABASE \"SkillStream_springboot\";"
```

### Option 3: Using SQL Script
Connect to PostgreSQL and run:
```sql
CREATE DATABASE "SkillStream_springboot";
```

## Running the Application

### Backend (Spring Boot)
```bash
cd backend
.\mvnw.cmd spring-boot:run
```

The server will start on **port 8000** (http://localhost:8000)

### Frontend (React)
```bash
cd frontend
npm install
npm run dev
```

## Configuration

The application is configured in `backend/src/main/resources/application.properties`:
- **Port**: 8000
- **Database**: SkillStream_springboot
- **Username**: postgres
- **Password**: Lakshit@123

If you need to change these settings, edit the `application.properties` file.

## Troubleshooting

### If you get database connection errors:
1. Verify PostgreSQL is running: `Test-NetConnection -ComputerName localhost -Port 5432`
2. Verify the database exists
3. Check username/password in `application.properties`
4. Ensure the database name matches exactly (case-sensitive): `SkillStream_springboot`

### If you get port already in use:
- Change `server.port` in `application.properties` to a different port (e.g., 8001)

