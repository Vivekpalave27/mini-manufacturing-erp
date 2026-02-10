ACCESS CONTROL MATRIX

Public APIs:
- /api/auth/**

ADMIN Only APIs:
- /api/users/**
- /api/roles/**

ADMIN + STAFF APIs:
- /api/items/**

Rules:
- All APIs (except /api/auth/**) require JWT token
- ADMIN has full access
- STAFF has limited access
- Unauthorized access returns 403 Forbidden
