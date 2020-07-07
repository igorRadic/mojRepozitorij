package hr.ferit.igorradic.reversechordy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Theory {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("about")
    @Expose
    private String about;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
