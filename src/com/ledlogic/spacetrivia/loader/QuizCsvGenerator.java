package com.ledlogic.spacetrivia.loader;

import java.io.IOException;

import net.sourceforge.stochunit.selectors.StochSelectorException;

public class QuizCsvGenerator {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		genTerminology();
		genFactions();
		genMorphs();
	}

	private static void genTerminology() throws StochSelectorException,
			IOException {
		// setup
		String inFileName = "./data/ep-terminology.txt";
		String outFileName = "out/questions_eclipse_phase_core_glossary.csv";

		int level = 1;
		int sublevel = 1;

		QuizFileService quizFileService = new QuizFileService();
		quizFileService.loadTextToCsvFile(inFileName, outFileName, level,
				sublevel, true);
	}

	private static void genFactions() throws StochSelectorException,
			IOException {
		// setup
		String inFileName = "./data/ep-factions.txt";
		String outFileName = "out/questions_eclipse_phase_factions.csv";

		int level = 3;
		int sublevel = 1;

		QuizFileService quizFileService = new QuizFileService();
		quizFileService.loadTextToCsvFile(inFileName, outFileName, level,
				sublevel, false);
	}

	private static void genMorphs() throws StochSelectorException, IOException {
		// setup
		String inFileName = "./data/ep-morphs.csv";
		String outFileName = "out/questions_eclipse_phase_morphs.csv";

		int level = 4;
		int sublevel = 1;

		QuizFileService quizFileService = new QuizFileService();
		quizFileService.loadCsvToCsvFile(inFileName, outFileName, level,
				sublevel);
	}
}
