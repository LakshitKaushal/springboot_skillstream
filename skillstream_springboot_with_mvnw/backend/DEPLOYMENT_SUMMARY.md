# 🚀 Render Deployment - Complete Command List

## 📋 All Commands You Need

### Step 1: Prepare Code for GitHub

```powershell
# Navigate to backend
cd "C:\Users\HP\Downloads\springboot final\skillstream_springboot_with_mvnw\backend"

# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit
git commit -m "Prepare for Render deployment"

# Add GitHub remote (replace with your repo URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# Push to GitHub
git push -u origin main
```

### Step 2: Test Build Locally (Optional)

```powershell
# Build JAR file
.\mvnw.cmd clean package -DskipTests

# Test the JAR (will fail without database, but verifies build works)
java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Step 3: Generate JWT Secret

```powershell
# Generate secure JWT secret
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

**Copy the output** - you'll need it for Render environment variables.

### Step 4: Render Dashboard Setup

#### 4.1 Create PostgreSQL Database

1. Go to: https://dashboard.render.com
2. Click: **"New +"** → **"PostgreSQL"**
3. Fill in:
   - **Name**: `skillstream-db`
   - **Database**: `skillstream`
   - **User**: `skillstream_user`
   - **Region**: Choose closest
   - **Plan**: Free
4. Click: **"Create Database"**
5. **Wait for database to be ready** (takes 1-2 minutes)

#### 4.2 Create Web Service

1. Go to: https://dashboard.render.com
2. Click: **"New +"** → **"Web Service"**
3. Connect your GitHub account (if not already)
4. Select your repository
5. Configure:
   - **Name**: `skillstream-backend`
   - **Root Directory**: `backend` ⚠️ **IMPORTANT**: If your repo root is the parent folder
   - **Environment**: `Java`
   - **Region**: Same as database
   - **Branch**: `main` (or `master`)
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/skillstream-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`

#### 4.3 Set Environment Variables

In the Web Service dashboard, go to **"Environment"** tab:

**Add these manually:**
```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=<paste-the-secret-from-step-3>
JWT_EXPIRATION_MS=86400000
```

**Add database variables automatically:**
1. Click **"Add Environment Variable"**
2. Select **"Add from Database"**
3. Choose your PostgreSQL database (`skillstream-db`)
4. This will auto-add:
   - `DATABASE_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`

#### 4.4 Deploy

1. Click **"Create Web Service"** (or **"Save Changes"** if editing)
2. Wait for deployment (5-10 minutes)
3. Watch the build logs in real-time

### Step 5: Verify Deployment

```powershell
# Replace YOUR_SERVICE_NAME with your actual service name
$serviceUrl = "https://skillstream-backend.onrender.com"

# Test health endpoint
Invoke-WebRequest -Uri "$serviceUrl/health" -UseBasicParsing

# Test API info
Invoke-WebRequest -Uri "$serviceUrl/" -UseBasicParsing

# Test courses endpoint
Invoke-WebRequest -Uri "$serviceUrl/api/v1/courses" -UseBasicParsing
```

Expected responses:
- `/health` → `{"status":"UP"}`
- `/` → API information JSON
- `/api/v1/courses` → `[]` (empty array)

## 🔧 Troubleshooting Commands

### Check Build Logs
- Go to Render dashboard → Your service → **"Logs"** tab

### Check Environment Variables
- Go to Render dashboard → Your service → **"Environment"** tab

### Restart Service
- Go to Render dashboard → Your service → **"Manual Deploy"** → **"Clear build cache & deploy"**

### Test Database Connection
```powershell
# Get connection string from Render PostgreSQL dashboard
# Test with psql (if installed) or use Render's database browser
```

## 📝 Important Notes

1. **Root Directory**: If your GitHub repo root is `skillstream_springboot_with_mvnw`, set Root Directory to `backend` in Render
2. **Port**: Render automatically sets `PORT` - don't hardcode it (already handled in code)
3. **Database URL Format**: Render provides it as `postgresql://user:pass@host:port/db`
4. **Free Tier**: Services spin down after 15 min inactivity - first request takes 30-60 seconds
5. **Build Time**: First build takes 5-10 minutes, subsequent builds are faster

## 🎯 Quick Checklist

- [ ] Code pushed to GitHub
- [ ] PostgreSQL database created on Render
- [ ] Web service created on Render
- [ ] Environment variables set:
  - [ ] `SPRING_PROFILES_ACTIVE=prod`
  - [ ] `JWT_SECRET` (secure random string)
  - [ ] `JWT_EXPIRATION_MS=86400000`
  - [ ] Database variables (auto-added from database)
- [ ] Service deployed successfully
- [ ] Health endpoint returns `{"status":"UP"}`
- [ ] API endpoints working

## 🔗 Next Steps

After successful deployment:

1. **Update Frontend**: Point frontend to production API URL
2. **Custom Domain**: (Optional) Add custom domain in Render
3. **Monitoring**: Set up alerts in Render dashboard
4. **Backup**: Configure database backups (paid plans)

## 📚 Files Created for Deployment

- ✅ `application-prod.properties` - Production configuration
- ✅ `render.yaml` - Render blueprint (optional, for automated setup)
- ✅ `.gitignore` - Git ignore file
- ✅ `DEPLOY_RENDER.md` - Detailed deployment guide
- ✅ `RENDER_COMMANDS.md` - Quick command reference

Your backend is now ready to deploy! 🎉

