package boardifier.model.animation;

import java.util.ArrayList;
import java.util.List;

/**
 * boardifier.model.Animation.AnimationStep is a simple wrapper to represent a step of an boardifier.model.Animation.Animation
 * In case of boardifier.model.Animation.MoveAnimation, data will contain 2D/3D coordinates of the next position
 * In case of a LookAnimation, data will contain some parameters to modify the visual aspect.
 */
public class AnimationStep {
    protected List<Double> data;

    public AnimationStep() {
        data = new ArrayList<>();
    }

    public AnimationStep(List<Double> data) {
        this.data = data;
    }

    public void addData(double value) {
        data.add(value);
    }

    public List<Double> getData() {
        return data;
    }

    public int getInt(int index) {
        double d = data.get(index);
        return (int) d;
    }

    public double getDouble(int index) {
        return data.get(index);
    }
}
