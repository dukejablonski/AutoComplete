import java.util.*;

public class HashListAutocomplete implements Autocompletor
{
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights)
    {
        if (terms == null || weights == null)
            throw new NullPointerException("ok");
        initialize(terms, weights);
    }


    @Override
    public List<Term> topMatches(String prefix, int k) {

       if(prefix == null) { throw new NullPointerException("Null pointer");}

       if(!myMap.containsKey(prefix)) {return new ArrayList<Term>();}

       if(k == 0) {return new ArrayList<Term>();}

       if(k < 0 ){throw new IllegalArgumentException("Illegal value of k" + k); }

        List<Term> all = myMap.get(prefix);
        List<Term> list = all.subList(0, Math.min(k, all.size()));
       return list;
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap = new HashMap<String, List<Term>>();
        for(int i=0; i<terms.length;i++){
            String s = terms[i];
            for(int j=0; j<= Math.min(MAX_PREFIX, s.length());j++){

                    String ss = s.substring(j, 0);
                    Term a = new Term(terms[i],weights[i]);
                    myMap.putIfAbsent(ss, new ArrayList<Term>());
                    myMap.get(ss).add(a);
            }
        }

        Comparator daddy = Comparator.comparing(Term::getWeight).reversed();

        for(String key: myMap.keySet()) {
            Collections.sort(myMap.get(key), daddy);
        }
    }

    @Override
    public int sizeInBytes() {

        if(mySize == 0) {

            for (String key : myMap.keySet()) {
                mySize += key.length() * BYTES_PER_CHAR;
                List<Term> keylist = myMap.get(key);

                for (int i = 0; i < keylist.size(); i++) {
                    Term t = keylist.get(i);
                    mySize = mySize + BYTES_PER_CHAR * t.getWord().length() + BYTES_PER_DOUBLE;
                }
            }
        }
    return mySize;
    }

}
