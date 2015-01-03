package com.ledlogic.spacetrivia.loader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stochunit.selectors.StochSelectorException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class QuizFileService {
	public void loadTextToCsvFile(String inFileName, String outFileName,
			int level, int sublevel, boolean aggregate)
			throws StochSelectorException, IOException {
		// load lines
		File file = new File(inFileName);
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(file, "UTF-8");
		} catch (Exception e) {
			return;
		}

		// aggregate lines
		if (aggregate) {
			for (int i = lines.size() - 1; i >= 0; i--) {
				String line = lines.get(i);
				if (!line.startsWith("n ")) {
					lines.remove(i);
					lines.add(i, lines.get(i - 1) + " " + line);
					lines.remove(i - 1);
				}
			}
		}

		// cleanup lines, gather terms array
		List<QuizTerm> terms = new ArrayList<QuizTerm>();
		for (int i = 0; i < lines.size(); i++) {
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

			term = term.trim();
			definition = definition.trim();

			QuizTerm quizTerm = createQuizTerm(level, sublevel, term,
					definition);
			terms.add(quizTerm);
		}

		getAlts(terms);

		// create file writer
		File csvFile = new File(outFileName);
		writeLinesToFile(csvFile, terms);
	}

	public void loadCsvToCsvFile(String inFileName, String outFileName,
			int level, int sublevel) throws StochSelectorException, IOException {
		// load lines
		FileReader fileReader = new FileReader(inFileName);
		CSVReader reader = new CSVReader(fileReader);

		List<QuizTerm> terms = new ArrayList<QuizTerm>();

		// read header row
		String[] nextLine = reader.readNext();

		// read data rows
		while ((nextLine = reader.readNext()) != null) {
			String term = nextLine[2].trim();
			String definition = nextLine[5].trim();

			// t1: remove "Mentons are" prefixes
			String p1 = term + " are ";
			int i1 = StringUtils.indexOfIgnoreCase(definition, p1);
			if (i1 == 0) {
				definition = definition.substring(p1.length()).trim();
				definition = StringUtils.capitalize(definition);
			}

			// t2: remove "The remade are" prefixes
			String p2 = "The " + term + "are";
			int i2 = StringUtils.indexOfIgnoreCase(definition, p2);
			if (i2 == 0) {
				definition = definition.substring(p2.length()).trim();
				definition = StringUtils.capitalize(definition);
			}

			// t3: remove "Sylph morphs are" prefixes
			String p3 = term + " morphs are";
			int i3 = StringUtils.indexOfIgnoreCase(definition, p3);
			if (i3 == 0) {
				definition = definition.substring(p3.length()).trim();
				definition = StringUtils.capitalize(definition);
			}

			// t4: remove "Splicers are " prefixes
			String p4 = term + "s are";
			int i4 = StringUtils.indexOfIgnoreCase(definition, p4);
			if (i4 == 0) {
				definition = definition.substring(p4.length()).trim();
				definition = StringUtils.capitalize(definition);
			}

			// t5: remove "Neo-avians include" prefixes
			String p5 = term + "s include";
			int i5 = StringUtils.indexOfIgnoreCase(definition, p5);
			if (i5 == 0) {
				definition = definition.substring(p5.length()).trim();
				definition = StringUtils.capitalize(definition);
			}

			QuizTerm quizTerm = createQuizTerm(level, sublevel, term,
					definition);
			terms.add(quizTerm);
		}

		reader.close();

		getAlts(terms);

		// create file writer
		File csvFile = new File(outFileName);
		writeLinesToFile(csvFile, terms);
	}

	private QuizTerm createQuizTerm(int level, int sublevel, String term,
			String definition) {
		QuizTerm quizTerm = new QuizTerm();
		quizTerm.setLevel(level);
		quizTerm.setSublevel(sublevel);
		quizTerm.setTerm(term);
		quizTerm.setDefinition(definition);
		return quizTerm;
	}

	private void getAlts(List<QuizTerm> terms) throws StochSelectorException {
		QuizTermListSelector selector = new QuizTermListSelector(terms);

		// get alts
		for (QuizTerm quizTerm : terms) {

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
			for (QuizTerm alt : alts) {
				String altTerm = alt.getTerm();
				altStrings.add(altTerm);
			}
			quizTerm.setAlts(altStrings);
		}
	}

	public void writeLinesToFile(File csvFile, List<QuizTerm> terms)
			throws IOException {

		// get csv lines
		List<String[]> lines = QuizTerm.toStringArray(terms);

		// write CSV
		File dirFile = new File("out");
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		FileWriter csvFileWriter = new FileWriter(csvFile, false);
		char commaChar = ',';
		char quoteChar = '"';
		CSVWriter writer = new CSVWriter(csvFileWriter, commaChar, quoteChar);
		writer.writeAll(lines);
		writer.close();
	}
}