package it.polimi.se2019.utils;

import it.polimi.se2019.model.gamemap.Coordinates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains a question and the possible answers.
 *
 * @author Marchingegno
 */
public class QuestionContainer implements Serializable {

    private final String question;
    private final boolean askString;
    private final boolean askCoordinates;

    private ArrayList<String> options;
    private ArrayList<Coordinates> coordinates;


    private QuestionContainer(String question, boolean askString, boolean askCoordinates) {
        this.question = question;
        this.askString = askString;
        this.askCoordinates = askCoordinates;
    }


    public static QuestionContainer createStringQuestionContainer(String question, List<String> options) {
        QuestionContainer questionContainer = new QuestionContainer(question, true, false);
        questionContainer.options = new ArrayList<>(options);
        return questionContainer;
    }

    public static QuestionContainer createCoordinatesQuestionContainer(String question, List<Coordinates> coordinates) {
        QuestionContainer questionContainer = new QuestionContainer(question, false, true);
        questionContainer.coordinates = new ArrayList<>(coordinates);
        return questionContainer;
    }

    ////////TEMPORARY METHOD//////////

    /**
     * Returns true if this QuestionContainer does not contain anything.
     * For example, the array of targets of the weapon is void.
     *
     * @return true if this QuestionContainer does not contain anything.
     */
    public boolean isThisQuestionContainerUseless() {
        if (isAskCoordinates() && (getCoordinates() == null || getCoordinates().isEmpty()))
            return true;
        if (isAskString())
            return getOptions() == null || getOptions().isEmpty();
        return false;
    }


    public boolean isAskString() {
        return askString;
    }

    public boolean isAskCoordinates() {
        return askCoordinates;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Coordinates> getCoordinates() {
        return coordinates;
    }
}
