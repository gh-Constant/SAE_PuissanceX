package boardifier.model.animation;

import boardifier.model.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclicFaceAnimation extends FaceAnimation {
    // number of cycles, each cycle contains all possibilities of numbers from 0 to nbLook-1
    protected int nbCycles;
    // is each sequence must be radomly shuffled
    protected boolean randomSequence;

    public CyclicFaceAnimation(Model model, List<Integer> faceIndexes, int nbCycles, int waitTime, boolean randomSequence) {
        super(model, faceIndexes, waitTime);
        this.randomSequence = randomSequence;
        if (randomSequence) {
            type = AnimationTypes.getType("look/random");
        }
        else {
            type = AnimationTypes.getType("look/sequence");
        }
        this.nbCycles = nbCycles;
        // WARNING : the dev will have to explicitly call computePoints()
    }

    public CyclicFaceAnimation(Model model, int nbFaces, int nbCycles, int waitTime, boolean randomSequence) {
        this(model, null, nbCycles, waitTime, randomSequence);
        List<Integer> indexes = new ArrayList<>();
        for(int i=0;i<nbFaces;i++) indexes.add(i);
        faceIndexes = indexes;
    }

    public void computeSteps() {
        List<Integer> num = new ArrayList<>();
        for(int i=0;i<faceIndexes.size();i++) num.add(faceIndexes.get(i));
        for(int i=0;i<nbCycles;i++) {
            if (randomSequence) {
                Collections.shuffle(num);
            }
            for(int j=0;j<faceIndexes.size();j++) {
                int val = num.get(j);
                for(int k=0;k<waitTime/frameGap;k++) {
                    AnimationStep step = new AnimationStep();
                    step.addData(val);
                    steps.add(step);
                }
            }
        }
    }
}
