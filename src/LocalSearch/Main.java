package LocalSearch;

public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem(15, 1, 123);
        State state = InitialStateFactory.generateInitialState(problem);
    }
}
