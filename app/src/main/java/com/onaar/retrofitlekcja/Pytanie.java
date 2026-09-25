package com.onaar.retrofitlekcja;

import com.google.gson.annotations.SerializedName;

public class Pytanie {
    @SerializedName("tresc")
    public String tresc;
    @SerializedName("odp_a")
    public String odp_a;
    @SerializedName("odp_b")
    public String odp_b;
    @SerializedName("odp_c")
    public String odp_c;
    @SerializedName("odp_d")
    public String odp_d;
    @SerializedName("poprawna")
    public int poprawna;

    public Pytanie(String tresc, String odp_a, String odp_b, String odp_c, String odp_d, int poprawna) {
        this.tresc = tresc;
        this.odp_a = odp_a;
        this.odp_b = odp_b;
        this.odp_c = odp_c;
        this.odp_d = odp_d;
        this.poprawna = poprawna;
    }
}
