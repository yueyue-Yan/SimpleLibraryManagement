drop table if exists Book;

/*==============================================================*/
/* Table: Book                                                  */
/*==============================================================*/
create table Book
(
   bno                  varchar(20) not null,
   bname                varchar(10) not null,
   price                double(7,2),
   author               varchar(20),
   remaining            int not null,
   primary key (bno)
);
