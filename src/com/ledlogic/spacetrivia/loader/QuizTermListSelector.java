package com.ledlogic.spacetrivia.loader;

import java.util.List;

import net.sourceforge.stochunit.selectors.StochListSelector;
import net.sourceforge.stochunit.selectors.StochSelectorException;

public class QuizTermListSelector extends StochListSelector {
	
	public QuizTermListSelector(List<QuizTerm> list) {
		super(list);
	}
	
	public QuizTerm selectQuizTerm() throws StochSelectorException {
		return (QuizTerm)select();
	}
}