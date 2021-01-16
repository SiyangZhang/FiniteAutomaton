import javafx.util.Pair;

import java.util.*;

public class NFA{

    int nstates;
    int[] accepting;
    int initialState;
    Set<Integer>[][] transition;
    char[] alphabet;
    Set<Integer> currentState;

    Set<Character> alphabets;
    public NFA(int nstates){
        this.nstates = nstates;
        alphabet = new char[128];
        alphabets = new HashSet<>();
        initialState = 0;
        transition = new Set[nstates][alphabet.length];
        for(Set<Integer>[] arr: transition){
            Arrays.fill(arr, new HashSet<>());
        }
    }

    public void setTransition(int srcState, char c, int dstState){
        if(!transition[srcState][c].isEmpty()){
            transition[srcState][c].add(dstState);
        }else{
            transition[srcState][c] = new HashSet<>();
            transition[srcState][c].add(dstState);
        }
        alphabets.add(c);
    }

    public boolean execute(String s){
        Set<Integer> set = new HashSet<>();
        set.add(initialState);
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            Set<Integer> result = new HashSet<>();
            for(int state: set){
                result.addAll(transition[state][c]);
            }
            set = result;
            currentState = set;
        }
        return accept();
    }

    public boolean accept(){
        for(int n: accepting){
            if(currentState.contains(n)){
                return true;
            }
        }
        return false;
    }

    public int getNstates() {
        return nstates;
    }

    public void setNstates(int nstates) {
        this.nstates = nstates;
    }

    public int[] getAccepting() {
        return accepting;
    }

    public void setAccepting(int[] accepting) {
        this.accepting = accepting;
    }

    public Set[][] getTransition() {
        return transition;
    }

    public void setTransition(Set[][] transition) {
        this.transition = transition;
    }

    public char[] getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(char[] alphabet) {
        this.alphabet = alphabet;
    }

    public static DFA NFAtoDFA(NFA nfa){
        Map<Set<Integer>, Integer> map = new HashMap<>();

        Set<Set<Integer>> buffer = new HashSet<>();

        Set<Integer> init = new HashSet<>();
        init.add(nfa.initialState);
        map.put(init, map.size());
        buffer.add(init);

        Set<Set<Integer>> marker = new HashSet<>();

        for(Set<Integer> state : buffer){
            marker.add(state);
            for(char c: nfa.alphabets) {
                Set<Integer> result = new HashSet<>();
                for(int n: state){
                    result.addAll(nfa.transition[n][c]);
                    if(!map.containsKey(result)){
                        map.put(result, map.size());
                        marker.add(result);
                    }
                }
            }
        }

        Map<Pair<Set<Integer>, Character>, Set<Integer>> records = new HashMap<>();
        while(!marker.isEmpty()){

            buffer = new HashSet<>();
            buffer.addAll(marker);
            for(Set<Integer> state: buffer){
                String s = state.toString();
                for(char c: nfa.alphabets){
                    Set<Integer> result = new HashSet<>();
                    for(int n: state){
                        result.addAll(nfa.transition[n][c]);
                    }

                    if(!map.containsKey(result)) {
                        marker.add(result);
                    }
                    System.out.println("(" + s + ", " + c + ") = " + result);
                    Pair<Set<Integer>, Character> pair = new Pair(state, c);
                    records.put(pair, result);
                }

            }


            for(Set<Integer> key: map.keySet()){
                marker.remove(key);
            }

            for(Set<Integer> result: marker){
                if(!map.containsKey(result)){
                    map.put(result, map.size());
                }
            }
        }

        DFA dfa = new DFA(map.size());

        for(Map.Entry<Pair<Set<Integer>, Character>, Set<Integer>> entry: records.entrySet()){
            Pair<Set<Integer>, Character> pair = entry.getKey();

            int srcState = map.get(pair.getKey());
            char c = pair.getValue();
            int dstState = map.get(records.get(pair));
            dfa.setTransition(srcState, c, dstState);
        }

        List<Integer> accept = new ArrayList<>();
        for(Set<Integer> set: map.keySet()){
            for(int n: nfa.getAccepting()){
                if(set.contains(n)){
                    accept.add(map.get(set));
                }
            }
        }

        int[] accepting = new int[accept.size()];
        for(int i = 0; i < accept.size(); i++){
            accepting[i] = accept.get(i);
        }
        dfa.setAccepting(accepting);
        return dfa;
    }

    public static void main(String[] args){
        NFA nfa = new NFA(3);
        nfa.setAccepting(new int[]{2});
        nfa.setTransition(0, '0', 0);
        nfa.setTransition(0, '0', 1);
        nfa.setTransition(0, '1', 0);
        nfa.setTransition(1, '1', 2);

        DFA dfa = NFAtoDFA(nfa);
        System.out.println(dfa.nstates);
        String[] ss = {"0101", "0110", "1101", "100110101", "01"};
        for(String s: ss){
            System.out.println(dfa.execute(s));
        }
    }

}
