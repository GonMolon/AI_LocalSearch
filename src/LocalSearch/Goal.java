package LocalSearch;

import aima.search.framework.GoalTest;

public class Goal implements GoalTest {
    @Override
    public boolean isGoalState(Object o) {
        return true;
    }
}
