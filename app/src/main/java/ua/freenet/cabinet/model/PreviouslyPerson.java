package ua.freenet.cabinet.model;


public class PreviouslyPerson {

    private String fio;
    private String card;
    private String pass;
    private String address;

    public PreviouslyPerson() {
    }

    public PreviouslyPerson(String fio, String card, String pass, String address) {
        this.fio = fio;
        this.card = card;
        this.pass = pass;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreviouslyPerson that = (PreviouslyPerson) o;

        return card != null ? card.equals(that.card) : that.card == null;

    }

    @Override
    public int hashCode() {
        return card != null ? card.hashCode() : 0;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
