import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WinningTeam {


    public static void main(String[] args) {

        test1();
        test2();
        test3();

    }

    public static void test1()
    {
        Set<Match> matches = new HashSet<>();

        matches.add(new Match("A","C"));
        matches.add(new Match("C","D"));
        matches.add(new Match("D","A"));
        matches.add(new Match("D","B"));

        System.out.println("Test1 " + didTeamWin("A","B",matches));
    }

    public static void test2()
    {
        Set<Match> matches = new HashSet<>();

        matches.add(new Match("B","A"));
        matches.add(new Match("A","C"));
        matches.add(new Match("C","D"));
        matches.add(new Match("B","D"));
        System.out.println("Test2 " + didTeamWin("A","B",matches));


    }

    public static void test3()
    {
        Set<Match> matches = new HashSet<>();

        matches.add(new Match("A","C"));
        matches.add(new Match("C","D"));
        matches.add(new Match("D","E"));
        System.out.println("Test3 " + didTeamWin("A","B",matches));

    }

    //assume win and lose team are different .
    public static boolean didTeamWin(String winTeam, String loseTeam, Set<Match> matches)
    {
        Map<String,Set<String>> graph = buildGraph(matches);
        Set<String> visited = new HashSet<>();
        return search(winTeam,graph.get(winTeam),visited,loseTeam,graph);

    }

    private static boolean search(String start,Set<String> vertices, Set<String> visited,String target ,  Map<String,Set<String>> graph)
    {
        if (visited.contains(start))
            return false;
        visited.add(start);

        if (vertices==null)
            return false;

        for (String vertex : vertices)
        {

            if (vertex.equals(target))
                return true;
            if (search(vertex,graph.get(vertex),visited,target,graph))
                return true;
        }

        return false;
    }


    private static Map<String,Set<String>> buildGraph(Set<Match> matches)
    {
        Map<String,Set<String>> graph = new HashMap<>();
        for (Match match : matches)
        {
            Set<String> vertices = graph.get(match.getTeamWon());
            if (vertices==null)
            {
                vertices = new HashSet<>();
                graph.put(match.getTeamWon(),vertices);
            }

            vertices.add(match.getTeamLost());
        }

        return graph;
    }











}


class Match
{
    String teamWon;
    String teamLost;

    public Match(String teamWon, String teamLost) {
        this.teamWon = teamWon;
        this.teamLost = teamLost;
    }

    public String getTeamWon() {
        return teamWon;
    }

    public void setTeamWon(String teamWon) {
        this.teamWon = teamWon;
    }

    public String getTeamLost() {
        return teamLost;
    }

    public void setTeamLost(String teamLost) {
        this.teamLost = teamLost;
    }
}