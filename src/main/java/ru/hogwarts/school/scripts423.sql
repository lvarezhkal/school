select  student.name, student.age , faculty.name
from student  inner join faculty  on student.faculty_id  = faculty.id ;
select student.name, student.age, avatar.student_id
from student inner join avatar on student.id=avatar.student_id ;