# CodeB IMS - User Authentication Module

## 1. Overview
The CodeB Internal Management System (IMS) provides secure Role-Based Access Control (RBAC) via JSON Web Tokens (JWT). The system features two main user roles:
- **Admin**: Full system control.
- **Sales Person**: Restricted features and invoicing.

## 2. Requirements
- **Backend**: Java 17, Spring Boot 3.x, MySQL
- **Frontend**: React 18, Vite, Bootstrap 5

## 3. Local Setup Instructions

### Database Setup
1. Ensure MySQL is running on `localhost:3306`.
2. Create a database named `codeb_ims`.
3. The Spring Boot application properties (`ims-backend/src/main/resources/application.properties`) are configured to connect to MySQL using the username `root` and an empty password by default. Adjust this if your MySQL setup differs.
4. Launch the application; Hibernate will automatically create all the required tables.

### Running Backend
1. Open terminal inside `ims-backend`.
2. Run `./mvnw spring-boot:run` (or use your IDE's Run button).
3. The backend API is exposed on `http://localhost:8080`.

### Running Frontend
1. Open terminal inside `ims-frontend`.
2. Run `npm install`.
3. Run `npm run dev`.
4. Open your browser to the local URL (usually `http://localhost:5173`).

## 4. End User Functionality (How to Use)
### Registration
1. Navigate to the `/register` page from the login screen.
2. Fill out your Full Name, Email, Password, and select a Role (Admin / Sales Person).
3. Submit the form. Upon success, you will be redirected to the Login page.

### Login
1. Navigate to `/login`.
2. Enter the registered Email and Password.
3. Upon success, the server responds with a secure JWT token, and you are redirected to the Dashboard.

### Dashboard (Role-Based Access)
- The Dashboard intelligently greets you by name.
- It displays conditional UI elements depending on whether you are an **Admin** or a **Sales Person**.
- Clicking **Logout** destroys the local JWT session and returns you to the login wrapper.
- All subsequent features (Invoicing, Subzones, Clients, Chains) are protected globally behind this Authentication milestone.

## 5. Deployment Guide (Free Server)
1. **GitHub Repository**: Push both `ims-backend` and `ims-frontend` to a new public GitHub Repository.
2. **Backend Hosting (Render / Railway)**: 
   - Connect your GitHub repo to a service like Render.com (Web Service). 
   - Choose Java environment, build command `mvn clean package -DskipTests`, start command `java -jar target/ims-0.0.1-SNAPSHOT.jar`.
   - Setup a free MySQL database on Aiven / Render and inject the credentials into Render environment variables (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`).
3. **Frontend Hosting (Vercel / Netlify)**:
   - Connect your repo on Vercel. 
   - Change the Root Directory to `/ims-frontend`. 
   - Once the backend is deployed on Render, update the Axios request URL in `Login.jsx` and `Register.jsx` from `http://localhost:8080` to your new Render.com HTTPS url. Deploy!

*(Note: Export this file as PDF for your assignment submission).*
