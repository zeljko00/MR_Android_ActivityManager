package etf.mr.project.activitymanager.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activity {
    private long id;
    private String title;
    private String desc;
    private String type;
    private String address;
    private double x;
    private double y;
    private Date starts;
    private Date ends;

    private List<String> imgs=new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Date getStarts() {
        return starts;
    }

    public void setStarts(Date starts) {
        this.starts = starts;
    }

    public Date getEnds() {
        return ends;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", starts=" + starts +
                ", ends=" + ends +
                ", imgs=" + imgs +
                '}';
    }
}
