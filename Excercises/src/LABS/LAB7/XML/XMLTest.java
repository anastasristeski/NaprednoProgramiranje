package LABS.LAB7.XML;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
class XMLLeaf implements XMLComponent {
    private final String tag;
    private final String value;
    private final Map<String, String> attributes;

    public XMLLeaf(String tag, String value) {
        this.tag = tag;
        this.value = value;
        this.attributes = new LinkedHashMap<>();
    }

    @Override
    public String getType() {
        return "LEAF";
    }

    @Override
    public void addAttribute(String attribute, String value) {
        attributes.putIfAbsent(attribute, value);
    }

    @Override
    public String toString() {
        return String.format("<%s%s%s>%s</%s>", tag, attributes.size() != 0 ? " " : "", attributes.entrySet().stream().map(i -> String.format("%s=\"%s\"", i.getKey(), i.getValue())).collect(Collectors.joining(" ")), value, tag);
    }
}

interface XMLComponent {
    void addAttribute(String attribute, String value);
    String getType();
}
class XMLComposite implements XMLComponent {
    private final String tag;
    private final Map<String, String> attributes;
    private final List<XMLComponent> components;

    public XMLComposite(String tag) {
        this.tag = tag;
        this.attributes = new LinkedHashMap<>();
        this.components = new ArrayList<>();
    }

    @Override
    public String getType() {
        return "COMPOSITE";
    }

    @Override
    public void addAttribute(String attribute, String value) {
        attributes.putIfAbsent(attribute, value);
    }

    public void addComponent(XMLComponent component) {
        components.add(component);
    }

    private String toStringRecursive(XMLComposite xml, int level) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        sb.append(String.format("<%s%s%s>%n", xml.tag, attributes.size() != 0 ? " " : "", xml.attributes.entrySet().stream().map(i -> String.format("%s=\"%s\"", i.getKey(), i.getValue())).collect(Collectors.joining(" "))));

        for (XMLComponent component : xml.components) {
            if (component.getType().equals("LEAF")) {
                for (int i = 0; i < level; i++) {
                    sb.append("\t");
                }
                sb.append("\t");
                sb.append(component).append("\n");
            } else if (component.getType().equals("COMPOSITE")) {
                sb.append(toStringRecursive((XMLComposite) component, level + 1)).append("\n");
            }
        }

        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        sb.append(String.format("</%s>", xml.tag));

        return sb.toString();
    }

    @Override
    public String toString() {
        return toStringRecursive(this, 0);
    }
}



public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase==1) {
            System.out.println(component);
        } else if (testCase == 2) {
            System.out.println(composite);
        } else if (testCase==3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level","1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level","2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level","3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));
            System.out.println(main);
            //TODO print the main object
        }
    }
}
