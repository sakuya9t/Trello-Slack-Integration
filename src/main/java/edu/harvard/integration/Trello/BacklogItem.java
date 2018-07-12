package edu.harvard.integration.Trello;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.harvard.integration.Trello.BacklogColumn.BACKLOG;
import static edu.harvard.integration.Trello.BacklogColumn.DONE;
import static edu.harvard.integration.Trello.BacklogColumn.IN_PROGRESS;


public class BacklogItem {
    private String title, description;
    private BacklogColumn column;
    private int storyPoints = 0;
    private Calendar dateCompleted;
    private String list = "";

    public BacklogItem(@Nonnull String title, int storyPoints, @Nonnull BacklogColumn column) {
        this(title, null, storyPoints, column, null);
    }

    public BacklogItem(@Nonnull String title, @Nullable Calendar dateCompleted, int storyPoints, @Nonnull BacklogColumn column) {
        this(title, dateCompleted, storyPoints, column, null);
    }

    public BacklogItem(@Nonnull String title, @Nullable Calendar dateCompleted, int storyPoints, @Nonnull BacklogColumn column, @Nullable String description) {
        this.storyPoints = storyPoints;
        this.title = title;
        this.description = description;
        this.dateCompleted = dateCompleted;
        this.column = column;
    }

    public BacklogItem(Map<String, Object> map){
        this.title = (String) map.get("name");
        this.column = this.convertList((String) map.get("list_name"));
        this.list = (String) map.get("list_name");
        this.dateCompleted = this.convertDate((String) map.get("due"));
        this.description = (String) map.get("desc");
        if(map.get("pts") != null) this.storyPoints = Integer.parseInt((String) map.get("pts"));
    }

    public int getStoryPoints() {
        return this.storyPoints;
    }
    public void setStoryPoints(String sp) {
        if(sp != null && sp != "") this.storyPoints = Integer.parseInt(sp);
    }

    @Nonnull
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title){this.title = title;}

    @Nullable
    public String getDescription() {
        return this.description;
    }

    @Nullable
    public Calendar getDateCompleted() {
        return this.dateCompleted;
    }

    @Nonnull
    public BacklogColumn getColumn() {
        return this.column;
    }

    public boolean equals(BacklogItem another){
        if(!this.title.equals(another.getTitle())) return false;
        if(!this.description.equals(another.getDescription())) return false;
        if(this.storyPoints != another.storyPoints) return false;
        return true;
    }

    private Calendar convertDate(String d){
        if(d == null || d == "") return null;
        Pattern pattern = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}");
        Matcher m = pattern.matcher(d);
        if(m.find()){
            String strdate = m.group();
            String[] arrdate = strdate.split("-");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Integer.parseInt(arrdate[0]));
            c.set(Calendar.MONTH, Integer.parseInt(arrdate[1]) - 1);
            c.set(Calendar.DATE, Integer.parseInt(arrdate[2]));
            return c;
        }
        return null;
    }

    private String dateStr(){
        if(this.dateCompleted == null) return "";
        return (new SimpleDateFormat("MM/dd/yyyy")).format(this.dateCompleted.getTime());
    }

    private BacklogColumn convertList(String list){
        String s = list.toLowerCase();
        if(s.contains("done")) return DONE;
        if(s.contains("progress")) return IN_PROGRESS;
        return BACKLOG;
    }

    public String toString(){
        return "PBI Title: " + this.title +
                "\nStory points: " + this.storyPoints +
                "\nBelongs to list: " + this.list +
                "\nStatus: " + this.column +
                "\nComplete Date: " + this.dateStr() +
                "\nDescription: " + this.description +
                "\n\n";
    }

    public String toSimpleString(){
        return "PBI Title: " + this.title +
                "\nStory points: " + this.storyPoints +
                "\nBelongs to list: " + this.list +
                "\nStatus: " + this.column +
                "\nComplete Date: " + this.dateStr() +
                "\n\n";
    }
}