package sisairis7;


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;


public class Students {

    private List<Student> students;

    public Students() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        int index = students.indexOf(student);
        if (index != -1) students.remove(index);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void parse(NodeList university) {

        for (int index = 0; index < university.getLength(); index++) {

            Node studentElement = university.item(index);

            Student student = new Student();

            if (studentElement.getNodeType() == Node.ELEMENT_NODE) {

                student.parse((Element) studentElement);

                students.add(student);

                System.out.print("Parse student:" + student);

            }

        }
    }
}