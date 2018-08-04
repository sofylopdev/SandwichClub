package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static final String JSON_TAG = JsonUtils.class.getName();

    public static final String NAME_JSON = "name";
    public static final String MAIN_NAME_JSON = "mainName";
    public static final String ALSO_KNOWN_JSON = "alsoKnownAs";
    public static final String ORIGIN_JSON = "placeOfOrigin";
    public static final String DESCRIPTION_JSON = "description";
    public static final String IMAGE_JSON = "image";
    public static final String INGREDIENTS_JSON = "ingredients";

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();
        List<String> alsoKnownAs = new ArrayList();
        List<String> ingredients = new ArrayList<>();

        try {
            JSONObject sandwichJSONObject = new JSONObject(json);
            JSONObject name = sandwichJSONObject.optJSONObject(NAME_JSON);
            String mainName = name.optString(MAIN_NAME_JSON);
            sandwich.setMainName(mainName);

            JSONArray alsoKnownAsArray = name.optJSONArray(ALSO_KNOWN_JSON);
            if (alsoKnownAsArray.length() > 0) {
                for (int i = 0; i < alsoKnownAsArray.length(); i++) {
                    String eachExtraName = alsoKnownAsArray.optString(i);
                    alsoKnownAs.add(eachExtraName);
                }
            }
            sandwich.setAlsoKnownAs(alsoKnownAs);

            String placeOfOrigin = sandwichJSONObject.optString(ORIGIN_JSON);
            if (!placeOfOrigin.isEmpty()) {
                sandwich.setPlaceOfOrigin(placeOfOrigin);
            }

            String description = sandwichJSONObject.optString(DESCRIPTION_JSON);
            sandwich.setDescription(description);

            String imageUrl = sandwichJSONObject.optString(IMAGE_JSON);
            sandwich.setImage(imageUrl);

            JSONArray ingredientsArray = sandwichJSONObject.optJSONArray(INGREDIENTS_JSON);
            for (int j = 0; j < ingredientsArray.length(); j++) {
                String ingredient = ingredientsArray.optString(j);
                ingredients.add(ingredient);
            }
            sandwich.setIngredients(ingredients);


        } catch (JSONException e) {
            Log.e(JSON_TAG, "Couldn't parse JSON string: " + e.getMessage());
        }
        return sandwich;
    }
}
