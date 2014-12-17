package com.ledlogic.spacetrivia.loader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import au.com.bytecode.opencsv.CSVWriter;

public class QuizCsvGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		// setup
		String inFileName = "./data/ep-terminology.txt";
		
		int level = 4;
		int sublevel = 1;
		
		// load lines
		File file = new File(inFileName);
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(file, "UTF-8");
		} catch (Exception e) {
			return;
		}
		
		// aggregate lines
		for (int i=lines.size()-1;i>=0;i--) {
			String line = lines.get(i);
			if (!line.startsWith("n ")) {
				lines.remove(i);
				lines.add(i, lines.get(i-1) + line);
				lines.remove(i-1);
			}
		}
		
		// cleanup lines, gather terms array
		List<QuizTerm> terms = new ArrayList<QuizTerm>();
		for (int i=0;i<lines.size();i++) {
			String line = lines.get(i);
			String[] s = line.split(":");
			String term = s[0];
			String definition = s[1];
			
			if (term.startsWith("n ")) {
				term = term.substring(2);
			}
			if (definition.startsWith("n ")) {
				definition = definition.substring(2);
			}
			
			QuizTerm quizTerm = new QuizTerm();
			quizTerm.setLevel(level);
			quizTerm.setSublevel(sublevel);
			quizTerm.setTerm(term);
			quizTerm.setDefinition(definition);
			terms.add(quizTerm);
		}
		
		QuizTermListSelector selector = new QuizTermListSelector(terms);
		
		// get alts
		for (QuizTerm quizTerm: terms) {
			
			// get other quiz terms
			List<QuizTerm> alts = new ArrayList<QuizTerm>();
			while (alts.size() < 2) {
				QuizTerm addTerm = selector.selectQuizTerm();
				if (addTerm != quizTerm && !alts.contains(addTerm)) {
					alts.add(addTerm);
				}
			}
			
			// strings
			List<String> altStrings = new ArrayList<String>();
			for (QuizTerm alt: alts) {
				String altTerm = alt.getTerm();
				altStrings.add(altTerm);
			}
			quizTerm.setAlts(altStrings);
		}
		
		// get csv lines
		List<String[]> quizTermLines = QuizTerm.toStringArray(terms);
		
		// write CSV
		File dirFile = new File("out");
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		// create file writer
		String csvName = "out/questions_eclipse_phase.csv";
		File csvFile = new File(csvName);
		writeLinesToFile(csvFile, quizTermLines);
	}

	private static void writeLinesToFile(File csvFile,
			List<String[]> quizTermLines) throws IOException {
		FileWriter csvFileWriter = new FileWriter(csvFile, false);
		char commaChar = ',';
		char quoteChar = '"';
		CSVWriter writer = new CSVWriter(csvFileWriter, commaChar, quoteChar);
		writer.writeAll(quizTermLines);
		writer.close();
	}

}
