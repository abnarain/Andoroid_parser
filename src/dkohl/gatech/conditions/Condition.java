package dkohl.gatech.conditions;

import dkohl.gatech.typing.model.FlaggableCharacter;

/**
 * A condition function interface
 * applies evaluates to true when
 * a condition concerning the character
 * is met
 * 
 * @author Daniel Kohlsdorf
 */
public interface Condition {

    public boolean applies(FlaggableCharacter chr);
    
}
