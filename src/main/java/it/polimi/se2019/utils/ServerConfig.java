package it.polimi.se2019.utils;

public class ServerConfig {
    private int waitingTimeInLobby;
    private int answerTimeLimit;
    private String host;
    private int rmiPort;
    private int socketPort;

    public long getWaitingTimeInLobbyMs() {
        return waitingTimeInLobby * 1000L; // Convert seconds to milliseconds.
    }

    public long getTurnTimeLimitMs() {
        return answerTimeLimit * 1000L; // Convert seconds to milliseconds.
    }

    public String getHost() {
        return host;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public int getSocketPort() {
        return socketPort;
    }
}