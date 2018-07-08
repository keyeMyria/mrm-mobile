package com.andela.mrm.login_flow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.mrm.R;

/**
 * ImageSliderAdapter class.
 * Adds the three onBoarding views
 */

public class ImageSliderAdapter extends PagerAdapter {

    private final int[] slideImages = {
            R.drawable.ic_meeting, R.drawable.ic_room_status, R.drawable.ic_rating
    };

    private final int[] slideHeaders = {
            R.string.onBoarding_header_one,
            R.string.onBoarding_header_two,
            R.string.onBoarding_header_three
    };

    private final int[] slideDescriptions = {
            R.string.onBoarding_description_one,
            R.string.onBoarding_description_two,
            R.string.onBoarding_description_three
    };

    @Override
    public int getCount() {
        return slideHeaders.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;

        View view = layoutInflater
                .inflate(R.layout.fragment_on_boarding, container, false);

        ImageView slideImage = view.findViewById(R.id.image_slide);
        TextView slideHeading = view.findViewById(R.id.text_header);
        TextView slideDescription = view.findViewById(R.id.text_description);

        slideImage.setImageResource(slideImages[position]);
        slideHeading.setText(slideHeaders[position]);
        slideDescription.setText(slideDescriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
