package com.onaar.retrofitlekcja;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET
    public Call<ArrayList<Pytanie>> getPytania();
}
