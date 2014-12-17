package com.ledlogic.spacetrivia.loader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class QuizTerm implements Serializable {
	private static final long serialVersionUID = 1L;

	private int level;

	private int sublevel;

	private String definition;

	private String term;

	private List<String> alts;
	
	public QuizTerm() {
	}

	public QuizTerm(int level, int sublevel, String definition, String term, List<String> alts) {
		this.level = level;
		this.sublevel = sublevel;
		this.definition = definition;
		this.term = term;
		this.alts = alts;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSublevel() {
		return sublevel;
	}

	public void setSublevel(int sublevel) {
		this.sublevel = sublevel;
	}

	public List<String> getAlts() {
		return alts;
	}

	public void setAlts(List<String> alts) {
		this.alts = alts;
	}

	/**
	 * @param questions
	 * @return
	 */
	public static List<String[]> toStringArray(List<QuizTerm> questions) {
		// no header assumption
		List<String[]> records = new ArrayList<String[]>();

		// iterate through questions
		for (QuizTerm question: questions) {
			List<String> row = new ArrayList<String>();
			row.add(Integer.toString(question.getLevel()));
			row.add(Integer.toString(question.getSublevel()));
			row.add(question.getDefinition());
			row.add(question.getTerm());
			row.addAll(question.getAlts());
			
			String[] arr = row.toArray(new String[0]);

			records.add(arr);
		}
		return records;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SIMPLE_STYLE);
	}
}