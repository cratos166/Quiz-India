package com.nbird.multiplayerquiztrivia.TOURNAMENT.MODEL;

public class ChatHolder {
    String name="";
    String answer="";
    long time=0;


    public ChatHolder() {
    }

    public ChatHolder(String name, String answer, long time) {
        this.name = name;
        this.answer = answer;
        this.time = time;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
