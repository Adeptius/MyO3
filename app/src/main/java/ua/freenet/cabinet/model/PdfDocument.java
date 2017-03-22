package ua.freenet.cabinet.model;





public enum PdfDocument {

    PRAVILA_BONUSOV("https://o3.ua/content/files/pravila_bonusnoi_sistemi.pdf"),
    PUBLICHNIY_DOGOVOR("https://o3.ua/content/files/publichnyj__dogovir.pdf");

    private String url;

    public String getUrl() {
        return url;
    }

    PdfDocument(String url) {
        this.url = url;
    }
}
