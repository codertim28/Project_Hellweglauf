package classes;

/**
 * Diese Klasse ist das DataModel für das TableView
 * in dem CompetitionView.
 *
 */
public class CompetitionViewRowData {

    private Chip chip;
	private Round round;
    
    public CompetitionViewRowData(Chip chip, Round round) {
        this.chip = chip;
        this.round = round;
    }
    
    public Chip getChip() { 
    	return chip; 
	}
    
    public Round getRound() { 
    	return round; 
	}
}
