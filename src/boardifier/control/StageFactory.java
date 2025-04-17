package boardifier.control;

import boardifier.model.GameException;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.view.GameStageView;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class StageFactory {

    protected static Map<String, String> stageModelNames;
    protected static Map<String, String> stageViewNames;
    static {
        stageModelNames = new HashMap<>();
        stageViewNames = new HashMap<>();
    }

    public static void registerModelAndView(String stageName, String modelClassName, String viewClassName) {
        stageModelNames.put(stageName, modelClassName); stageViewNames.put(stageName, viewClassName);
    }

    public static GameStageModel createStageModel(String stageName, Model model) throws GameException {
        if (stageModelNames.containsKey(stageName)) {
            String className = stageModelNames.get(stageName);
            GameStageModel stageModel = null;
            try {
                Class classDefinition = Class.forName(className);
                Class[] params = new Class[2];
                params[0] = String.class;
                params[1] = Model.class;
                Object o = classDefinition.getDeclaredConstructor(params).newInstance(stageName, model);
                stageModel = (GameStageModel) o;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return stageModel;
        }
        throw new GameException("Invalid stage name: "+stageName+". Cannot create an instance of StageModel");
    }
    public static GameStageView createStageView(String stageName, GameStageModel model) throws GameException{
        if (stageViewNames.containsKey(stageName)) {
            String className = stageViewNames.get(stageName);
            GameStageView stageView = null;
            try {
                Class classDefinition = Class.forName(className);
                Class[] params = new Class[2];
                params[0] = String.class;
                params[1] = GameStageModel.class;
                Object o = classDefinition.getDeclaredConstructor(params).newInstance(stageName, model);
                stageView = (GameStageView) o;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return stageView;
        }
        throw new GameException("Invalid stage name: "+stageName+". Cannot create an instance of StageView");
    }
}
