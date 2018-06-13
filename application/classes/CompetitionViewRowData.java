package classes;

import classes.model.Chip;
import classes.model.Lap;

/**
 * Diese Klasse ist das DataModel f�r das TableView
 * in dem CompetitionView.
 *
 */
public class CompetitionViewRowData {

    private Chip chip;
	private Lap lap;
    
    public CompetitionViewRowData(Chip chip, Lap lap) {
        this.chip = chip;
        this.lap = lap;
    }
    
    public Chip getChip() { 
    	return chip; 
	}
    
    public Lap getRound() { 
    	return lap; 
	}
}
