package classes.controller;

import java.io.IOException;

import classes.Constants;
import classes.Data;
import classes.model.Competition;
import classes.repository.CompetitionRepository;

public class CompetitionController extends Controller {
	
	private Competition competition;
	private CompetitionRepository competitionRepository;
	
	private ChipsController chipsController;
	
	public CompetitionController() throws IOException {
		setCompetitionRepository(new CompetitionRepository(Constants.competitionFilePath()));
		setCompetition(competitionRepository.read());
		
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
	
	// GETTER & SETTER
	public void setPath(String path) {
		competitionRepository.setPath(path);
		chipsController.getRepository().setPath(path.replaceAll(".xml", ".chips.xml"));
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
		String path = competitionRepository.getPath();
		chipsController = new ChipsController(path.replaceAll(".xml", ".chips.xml"));
	}
}
