package sisairis7;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;


public class Student {

    private int id;
    private String name;
    private double mark;

    public Student() {
        this.id = 0;
        this.name = "NoName";
        this.mark = 0.0;
    }

    public Student(int id, String name, double mark) {
        this.id = id;
        this.name = name;
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student N" + id + ". Name: "
                + name + ". Mark: " + mark + ".";
    }

    public void parse(Element nNode) {

        setId( Integer.parseInt(nNode.getAttribute("id")));
        setName(nNode.getElementsByTagName("name").item(0).getTextContent());
        setMark(Double.parseDouble(nNode.getElementsByTagName("mark").item(0).getTextContent()));

    }

    public Element toElement(Document doc) throws ParserConfigurationException {
        Element element = doc.createElement("student");

        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(this.getId()));
        element.setAttributeNode(attr);

        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(this.getName()));
        element.appendChild(name);

        Element mark = doc.createElement("mark");
        mark.appendChild(doc.createTextNode(String.valueOf(this.getMark())));
        element.appendChild(mark);

        return element;
    }

}