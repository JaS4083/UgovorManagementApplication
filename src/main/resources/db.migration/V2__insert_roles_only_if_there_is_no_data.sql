DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM role) THEN
      INSERT INTO role (name)
      VALUES
        ('ROLE_USER'),
        ('ROLE_VIEWER'),
        ('ROLE_MODERATOR'),
        ('ROLE_MANAGER');
   END IF;
END $$;