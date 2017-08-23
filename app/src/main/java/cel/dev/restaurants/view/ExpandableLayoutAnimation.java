package cel.dev.restaurants.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import cel.dev.restaurants.utils.Values;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

/** This class is a custom Animation which
 *  allows for the expansion or collapse of a view during
 *  a certain time
 *  This Animation both supports collapse or expansion deepening on the expand
 *  parameter in the constructor of the class
 *
 *  The height of the View passed in the constructor will
 *  be decrease/increase until the target size is reached
 * */
public class ExpandableLayoutAnimation extends Animation {

    private final View expandableLayout;
    private final boolean expand;
    private final int targetHeight;

    public ExpandableLayoutAnimation(View expandableLayout, boolean expand) {
        this.expandableLayout = expandableLayout;
        this.expand = expand;
        if (expand) {
            //Measures what height the layout would have if it was visible and had wrap_content as height
            expandableLayout.measure(MATCH_PARENT, WRAP_CONTENT);
            //this height is stored in target height and will be the final height when the layout have expanded
            targetHeight = expandableLayout.getMeasuredHeight();
            //sets the height to 1 which will be the start
            expandableLayout.getLayoutParams().height = 1;
            //sets the visibility of the layout before starting the transform
            expandableLayout.setVisibility(View.VISIBLE);
        } else {
            //target height will here be used as the original height during the collapse animation
            targetHeight = expandableLayout.getMeasuredHeight();
        }
        setDuration(Values.ON_EXPAND_COLLAPSE_EXPANDABLE_VIEW_ANIMATION_TIME);
    }


    /** The transform will happen between the interpolated time 0 -> 1
     *  when the time i 1 then the animation has finished
     * */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (expand) {
            /*  If interpolated time is equal to 1 (animation has finished)
            *   the height will be set to WRAP_CONTENT, otherwise the height will be set to
            *   a percentage of the target height (since interpolated time range from 0 to 1
            *   it can be used to create a linear animation)
            * */
            expandableLayout.getLayoutParams().height = interpolatedTime == 1
                    ? ViewGroup.LayoutParams.WRAP_CONTENT
                    : (int) (targetHeight * interpolatedTime);
            expandableLayout.requestLayout();
        } else {
            if (interpolatedTime == 1) {
                //if the animation has been finished the visibility of the layout will be set to gone
                expandableLayout.setVisibility(View.GONE);
            } else {
                /*  The height of the of the layout will be decreased by calculating
                *   Original height - (original height * percentage of animation completion (which is interpolated time))
                * */
                expandableLayout.getLayoutParams().height = targetHeight - (int) (targetHeight * interpolatedTime);
                expandableLayout.requestLayout();
            }
        }
    }
}
