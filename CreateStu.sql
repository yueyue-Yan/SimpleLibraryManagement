drop table if exists Student;

/*==============================================================*/
/* Table: Student                                               */
/*==============================================================*/
create table Student
(
   sno                  varchar(20) not null,
   sname                varchar(255) not null,
   psaaword             varchar(255) not null,
   primary key (sno)
);
