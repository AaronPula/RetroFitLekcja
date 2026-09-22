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
}
