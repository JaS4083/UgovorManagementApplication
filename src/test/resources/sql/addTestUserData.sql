WITH inserted_user AS (
  INSERT INTO public.user (id, email, password, first_name, last_name)
  VALUES ('31f91b36-dd1f-480b-b3bb-5dc55bc17e75',
          'artem.java.sirobaba@omegasoft.com',
          '$2a$12$yIuciay7JmoEgsuxPTjB9OHVV2WfGkTGJ.FkjyQmRGtB2QyJBjp72',
          'Artem',
          'Sirobaba'),
           ('223650db-cb29-4e04-8be1-ffb56675abdf',
           'artem.test.java.sirobaba@omegasoft.com.co',
          '$2a$10$MUZhmdJTTtFiLQZe6KGyyefErddWcnVPhng7e76ZQitKop9b86Egq',
          'Artem',
          'Sirobaba')
  RETURNING id
)

INSERT INTO user_roles (user_id, role_id)
SELECT id, 4
FROM inserted_user;