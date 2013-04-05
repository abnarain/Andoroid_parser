package dkohl.gatech.typing.classification;

import dkohl.gatech.typing.error.ErrorTypes;

public class Classification {

    private char typed;
    private ErrorTypes classification;
    private int acceleration;
    private int velocity;
    private int time;
    
    public Classification(char typed, ErrorTypes classification, int time, int velocity, int acceleration) {
	super();
	this.setTime(time);
	this.setVelocity(velocity);
	this.setAcceleration(acceleration);
	this.typed = typed;
	this.classification = classification;
    }
    
    public Classification(char typed, ErrorTypes classification) {
	super();
	this.setTime(0);
	this.setVelocity(0);
	this.setAcceleration(0);
	this.typed = typed;
	this.classification = classification;
    }

    
    public char getTyped() {
        return typed;
    }
    
    public void setTyped(char typed) {
        this.typed = typed;
    }
    
    public ErrorTypes getClassification() {
        return classification;
    }
    
    public void setClassification(ErrorTypes classification) {
        this.classification = classification;
    }
    
    @Override
    public String toString() {

	return classification + "(" + typed + ")";
    }

    public int getAcceleration() {
	return acceleration;
    }

    public void setAcceleration(int acceleration) {
	this.acceleration = acceleration;
    }

    public int getVelocity() {
	return velocity;
    }

    public void setVelocity(int velocity) {
	this.velocity = velocity;
    }

    public int getTime() {
	return time;
    }

    public void setTime(int time) {
	this.time = time;
    }

}
