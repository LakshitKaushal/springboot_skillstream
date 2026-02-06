# How to Find DATABASE_URL in Render

## Method 1: From PostgreSQL Datastore Service

1. Go to https://dashboard.render.com
2. Click on your **PostgreSQL datastore** service (e.g., `skillstream-db`)
3. In the **Info** tab, you'll see:
   - **Internal Database URL** - Use this if your web service is in the same region
   - **External Database URL** - Use this if connecting from outside
   - Format: `postgresql://user:password@host:port/database`

## Method 2: From Web Service Environment Variables

1. Go to https://dashboard.render.com
2. Click on your **Web Service** (e.g., `skillstream-backend`)
3. Go to **Environment** tab
4. Look for `DATABASE_URL` in the list
5. If it's there, you can click on it to see the value (password is hidden)
6. If it's NOT there, use Method 3 to link your Datastore

## Method 3: Link Datastore to Web Service (Recommended)

1. Go to your **Web Service** → **Environment** tab
2. Click **"Add Environment Variable"**
3. Select **"Add from Datastore"** (or "Add from Database" if shown)
4. Choose your PostgreSQL datastore from the dropdown
5. Render will automatically add:
   - `DATABASE_URL` (full connection string)
   - `DB_USERNAME` (database username)
   - `DB_PASSWORD` (database password)

## Method 4: Manual Construction

If you need to create it manually, the format is:

```
postgresql://username:password@host:port/database
```

**Example:**
```
postgresql://skillstream_user:MyPassword123@dpg-abc123-a.oregon-postgres.render.com:5432/skillstream
```

**To get the components:**
- **Username**: From database service → Info tab → User
- **Password**: The password you set when creating the database
- **Host**: From database service → Info tab → Hostname
- **Port**: Usually `5432` (shown in database service)
- **Database**: From database service → Info tab → Database name

## Quick Checklist

- [ ] PostgreSQL datastore is created
- [ ] Web Service is created
- [ ] Datastore is linked to Web Service (Method 3 - "Add from Datastore")
- [ ] `DATABASE_URL` appears in Web Service → Environment tab
- [ ] `SPRING_PROFILES_ACTIVE=prod` is set
- [ ] `JWT_SECRET` is set

## Troubleshooting

**If DATABASE_URL is missing:**
- Use Method 3: Click "Add Environment Variable" → "Add from Datastore"
- Select your PostgreSQL datastore from the dropdown
- Make sure both services are in the same Render account

**If connection fails:**
- Check if using Internal vs External URL (use Internal if same region)
- Verify database is running (green status in dashboard)
- Check firewall/network settings

**To view the actual URL (password hidden):**
- Go to Web Service → Environment → Click on `DATABASE_URL`
- The value will show with password masked: `postgresql://user:****@host:port/db`

