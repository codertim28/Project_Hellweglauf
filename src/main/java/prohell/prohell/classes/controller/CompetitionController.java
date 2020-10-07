package prohell.prohell.classes.controller;

import java.io.File;
import java.io.IOException;

import prohell.prohell.classes.Constants;
import prohell.prohell.classes.model.Competition;
import prohell.prohell.classes.repository.CompetitionRepository;

public class CompetitionController extends Controller {
	
	private Competition competition;
	private CompetitionRepository competitionRepository;
	
	private ChipsController chipsController;
	
	public CompetitionController() throws IOException {

		setCompetitionRepository(new CompetitionRepository(Constants.COMPETITION_FILE_PATH));

		if(!new File(Constants.COMPETITION_FILE_PATH).exists()) {
			reset();
		}
		else {
			setCompetition(competitionRepository.read());
		}

		initChipsController();
		chipsController.load();
	}
	
	public CompetitionController(Competition comp, CompetitionRepository compRepo) {
		setCompetitionRepository(compRepo);
		setCompetition(comp);
		
		initChipsController();
		chipsController.load();
	}

	public boolean save() {
		chipsController.save(); // -> Thread
		return competitionRepository.write(competition);
	}
	
	public boolean saveSync() {
		return chipsController.saveSync() &
			competitionRepository.write(competition);
	}
	
	public void load() {
		try {
			competition = competitionRepository.read();
			chipsController.load();
		} catch (IOException e) {
			log.error(e);
		}
	}

	public void reset() {
		Competition newCompetition = Competition.fromProperties();
		if(competition != null) {
			newCompetition.setType(competition.getType());
		}
		setCompetition(newCompetition);
	}
	
	// GETTER & SETTER
	public void setPath(String path) {
		competitionRepository.setPath(path);
		//chipsController.getRepository().setPath(path.replaceAll(".csv", ".chips.csv"));
	}
	
	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public CompetitionRepository getCompetitionRepository() {
		return competitionRepository;
	}

	public void setCompetitionRepository(CompetitionRepository competitionRepository) {
		this.competitionRepository = competitionRepository;
	}

	public ChipsController getChipsController() {
		return chipsController;
	}

	public void setChipsController(ChipsController chipsController) {
		this.chipsController = chipsController;
	}
	
	private void initChipsController() {
		chipsController = new ChipsController(Constants.CHIPS_FILE_PATH);
	}
}
