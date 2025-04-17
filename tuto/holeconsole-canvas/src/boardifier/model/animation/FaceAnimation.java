package boardifier.model.animation;

import boardifier.model.Model;

import java.util.ArrayList;
import java.util.List;

public class FaceAnimation extends Animation {
    // the list of possible index of faces that are used during this animation
    protected List<Integer> faceIndexes;
    // waits : waiting time between a change in look. Will be rounded to the closest multiples of frameGap
    protected int waitTime;


    public FaceAnimation(Model model, List<Integer> faceindexes, int waitTime) {
        super(model, AnimationTypes.getType("look/simple"));
        this.faceIndexes = faceindexes;
        if (waitTime < frameGap) waitTime = frameGap;
        waitTime = (waitTime/frameGap)*frameGap;
        this.waitTime = waitTime;
        // WARNING : the dev will have to explicitly call computePoints()
    }

    public FaceAnimation(Model model, int nbFaces, int waitTime) {
        this(model, null, waitTime);
        List<Integer> indexes = new ArrayList<>();
        for(int i=0;i<nbFaces;i++) indexes.add(i);
        faceIndexes = indexes;

    }

    public void computeSteps() {
        for(int i = 0; i< faceIndexes.size(); i++) {
            int val = faceIndexes.get(i);
            for(int j=0;j<waitTime/frameGap;j++) {
                AnimationStep step = new AnimationStep();
                step.addData(val);
                steps.add(step);
            }
        }
    }
}
