-- postgresql
-- 用户表
CREATE SEQUENCE public.userinfo_seq
    INCREMENT 1
    START 527
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.userinfo
(
    user_id integer NOT NULL DEFAULT nextval('userinfo_seq'::regclass),
    user_name character varying(20) COLLATE pg_catalog."default" NOT NULL,
    password character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT userinfo_pkey PRIMARY KEY (user_id)
);
-- 用户数据
INSERT INTO userinfo (user_name,password) VALUES ('nicker','e10adc3949ba59abbe56e057f20f883e');


-- mysql
/*CREATE TABLE `db_jira`.`userinfo` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`user_id`));*/