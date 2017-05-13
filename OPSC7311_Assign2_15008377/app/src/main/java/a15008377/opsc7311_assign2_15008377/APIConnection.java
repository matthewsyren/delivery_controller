package a15008377.opsc7311_assign2_15008377;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by matthew on 2017/02/03.
 * Class fetches JSON returned from the appropriate API in an AsyncTask
 */

public class APIConnection extends AsyncTask<String, Void, String> {

    //Declares an object of the IAPIConnectionResponse interface, which will be used to send the JSON back to the  thread
    public IAPIConnectionResponse delegate = null;

    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

    //Method retrieves the JSON returned from the API
    protected String doInBackground(String... urls) {
        try {
            HttpURLConnection urlConnection = null;
            try {
                BufferedReader bufferedReader = null;
                StringBuilder stringBuilder = new StringBuilder();

                for(int i = 0; i < urls.length; i++){
                    URL url = new URL(urls[i]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    bufferedReader  = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if(urls.length > 1 && i < urls.length - 1){
                            stringBuilder.append(line).append(",\n");
                        }
                        else{
                            stringBuilder.append(line).append("\n");
                        }
                    }
                }

                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    //Method passes the JSON back to the Main thread (to the point from which this class was instantiated)
    protected void onPostExecute(String response) {
        if(delegate != null){
            delegate.getJsonResponse(response);
        }
    }
}