package LearningMethods;

import java.util.ArrayList;
import Math.My_Math;

public class randomRotations {
	My_Math m = new My_Math();
	ArrayList<Float> rotations = new ArrayList<Float>(); //keeps track of previous rotations

public int getRotationsLength(){
	return rotations.size(); //returns the size of all the rotations
}

public void cleanRotation(){
	rotations.removeAll(rotations); //gets rid of all the previous rotations
}

//pulls a random rotation between PI/4 and -PI/4, i've experimented that these are the best limitations for this experiment
public float pullRotation(){
	float r = m.map((float)Math.random(),0,1,(float)-Math.PI/4, (float)Math.PI/4);
	rotations.add(r);
	return r;
}
}
