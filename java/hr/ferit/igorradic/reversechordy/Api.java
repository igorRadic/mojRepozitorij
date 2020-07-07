package hr.ferit.igorradic.reversechordy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface Api {

    @GET("chords.json")
    Call<List<Theory>> getInfo();

}
