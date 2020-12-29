import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class DFA {

    int nstates;
    int[] accepting;
    int[][] transition;
    char[] alphabet;
    int currentState;


    public boolean execute(String s){
        int state = 0;
        for(int i = 0; i < s.length(); i++){
            try {
                state = transition[state][s.charAt(i)];
//                System.out.println(state);
            }catch (Exception e){
                state = -1;
                break;
            }
        }
        currentState = state;
        return accept();
    }

    public void setTransition(int srcState, char c, int dstState){
        transition[srcState][c] = dstState;
    }

    public boolean accept(){
        for(int n: accepting){
            if(n == currentState){
                return true;
            }
        }
        return false;
    }


    public DFA(int nstates){
        alphabet = new char[128];
        this.nstates = nstates;
        transition = new int[nstates][alphabet.length];
        for(int[] arr: transition){
            Arrays.fill(arr, -1);
        }
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

    public int[][] getTransition() {
        return transition;
    }

    public void setTransition(int[][] transition) {
        this.transition = transition;
    }

    public char[] getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(char[] alphabet) {
        this.alphabet = alphabet;
    }

    public static void main(String[] args){
        int[] accepting = {0};
        DFA dfa = new DFA(4);
        dfa.setAccepting(accepting);
        dfa.setTransition(0, '0', 3);
        dfa.setTransition(0, '1', 1);
        dfa.setTransition(1, '0', 2);
        dfa.setTransition(1, '1', 0);
        dfa.setTransition(2, '0', 1);
        dfa.setTransition(2, '1', 3);
        dfa.setTransition(3, '0', 0);
        dfa.setTransition(3, '1', 2);

    }
}
