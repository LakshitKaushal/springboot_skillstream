# How to Set Database Details in Render

## Method 1: Link Datastore (Easiest - Recommended) ✅

1. **Go to your Web Service** in Render Dashboard
2. Click **"Environment"** tab
3. Click **"Add Environment Variable"** button
4. Select **"Add from Datastore"** from the dropdown
5. Choose your **PostgreSQL datastore** from the list
6. Click **"Add"**

Render will automatically add:
- ✅ `DATABASE_URL` - Full connection string
- ✅ `DB_USERNAME` - Database username  
- ✅ `DB_PASSWORD` - Database password

**Done!** No need to manually copy/paste anything.

---

## Method 2: Manual Setup

If you prefer to set them manually:

### Step 1: Get Database Details

1. Go to your **PostgreSQL Datastore** in Render Dashboard
2. Click **"Info"** tab
3. Note down:
   - **Internal Database URL** (or External if needed)
   - **User** (username)
   - **Database** name
   - **Password** (the one you set when creating)

### Step 2: Add Environment Variables

In your **Web Service** → **Environment** tab:

#### Add DATABASE_URL:
- **Name**: `DATABASE_URL`
- **Value**: Copy the **Internal Database URL** from your datastore
  - Format: `postgresql://user:password@host:port/database`
  - Example: `postgresql://skillstream_user:MyPass123@dpg-abc123-a.oregon-postgres.render.com:5432/skillstream`

#### Add DB_USERNAME:
- **Name**: `DB_USERNAME`
- **Value**: Your database username
  - Example: `skillstream_user`

#### Add DB_PASSWORD:
- **Name**: `DB_PASSWORD`
- **Value**: Your database password
  - Example: `MyPass123`

---

## Complete Environment Variables Checklist

Make sure you have ALL of these set in your Web Service:

### Required for Database:
- [ ] `DATABASE_URL` (or use Method 1 to auto-add all three)
- [ ] `DB_USERNAME`
- [ ] `DB_PASSWORD`

### Required for Application:
- [ ] `SPRING_PROFILES_ACTIVE=prod`
- [ ] `JWT_SECRET=<your-secure-random-string>`
- [ ] `JWT_EXPIRATION_MS=86400000` (optional, has default)

---

## Generate JWT_SECRET

If you need to generate a secure JWT_SECRET:

**PowerShell:**
```powershell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

**Linux/Mac:**
```bash
openssl rand -base64 32
```

---

## After Setting Variables

1. **Save Changes** (if manual setup)
2. **Redeploy** your service:
   - Click **"Manual Deploy"** → **"Clear build cache & deploy"**
   - Or wait for auto-deploy

3. **Check Logs** - You should see:
   ```
   DATABASE_URL is set: true
   Constructed JDBC URL: jdbc:postgresql://...
   ```

---

## Troubleshooting

**If DATABASE_URL is missing:**
- Use Method 1 (Add from Datastore) - it's the easiest
- Make sure your datastore is created and running
- Verify both services are in the same Render account

**If connection fails:**
- Check if using Internal vs External URL
- Internal URL: Use if Web Service and Datastore are in same region
- External URL: Use if connecting from outside Render
- Verify datastore is running (green status)

**To view DATABASE_URL value:**
- Go to Environment tab → Click on `DATABASE_URL`
- Password will be masked: `postgresql://user:****@host:port/db`

