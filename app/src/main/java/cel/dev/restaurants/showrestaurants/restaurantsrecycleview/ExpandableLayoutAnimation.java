package cel.dev.restaurants.showrestaurants.restaurantsrecycleview;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import cel.dev.restaurants.utils.Values;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class ExpandableLayoutAnimation extends Animation {

    private final View expandableLayout;
    private final boolean expand;
    private final int targetHeight;

    public ExpandableLayoutAnimation(View expandableLayout, boolean expand) {
        this.expandableLayout = expandableLayout;
        this.expand = expand;
        if (expand) {
            expandableLayout.measure(MATCH_PARENT, WRAP_CONTENT);
            targetHeight = expandableLayout.getMeasuredHeight();
            expandableLayout.getLayoutParams().height = 1;
            expandableLayout.setVisibility(View.VISIBLE);
        } else {
            targetHeight = expandableLayout.getMeasuredHeight();
        }
        setDuration(Values.ON_EXPAND_COLLAPSE_EXPANDABLE_VIEW_ANIMATION_TIME);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (expand) {
            expandableLayout.getLayoutParams().height = interpolatedTime == 1
                    ? ViewGroup.LayoutParams.WRAP_CONTENT
                    : (int)(targetHeight * interpolatedTime);
            expandableLayout.requestLayout();
        } else {
            if(interpolatedTime == 1){
                expandableLayout.setVisibility(View.GONE);
            }else{
                expandableLayout.getLayoutParams().height = targetHeight - (int)(targetHeight * interpolatedTime);
                expandableLayout.requestLayout();
            }
        }
    }
}