package app.wsu.littledaffy.model;

public class CollarDto {
    private String codigoCollar, id, mascota, user;

    public CollarDto(String codigoCollar, String id, String mascota, String user) {
        this.codigoCollar = codigoCollar;
        this.id = id;
        this.mascota = mascota;
        this.user = user;
    }

    public String getCodigoCollar() {
        return codigoCollar;
    }

    public void setCodigoCollar(String codigoCollar) {
        this.codigoCollar = codigoCollar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public CollarDto(){}
}
