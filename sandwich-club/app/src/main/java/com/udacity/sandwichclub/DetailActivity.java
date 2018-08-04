package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView originTv;
    private TextView descriptionTv;
    private TextView ingredientsTv;
    private TextView alsoKnownTv;
    private TextView alsoKnownLabel;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        originTv = findViewById(R.id.origin_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);
        alsoKnownTv = findViewById(R.id.also_known_tv);
        alsoKnownLabel = findViewById(R.id.also_known_label);
        constraintLayout = findViewById(R.id.constraint_layout);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        if (!TextUtils.isEmpty(sandwich.getPlaceOfOrigin())) {
            originTv.setText(sandwich.getPlaceOfOrigin());
        } else {
            originTv.setText(R.string.unknown_origin);
        }

        descriptionTv.setText(sandwich.getDescription());

        List<String> listOfIngredients = sandwich.getIngredients();
        int lastIngredientPosition = listOfIngredients.size() - 1;
        for (String ingredient : listOfIngredients) {
            if (ingredient.equals(listOfIngredients.get(lastIngredientPosition))) {
                ingredientsTv.append("- " + ingredient.toLowerCase());
                break;
            }
            ingredientsTv.append("- " + ingredient.toLowerCase() + "\n" + "\n");
        }

        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
        if (!alsoKnownAsList.isEmpty()) {
            alsoKnownTv.setText(TextUtils.join(", ",alsoKnownAsList));

        } else {
            alsoKnownTv.setVisibility(View.GONE);
            alsoKnownLabel.setVisibility(View.GONE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.ingredients_label, ConstraintSet.TOP, R.id.origin_tv, ConstraintSet.BOTTOM);
            constraintSet.applyTo(constraintLayout);
        }
    }
}
