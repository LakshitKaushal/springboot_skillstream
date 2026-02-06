# Deploy Backend to Render - Complete Guide

## Prerequisites
1. GitHub account
2. Render account (sign up at https://render.com)
3. Your code pushed to a GitHub repository

## Step 1: Prepare Your Code

### 1.1 Update application.properties (if needed)
The local `application.properties` is already configured. The production config uses environment variables.

### 1.2 Commit and Push to GitHub
```bash
cd "skillstream_springboot_with_mvnw/backend"
git init  # if not already a git repo
git add .
git commit -m "Prepare for Render deployment"
git remote add origin <YOUR_GITHUB_REPO_URL>
git push -u origin main
```

## Step 2: Create PostgreSQL Database on Render

1. Go to https://dashboard.render.com
2. Click **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name**: `skillstream-db` (or any name)
   - **Database**: `skillstream` (or any name)
   - **User**: `skillstream_user` (or any name)
   - **Region**: Choose closest to you
   - **Plan**: Free (or paid)
4. Click **"Create Database"**
5. **IMPORTANT**: Note down the connection details:
   - Internal Database URL
   - External Database URL (if needed)
   - Host, Port, Database name, Username, Password

## Step 3: Deploy Web Service on Render

### Option A: Using render.yaml (Recommended)

1. Go to https://dashboard.render.com
2. Click **"New +"** → **"Blueprint"**
3. Connect your GitHub repository
4. Render will detect `render.yaml` automatically
5. Click **"Apply"**

### Option B: Manual Setup

1. Go to https://dashboard.render.com
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository
4. Select the repository and branch
5. Configure:
   - **Name**: `skillstream-backend`
   - **Environment**: `Java`
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`
   - **Root Directory**: `backend` (if your repo root is the parent folder)

## Step 4: Configure Environment Variables

In your Render Web Service dashboard, go to **"Environment"** tab and add:

### Required Variables:
```
SPRING_PROFILES_ACTIVE=prod
PORT=5000
```

### Database Variables:
Get these from your PostgreSQL service:

**Option 1: Using DATABASE_URL (if Render provides it)**
```
DATABASE_URL=postgresql://user:password@host:port/database
```

**Option 2: Using separate variables**
```
DATABASE_URL=postgresql://skillstream_user:YOUR_PASSWORD@dpg-xxxxx-a/skillstream
DB_USERNAME=skillstream_user
DB_PASSWORD=YOUR_PASSWORD
```

### JWT Variables:
```
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
JWT_EXPIRATION_MS=86400000
```

**To generate a secure JWT_SECRET:**
```bash
# On Linux/Mac:
openssl rand -base64 32

# On Windows PowerShell:
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

## Step 5: Link Database to Web Service

1. In your Web Service dashboard
2. Go to **"Environment"** tab
3. Under **"Add Environment Variable"**, click **"Add from Database"**
4. Select your PostgreSQL database
5. Render will automatically add:
   - `DATABASE_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`

## Step 6: Deploy

1. Click **"Save Changes"**
2. Render will automatically start building and deploying
3. Wait for deployment to complete (usually 5-10 minutes)
4. Your backend will be available at: `https://your-service-name.onrender.com`

## Step 7: Verify Deployment

1. Check health endpoint:
   ```
   https://your-service-name.onrender.com/health
   ```
   Should return: `{"status":"UP"}`

2. Check API info:
   ```
   https://your-service-name.onrender.com/
   ```
   Should return API information

3. Test courses endpoint:
   ```
   https://your-service-name.onrender.com/api/v1/courses
   ```
   Should return: `[]` (empty array if no courses)

## Troubleshooting

### Build Fails
- Check build logs in Render dashboard
- Ensure Java 17 is available (Render auto-detects)
- Verify `pom.xml` is correct

### Database Connection Fails
- Verify DATABASE_URL format: `postgresql://user:password@host:port/database`
- Check database is running in Render dashboard
- Ensure environment variables are set correctly

### Application Crashes
- Check logs in Render dashboard
- Verify all environment variables are set
- Ensure JWT_SECRET is set

### Port Issues
- Render sets PORT automatically, don't hardcode it
- Use `${PORT}` in application.properties (already done)

## Update Frontend to Use Production Backend

After deployment, update your frontend `vite.config.js` or set environment variable:

```javascript
// In frontend/.env or vite.config.js
VITE_API_BASE_URL=https://your-service-name.onrender.com/api/v1
```

## Quick Commands Reference

```bash
# Local build test
cd backend
./mvnw clean package -DskipTests

# Test JAR locally
java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# Git commands
git add .
git commit -m "Deploy to Render"
git push origin main
```

## Render Free Tier Limitations

- Services spin down after 15 minutes of inactivity
- First request after spin-down takes ~30-60 seconds
- 750 hours/month free (enough for always-on if single service)
- Database: 90 days retention on free tier

## Next Steps

1. Set up custom domain (optional)
2. Configure SSL (automatic on Render)
3. Set up monitoring/alerts
4. Update frontend API URL to production

