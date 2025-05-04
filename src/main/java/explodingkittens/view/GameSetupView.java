package explodingkittens.view;

public interface GameSetupView {
    String promptNickname(int playerIndex);
    void showError(String message);
}
