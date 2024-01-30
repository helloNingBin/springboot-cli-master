package example.concurrent.bean;

public class FileBean {
    int id;
    String detail;
    String file;

    public FileBean(int id, String detail, String file) {
        this.id = id;
        this.detail = detail;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
