import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class StudentService {
    //service katmani (business) mantıksal işlemler, kontroller


    private Scanner input = StudentController.inp;

    private Repository repository = new StudentRepository();

    public void createTable() {

        repository.cereateTable();
    }

    public Student getStudentInfo() {
        System.out.println("AD : ");
        String name = input.nextLine();

        System.out.println("SOY AD : ");
        String lastName = input.nextLine();

        System.out.println("ŞEHİR : ");
        String city = input.nextLine();

        System.out.println("YAŞ : ");
        int age = input.nextInt();


        return new Student(name, lastName, city, age);
    }

    public void saveStudent(Student newStudent) {


        repository.save(newStudent);
    }

    public void printAllStudent() {

        List<Student> studentList = repository.findall();
        System.out.println("-------TÜM ÖĞRENCİLER--------");
        for (Student s : studentList) {
            System.out.println("id : " + s.getId() +
                    " adı : " + s.getName() +
                    " soy adı : " + s.getLastName() +
                    " şehir : " + s.getCity() +
                    " yaş : " + s.getAge());
        }

    }


    public Student getStudentById(int id) {
        Student student = (Student) repository.findById(id);

        if (student == null) {
            System.out.println("id si verilen öğrenci bulunamadı");
        }
        return student;
    }

    public Student getStudentByName(String nameLastName) {
        Student student = (Student) repository.findByNameLastName(nameLastName);

        if (student == null) {
            System.out.println("ismi verilen öğrenci bulunamadı");
        }
        return student;
    }


    public void printStudentById(int id) {
        Student foundStudent = getStudentById(id);
        if (foundStudent != null) {
            System.out.println(foundStudent);
        }
    }

    public void printStudentByNameLastName(String nameLastName) {
        Student foundStudent = getStudentByName(nameLastName);
        if (foundStudent != null) {
            System.out.println(foundStudent);
        }
    }

    public void updateStudent(int id) {
        Student found = getStudentById(id);// girilen id li öğrenci varmı diye kontrol ettik.

        if (found != null) {
            Student newStudent = getStudentInfo();// kullanıcıdan yeni bilgileri aldık.
            repository.update(newStudent, id);// güncelleme işlemini id ile yaptık.
        }

    }

    public void deleteStudent(int id) {
        getStudentById(id); // öğrencinin gerçekten var olup olmadığını kontro ettik.
        repository.deletedById(id);
    }

    public void generateReport() {

        List<Student> allStudent = repository.findall();
        System.err.println("Rapor oluşturuluyor.");

        try {
            Thread.sleep(10000);

            FileWriter writer = new FileWriter("student_report.txt");
            writer.write("*** Student Report ***\n");
            writer.write("-----------------------\n");

            for (Student student : allStudent) {
                writer.write("Ad: " + student.getName() + " SoyAd: " + student.getLastName() + "\n");
            }
            writer.close();


        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        System.err.println("Rapor oluşturuldu ve student_report.txt dosyasına yazıldı...");
    }


}
