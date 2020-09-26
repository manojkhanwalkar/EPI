package ks;

import javax.print.attribute.HashAttributeSet;
import java.util.*;
import java.util.stream.Collectors;

public class KSGraph {

    Map<String,Person> persons = new HashMap<>();

    Map<String,Word> words = new HashMap<>();


    public void addPersonPersonRelation(String from , String to)
    {
       Person fromPerson =  persons.computeIfAbsent(from,f->new Person(from));

       Person toPerson = persons.computeIfAbsent(to,t->new Person(to));

       fromPerson.add(toPerson);
       toPerson.add(fromPerson);
    }


    public void addPersonWordRelationship(String from , String wordStr)
    {
        Person fromPerson =  persons.computeIfAbsent(from,f->new Person(from));
        Word word = words.computeIfAbsent(wordStr,w->new Word(w));

        word.add(fromPerson);
        fromPerson.add(word);

    }


    public List<String> whom(String name)
    {

        Person person = persons.get(name);
        if (person!=null)
        {
            List<String> results = person.persons.keySet().stream().map(p->p.emailid).collect(Collectors.toList());
            return results;
        }
        else
        {
            return null;
        }
    }

    public List<String> topNPersons(String name, int count)
    {

        Person person = persons.get(name);
        if (person!=null)
        {
            List<String> results = person.persons.entrySet().stream().sorted((e1,e2)->{return Integer.compare(e2.getValue(),e1.getValue());})
                    .limit(count)
                    .map(entry->entry.getKey().emailid).collect(Collectors.toList());

         /*   List<String> results = person.persons.entrySet().stream().sorted(Map.Entry<Person,Integer>::comparingByValue().reversed())
                    .limit(count)
                    .map(entry->entry.getKey().emailid).collect(Collectors.toList());*/


            return results;
        }
        else
        {
            return null;
        }
    }


    public List<String> topNAreasOfExpertise(String name, int count)
    {
        Person person = persons.get(name);
        if (person!=null)
        {
            List<String> results = person.words.entrySet().stream().sorted((e1,e2)->{return Integer.compare(e2.getValue(),e1.getValue());})
                    .limit(count)
                    .map(entry->entry.getKey().word).collect(Collectors.toList());

         /*   List<String> results = person.persons.entrySet().stream().sorted(Map.Entry<Person,Integer>::comparingByValue().reversed())
                    .limit(count)
                    .map(entry->entry.getKey().emailid).collect(Collectors.toList());*/


            return results;
        }
        else
        {
            return null;
        }
    }


    public List<String> topNExperts(String name, int count)
    {
        Word word = words.get(name);
        if (word!=null)
        {
            List<String> results = word.persons.entrySet().stream().sorted((e1,e2)->{return Integer.compare(e2.getValue(),e1.getValue());})
                    .limit(count)
                    .map(entry->entry.getKey().emailid).collect(Collectors.toList());



            return results;
        }
        else
        {
            return null;
        }
    }


    public boolean doesP1knowP2(String name1, String name2)
    {
        Set<Person> visited = new HashSet<>();
        Queue<Person> process = new ArrayDeque<>();

        Person p1 = persons.get(name1);
        process.add(p1);

        while(!process.isEmpty())
        {

            Person curr = process.remove();
            if (curr.emailid.equalsIgnoreCase(name2))
                return true;

            if (visited.contains(curr))
                continue;
            curr.persons.keySet().stream().forEach(p->process.add(p));

            visited.add(curr);
        }

        return false;
    }


    @Override
    public String toString() {
        return "KSGraph{" +
                "persons=" + persons +
                ", words=" + words +
                '}';
    }

    public static void main(String[] args) {
        KSGraph ksGraph = new KSGraph();

        FileProcessor fileProcessor = new FileProcessor(ksGraph);

        fileProcessor.processPersons();
        fileProcessor.processWords();

        System.out.println(ksGraph);

        System.out.println(ksGraph.whom("P1"));

        System.out.println(ksGraph.topNPersons("P1",2));

        System.out.println(ksGraph.topNAreasOfExpertise("P5",2));

        System.out.println(ksGraph.topNExperts("W5",2));

        System.out.println(ksGraph.doesP1knowP2("P1" , "P7"));

    }
}
