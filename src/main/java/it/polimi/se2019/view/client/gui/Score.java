package it.polimi.se2019.view.client.gui;

public class Score {
    private String nickname;
    private String rank;
    private String points;

    public Score(String nickname, int rank, int points) {
        this.nickname = nickname;
        this.rank = Integer.toString(rank);
        this.points = Integer.toString(points);
    }

    public String getNickname() {
        return nickname;
    }

    public String getRank() {
        return rank;
    }

    public String getPoints() {
        return points;
    }

    public String toString() {
        return "[" + rank + "]" + nickname + ": " + points;
    }
}
