package fuzzy;

import DP.LevenisteinDistance;
import lookahead.Trie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FuzzyNameMatch {


    Trie fnameTrie = new Trie();


    Trie lnameTrie = new Trie();

    NameRecordDB nameRecordDB = new NameRecordDB();


    List<NameRecord> getMatches(String Fname , String Lname)
    {
        String fname = Fname.toLowerCase();
        String lname = Lname.toLowerCase();

        List<NameRecord> result ;


        List<String> fnames = Trie.allWordsStartingWith(fname.substring(0,1),fnameTrie).stream().filter(f-> LevenisteinDistance.editDistance(f,fname)<=2).collect(Collectors.toList());

        List<String> lnames = Trie.allWordsStartingWith(lname.substring(0,1),lnameTrie).stream().filter(l->LevenisteinDistance.editDistance(l,lname)<=2).collect(Collectors.toList());

        List<NameRecord> possibilities = new ArrayList<>();
        fnames.forEach(f-> {
            lnames.forEach(l->{

                NameRecord nr = new NameRecord(f,l);

                possibilities.add(nr);

            });

        } );


        result = possibilities.stream().filter(poss-> {
            NameRecord nr = new NameRecord(poss.fname,poss.lname);
            return nameRecordDB.contains(nr);
        }).collect(Collectors.toList());




        return result;

    }


    public void add(String fname , String lname)
    {
        fname = fname.toLowerCase();
        lname = lname.toLowerCase();

        NameRecord nameRecord = new NameRecord(fname,lname);

        nameRecordDB.add(nameRecord);

        fnameTrie.insert(fname);
        lnameTrie.insert(lname);
    }




}
