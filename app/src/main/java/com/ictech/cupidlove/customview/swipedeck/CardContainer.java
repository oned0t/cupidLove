package com.ictech.cupidlove.customview.swipedeck;

import android.view.View;

import com.ictech.cupidlove.customview.swipedeck.Utility.SwipeCallback;
import com.ictech.cupidlove.customview.swipedeck.Utility.SwipeListener;


/**
 * Created by aaron on 21/08/2016.
 */
public class CardContainer {

    private View view;
    int positionWithinViewGroup = -1;
    int positionWithinAdapter = -1;
    private SwipeListener swipeListener;
    private SwipeCallback callback;
    private SwipeDeck parent;
    private long id;
    private int swipeDuration = SwipeDeck.ANIMATION_DURATION;
    public View leftImageView, rightImageview, topImageView;

    public CardContainer(View view, SwipeDeck parent, SwipeCallback callback) {
        this.view = view;
        this.parent = parent;
        this.callback = callback;

        setupSwipeListener();
    }

    public void setPositionWithinViewGroup(int pos) {
        this.positionWithinViewGroup = pos;
    }

    public int getPositionWithinViewGroup() {
        return positionWithinViewGroup;
    }

    public View getCard() {
        return this.view;
    }

    public void cleanupAndRemoveView() {
        //wait for card to render off screen, do cleanup and remove from viewgroup
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                deleteViewFromSwipeDeck();
            }
        }, swipeDuration);
    }

    private void deleteViewFromSwipeDeck() {
        parent.removeView(view);
        parent.removeFromBuffer(this);
    }

    public void setSwipeEnabled(boolean enabled) {
        //also checks in case user doesn't want to be able to swipe the card freely
        if (enabled && parent.SWIPE_ENABLED) {
            view.setOnTouchListener(swipeListener);
        } else {
            view.setOnTouchListener(null);
        }
    }

    public void setLeftImageResource(int leftImageResource) {
        View left = view.findViewById(leftImageResource);
        left.setAlpha(0);
        swipeListener.setLeftView(left);
        leftImageView = left;
    }


    public void setRightImageResource(int rightImageResource) {
        View right = view.findViewById(rightImageResource);
        right.setAlpha(0);
        swipeListener.setRightView(right);
    }

    public void setTopImageResource(int topImageResource) {
        View top = view.findViewById(topImageResource);
        top.setAlpha(0);
        swipeListener.setTopView(top);
    }

    public void setupSwipeListener() {
        this.swipeListener = new SwipeListener(
                view,
                callback,
                parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.ROTATION_DEGREES,
                parent.OPACITY_END,
                parent
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPositionWithinAdapter(int position) {
        this.positionWithinAdapter = position;
    }

    public int getPositionWithinAdapter() {
        return positionWithinAdapter;
    }

    public void swipeCardLeft(int duration) {
        // Remember how long card would be animating
        swipeDuration = duration;
        // Disable touch events
        setSwipeEnabled(false);
        swipeListener.swipeCardLeft(duration);
    }

    public void swipeCardRight(int duration) {
        swipeDuration = duration;
        setSwipeEnabled(false);
        swipeListener.swipeCardRight(duration);
    }

    public void swipeCardTop(int duration) {
        swipeDuration = duration;
        setSwipeEnabled(false);
        swipeListener.swipeCardTop(duration);
    }

//            public void swipeCardBottom(int duration) {
//                swipeDuration = duration;
//                setSwipeEnabled(false);
//             swipeListener.swipeCardBottom(duration);
//            }
}
