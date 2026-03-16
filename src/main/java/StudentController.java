import javax.lang.model.element.Name;
import java.util.Scanner;

public class StudentController {

    //Controller, kullanıcıyla iletişim kurar ve istekleri sadece Service sınıfına iletir


    public static Scanner inp=new Scanner(System.in);

    public static void main(String[] args) {
        start();
    }

    private static void start() {

        StudentService service=new StudentService();

        service.createTable();

        int select;
        int id;
        String nameLastName;


        do{
            System.out.println("======================================");
            System.out.println("Öğrenci Yönetim Paneli");
            System.out.println("1- Öğrenci kaydetme");
            System.out.println("2- tüm Öğrencileri görüntüleme");
            System.out.println("3- Öğrenciyi güncelleme");
            System.out.println("4- Öğrenciyi silme");
            System.out.println("5- İd yegöre bir Öğrenciyi Görüntüleme");
            System.out.println("6- İsime göre bir Öğrenciyi Görüntüleme");
            System.out.println("7- Tüm Öğrencilerin ad SoyAd bilgilerini rapora yazdırma");
            System.out.println("0- ÇIKIŞ");
            System.out.println(" İşlem seçiniz : ");
            select =inp.nextInt(); // kullanıcının seçimini okuduk select e yükledik.
            inp.nextLine();// yeni satır karekterini temizledik.


            switch (select){
                case 1:
                    Student newStudent=service.getStudentInfo();
                    service.saveStudent(newStudent);
                    StudentRepository show=new StudentRepository();
                    show.showedStudent(newStudent);


                    break;
                case 2:
                    service.printAllStudent();
                    break;
                case 3:

                    id=getIdInfo();
                    service.updateStudent(id);
                    break;
                case 4:

                    id=getIdInfo();
                    service.deleteStudent(id);
                    break;
                case 5:

                    id=getIdInfo();
                    service.printStudentById(id);
                    break;
                case 6 :
                     nameLastName =getNameInfo();
                     service.printStudentByNameLastName(nameLastName);
                     break;

                case 7:

                    new  Thread(()->{
                        service.generateReport();
                    }).start();
                    break;
                case 0:
                    System.out.println("iyi günler.....");
                    break;
                default:
                    System.out.println("hatalı giriş!!!");
                    break;




            }


        }while (select!=0);


    }

    //------- helper methot
    private static int getIdInfo(){
        System.out.println("öğrenci id");
        int id=inp.nextInt();
        inp.nextLine();
        return id;
    }
    private static String getNameInfo() {
        System.out.println("öğrenci isimi : ");
        String name = inp.next();
        System.out.println("öğrenci Soyisimi : ");
        String lastName = inp.next();
        String nameLastName=name+" "+lastName;
        inp.nextLine();

        return nameLastName;
    }
}
