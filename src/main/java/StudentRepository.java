import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//repository katmani: Veritabanı ile ilgili işlemler

public class StudentRepository implements Repository<Student, Integer, String> {

    @Override
    public void cereateTable() {

        JdbcUtils.setConnection();
        JdbcUtils.setStatement();

        try {
            JdbcUtils.statement.execute(
                    "create table if not exists t_student(" +
                            "id serial unique," +
                            "name varchar(50) not null check(length(name)>0)," +
                            "lastName varchar(50) not null check(length(lastName)>0)," +
                            "city varchar(50) not null," +
                            "age Integer not null check(age>0) )"
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                JdbcUtils.statement.close();
                JdbcUtils.connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void showedStudent(Student student) {
        JdbcUtils.setConnection();
        JdbcUtils.setStatement();
        try {
            ResultSet rs = JdbcUtils.statement.executeQuery("SELECT * from t_student where id= (select max (id) from t_student);");
            rs.next();
            System.out.println("--Kaydedilen Ögrenci--");
            //System.out.println((int) (rs.getInt("id")));
            System.out.println("ID :" + rs.getInt("id")); //resultset kismi buid bilgisini getirtmek icin olusturuldu..
            System.out.println("Ad :" + student.getName());
            System.out.println("Soyadi :" + student.getLastName());
            System.out.println("Sehir :" + student.getCity());
            System.out.println("Yas :" + student.getAge());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Student entity) {

        JdbcUtils.setConnection();
        JdbcUtils.setPrst("insert into t_student(name, lastName, city, age) values(?,?,?,?)");
        JdbcUtils.setStatement();

        try {
            JdbcUtils.prst.setString(1, entity.getName());
            JdbcUtils.prst.setString(2, entity.getLastName());
            JdbcUtils.prst.setString(3, entity.getCity());
            JdbcUtils.prst.setInt(4, entity.getAge());
            JdbcUtils.prst.executeUpdate(); // sorguyu çalıştırır.
            System.out.println("öğrenci kaydetme başarılı!!");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                JdbcUtils.statement.close();
                JdbcUtils.connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public List<Student> findall() {
        JdbcUtils.setConnection();
        JdbcUtils.setStatement();

        List<Student> allStudent = new ArrayList<>();

        try {
            ResultSet rs = JdbcUtils.statement.executeQuery("select * from t_student");
            while (rs.next()) {
                Student student = new Student(
                        rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getString("city"),
                        rs.getInt("age"));
                student.setId(rs.getInt("id"));
                allStudent.add(student);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                JdbcUtils.statement.close();
                JdbcUtils.connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        // doldurulmuş olan öğrenci listesini döndürelim.
        return allStudent;
    }


    @Override
    public void update(Student entity, Integer id) {
        JdbcUtils.setConnection();
        JdbcUtils.setPrst("update t_student set name = ?, lastName=?, city=?, age=? where id=?");

        try {
            JdbcUtils.prst.setString(1, entity.getName());
            JdbcUtils.prst.setString(2, entity.getLastName());
            JdbcUtils.prst.setString(3, entity.getCity());
            JdbcUtils.prst.setInt(4, entity.getAge());
            JdbcUtils.prst.setInt(5, id); // id parametre ile gelecek
            int updated = JdbcUtils.prst.executeUpdate(); // sorgu çalıştırıldı ve etkilenen kayıt sayısı alındı.
            if (updated > 0) {
                System.out.println("Güncelleme başarılı");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                JdbcUtils.statement.close();
                JdbcUtils.connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    }

    @Override
    public void deletedById(Integer id) {
        Student deletedStudent = findById(id);
        JdbcUtils.setConnection();
        JdbcUtils.setPrst("delete from t_student where id=?");

        try {
            JdbcUtils.prst.setInt(1, id);
            int deleted = JdbcUtils.prst.executeUpdate();
            if (deleted > 0) {
                System.out.println("silme işlemi başarılı");
                System.out.println("--silinen Ögrenci--");
                System.out.println("ID :" + deletedStudent.getId());
                System.out.println("Ad :" + deletedStudent.getName());
                System.out.println("Soyadi :" + deletedStudent.getLastName());
                System.out.println("Sehir :" + deletedStudent.getCity());
                System.out.println("Yas :" + deletedStudent.getAge());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                JdbcUtils.statement.close();
                JdbcUtils.connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    }

    @Override
    public Student findById(Integer id) {

        JdbcUtils.setConnection();
        JdbcUtils.setPrst("select * from t_student where id=?");
        Student student = null;

        try {
            JdbcUtils.prst.setInt(1, id);
            ResultSet rs = JdbcUtils.prst.executeQuery();
            if (rs.next()) {// eğer kayıt bulunduysa Result set boş değilse, ürn id yanlış girildi.
                student = new Student(rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getString("city"),
                        rs.getInt("age"));

                student.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                JdbcUtils.statement.close();
                JdbcUtils.connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return student; // Bulunan öğrenci objesini veya null değerini return ettik.

    }

    @Override
    public Student findByNameLastName(String nameLastName) {

        JdbcUtils.setConnection();
        String name = nameLastName.split(" ")[0];
        String lastName = nameLastName.split(" ")[1];
        JdbcUtils.setPrst("select * from t_student where name=? and lastName=?");
        Student student = null;

        try {
            JdbcUtils.prst.setString(1, name);
            JdbcUtils.prst.setString(2, lastName);
            ResultSet rs = JdbcUtils.prst.executeQuery();

            while (rs.next()) {// eğer kayıt bulunduysa Result set boş değilse, ürn id yanlış girildi.
                student = new Student(rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getString("city"),
                        rs.getInt("age"));


                student.setId(rs.getInt("id"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                JdbcUtils.statement.close();
                JdbcUtils.connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return student;

    }
}