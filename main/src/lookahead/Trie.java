package lookahead;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Trie {

    boolean end = false;

    Trie[] tries = new Trie[26];

    public void insert(String str) {

        char c = str.charAt(0);
        int pos = (int)c-97;

        Trie current = tries[pos];
        if (current==null)
        {
            tries[pos] = new Trie();
        }

        if (str.length()>1)
        {
            tries[pos].insert(str.substring(1));
        }
        else
        {
            tries[pos].end=true;   // a word ends here.
            return;
        }

    }


    public static boolean exists(String word , Trie start)
    {
        int len = word.length();

        Trie curr = start;

        char[] buf = word.toCharArray();

        for (int i=0;i<len;i++)
        {
            int pos = (int)buf[i]-97;
            if (pos<0 || pos>=26)
            {
                return false;
            }
            if (curr.tries[pos]!=null)
            {
                curr = curr.tries[pos];
                continue;
            }
            else
            {
                return false;
            }
        }

        return true;
    }


    static class Tuple
    {
        Trie trie;
        String word;

        public Tuple(Trie trie, String word)
        {
            this.trie = trie;
            this.word = word;
        }
    }
   public static List<String> allWordsStartingWith(String partial, Trie root)
    {

        List<String> words = new ArrayList<>();
        Trie start = startTrie(partial,root);

        if (start==null)
            return words;

        Tuple tuple = new Tuple(start,partial);
        Queue<Tuple> queue = new ArrayDeque<>();
        queue.add(tuple);

        while(!queue.isEmpty())
        {
            Tuple curr = queue.remove();


            if (curr.trie.end)
            {
                words.add(curr.word);
            }


            Trie[] tries = curr.trie.tries;
            for (int i=0;i<curr.trie.tries.length;i++)
            {
                if (tries[i]!=null)
                {
                    char c =(char) (97+i);
                    String word = curr.word+c;
                    queue.add(new Tuple(tries[i],word));
                }

            }

        }

        return words;

    }

    private static Trie startTrie(String word, Trie start)
    {
        int len = word.length();

        Trie curr = start;

        char[] buf = word.toCharArray();

        for (int i=0;i<len;i++)
        {
            int pos = (int)buf[i]-97;
            if (pos<0 || pos>=26)
            {
                return null;
            }
            if (curr.tries[pos]!=null)
            {
                curr = curr.tries[pos];
                continue;
            }
            else
            {
                return null;
            }
        }

        return curr;

    }
}
