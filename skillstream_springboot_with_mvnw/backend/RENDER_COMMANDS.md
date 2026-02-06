# Quick Commands for Render Deployment

## 1. Prepare and Push to GitHub

```bash
# Navigate to backend directory
cd "C:\Users\HP\Downloads\springboot final\skillstream_springboot_with_mvnw\backend"

# Initialize git (if not already)
git init

# Add all files
git add .

# Commit
git commit -m "Prepare for Render deployment"

# Add remote (replace with your GitHub repo URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git

# Push to GitHub
git push -u origin main
```

## 2. Test Build Locally (Optional)

```bash
# Build the JAR
.\mvnw.cmd clean package -DskipTests

# Test run with production profile
java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 3. Render Dashboard Setup

### Create PostgreSQL Database:
1. Go to: https://dashboard.render.com
2. Click: **"New +"** → **"PostgreSQL"**
3. Name: `skillstream-db`
4. Database: `skillstream`
5. User: `skillstream_user`
6. Plan: **Free**
7. Click: **"Create Database"**

### Create Web Service:
1. Go to: https://dashboard.render.com
2. Click: **"New +"** → **"Web Service"**
3. Connect GitHub repository
4. Select your repository
5. Configure:
   - **Name**: `skillstream-backend`
   - **Root Directory**: `backend` (if repo root is parent folder)
   - **Environment**: `Java`
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`

### Set Environment Variables:
In Web Service → **Environment** tab, add:

```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=<generate-secure-random-string>
JWT_EXPIRATION_MS=86400000
```

Then click **"Add from Database"** and select your PostgreSQL database to auto-add:
- `DATABASE_URL`
- `DB_USERNAME`  
- `DB_PASSWORD`

### Generate JWT_SECRET (PowerShell):
```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

## 4. Deploy

1. Click **"Create Web Service"** or **"Save Changes"**
2. Wait for build to complete (~5-10 minutes)
3. Your API will be at: `https://skillstream-backend.onrender.com`

## 5. Verify Deployment

```powershell
# Test health endpoint
Invoke-WebRequest -Uri "https://your-service-name.onrender.com/health" -UseBasicParsing

# Test API info
Invoke-WebRequest -Uri "https://your-service-name.onrender.com/" -UseBasicParsing

# Test courses endpoint
Invoke-WebRequest -Uri "https://your-service-name.onrender.com/api/v1/courses" -UseBasicParsing
```

## 6. Update Frontend (After Deployment)

Update `frontend/vite.config.js`:
```javascript
proxy: {
  '/api/v1': {
    target: 'https://your-service-name.onrender.com',
    changeOrigin: true,
    secure: true
  }
}
```

Or set environment variable:
```bash
# In frontend/.env
VITE_API_BASE_URL=https://your-service-name.onrender.com/api/v1
```

## Quick Reference

| Step | Command/Action |
|------|----------------|
| Build locally | `.\mvnw.cmd clean package -DskipTests` |
| Test JAR | `java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod` |
| Git add | `git add .` |
| Git commit | `git commit -m "message"` |
| Git push | `git push origin main` |
| Generate JWT_SECRET | `[Convert]::ToBase64String((1..32 \| ForEach-Object { Get-Random -Maximum 256 }))` |

## Important Notes

- ✅ Render automatically sets `PORT` environment variable
- ✅ Database connection is handled via `DATABASE_URL`
- ✅ Free tier services spin down after 15 min inactivity
- ✅ First request after spin-down takes 30-60 seconds
- ✅ All environment variables must be set in Render dashboard

