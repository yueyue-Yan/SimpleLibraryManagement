drop table if exists sb;

/*==============================================================*/
/* Table: sb                                                    */
/*==============================================================*/
create table sb
(
   sno                  varchar(20) ,
   bno                  varchar(20),
   foreign key(sno) references student(sno),
   foreign key(bno) references book(bno)  
);
