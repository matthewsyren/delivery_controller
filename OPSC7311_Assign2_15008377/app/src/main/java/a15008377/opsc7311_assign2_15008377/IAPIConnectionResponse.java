package a15008377.opsc7311_assign2_15008377;

/**
 * Created by matthew on 2017/02/03.
 * This class is used to parse the JSON returned from an API
 */

public interface IAPIConnectionResponse {
    //Method is used to get JSON from an API. The class that needs the data will implement this interface, and the APIConnection class sends the data to the method once it has fetched the data
    public void getJsonResponse(String response);
}
