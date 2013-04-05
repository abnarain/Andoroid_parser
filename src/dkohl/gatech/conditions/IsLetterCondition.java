package dkohl.gatech.conditions;

import dkohl.gatech.typing.model.FlaggableCharacter;

/**
 * Evaluates valid input character
 * 
 * @author Daniel Kohlsdorf
 */
public class IsLetterCondition implements Condition {

    @Override
    public boolean applies(FlaggableCharacter chr) {
	// is the character a space or a letter
	return Character.isLetter(chr.getSymbol()) || chr.getSymbol() == ' ';
    }

}
