import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.util.automata.builders.AutomatonBuilders;
import net.automatalib.util.automata.builders.DFABuilder;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;
import net.automatalib.words.impl.ListAlphabet;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDFA<I> {
    private CompactDFA<I> dfa;
    private Object Arrays;

    public ProductDFA( CompactDFA<I> dfa1) {
        this.dfa = dfa1;
    }

    public ProductDFA<I> interleaving_parallel_composition(CompactDFA<I> dfa2){
        I[] sigma1 = (I[]) this.dfa.getInputAlphabet().toArray();
        I[] sigma2 = (I[]) dfa2.getInputAlphabet().toArray();

        I[] sigma = (I[]) Array.newInstance(sigma1.getClass().getComponentType(),
                sigma1.length + sigma2.length);
        System.arraycopy(sigma1, 0, sigma, 0, sigma1.length);
        System.arraycopy(sigma2, 0, sigma, sigma1.length, sigma2.length);
        Alphabet<I> alph = Alphabets.fromArray(sigma);

        Map<String,Integer> states = new HashMap<String,Integer>();
        Integer si=null,sf=null;
        boolean accepting = false;


        String initial_label = Integer.toString(dfa.getInitialState()) + "_" + Integer.toString(dfa2.getInitialState());

        CompactDFA<I> productDFA = new CompactDFA<I>(alph);

        for(Integer s1:this.dfa.getStates()){
            accepting = dfa.isAccepting(s1);
            for (Integer s2:dfa2.getStates()) {
                accepting = accepting || dfa2.isAccepting(s2);
                String s_label = Integer.toString(s1) + "_" + Integer.toString(s2);
                if(!states.containsKey(s_label)) states.put(s_label, productDFA.addState());

                si = states.get(s_label);
                productDFA.setAccepting(si, accepting);

                for (I action : sigma1) {
                    Integer tgt = this.dfa.getTransition(s1, action);
                    String tgt_label = Integer.toString(tgt) + "_" + Integer.toString(s2);
                    if(!states.containsKey(tgt_label)) states.put(tgt_label, productDFA.addState());
                    sf = states.get(tgt_label);
                    productDFA.addTransition(si, action, sf, null);

                }
                for (I action : sigma2) {
                    Integer tgt = dfa2.getTransition(s2, action);
                    String tgt_label = Integer.toString(s1) + "_" + Integer.toString(tgt);

                    if(!states.containsKey(tgt_label)) states.put(tgt_label, productDFA.addState());
                    sf = states.get(tgt_label);
                    productDFA.addTransition(si, action, sf, null);
                }
            }

        }

        Integer s0 = states.get(initial_label);
        productDFA.setInitialState(s0);
        this.setDfa(productDFA);
        return this;
    }

    public CompactDFA<I> getDfa() {
        return dfa;
    }

    public void setDfa(CompactDFA<I> dfa) {
        this.dfa = dfa;
    }
}
