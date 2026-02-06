# Manual Render Setup (No Blueprint)

Since the Blueprint approach is having path issues, use **manual Web Service configuration** instead.

## Step 1: Create PostgreSQL Database

1. Go to https://dashboard.render.com
2. Click **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name**: `skillstream-db`
   - **Database**: `skillstream`
   - **User**: `skillstream_user`
   - **Plan**: Free
4. Click **"Create Database"**
5. Wait for it to be ready

## Step 2: Create Web Service (Manual)

1. Go to https://dashboard.render.com
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub account (if not already)
4. Select repository: `LakshitKaushal/springboot_skillstream`
5. Configure the service:

### Basic Settings:
- **Name**: `skillstream-backend`
- **Environment**: **Docker** (select from dropdown)
- **Region**: Choose closest to you
- **Branch**: `master`

### Build & Deploy:
- **Root Directory**: Leave **EMPTY** (don't set anything)
- **Dockerfile Path**: `skillstream_springboot_with_mvnw/backend/Dockerfile`
- **Docker Build Context**: Leave **EMPTY** (defaults to repo root)

### Environment Variables:
Click **"Add Environment Variable"** and add:

```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=<generate-using-command-below>
JWT_EXPIRATION_MS=86400000
```

**Generate JWT_SECRET:**
```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

### Link Database:
1. Click **"Add Environment Variable"**
2. Select **"Add from Database"**
3. Choose your PostgreSQL database (`skillstream-db`)
4. This auto-adds:
   - `DATABASE_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`

## Step 3: Deploy

1. Click **"Create Web Service"**
2. Wait for build to complete (5-10 minutes)
3. Your API will be at: `https://skillstream-backend.onrender.com`

## Important Notes:

- ✅ **Root Directory**: Leave EMPTY - this is key!
- ✅ **Dockerfile Path**: `skillstream_springboot_with_mvnw/backend/Dockerfile`
- ✅ **Docker Build Context**: Leave EMPTY (uses repo root by default)
- ✅ The Dockerfile already handles copying from repo root

## Troubleshooting:

If build fails:
1. Check build logs in Render dashboard
2. Verify Dockerfile path is correct
3. Make sure Root Directory is EMPTY
4. Verify all environment variables are set

